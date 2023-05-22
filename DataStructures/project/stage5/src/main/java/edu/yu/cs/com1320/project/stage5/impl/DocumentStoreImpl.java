package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.stage5.DocumentStore;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.impl.BTreeImpl;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.Undoable;
import edu.yu.cs.com1320.project.MinHeap;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.io.InputStream;
import java.net.URI;
import java.util.function.Function;


public class DocumentStoreImpl implements DocumentStore {
    /**
     * Compares 
     */
    private class URIUseTimeComparator implements Comparable<URIUseTimeComparator> {
        private final URI uri;
        private long lastUseTime;

        public URIUseTimeComparator(URI uri) {
            this.uri = uri;
            this.lastUseTime = docStore.get(uri).getLastUseTime();
        }

        @Override
        public int compareTo(URIUseTimeComparator otherURI) {
            return (int) (docStore.get(this.uri).getLastUseTime() - docStore.get(otherURI.uri).getLastUseTime());
        }

        @Override
        public boolean equals(Object obj) {
            if(this == obj) {
                return true;
            }

            if(obj == null) {
                return false;
            }

            if(getClass() != obj.getClass()) {
                return false;
            }
            URIUseTimeComparator otherURI = (URIUseTimeComparator) obj;
            return this.uri.equals(otherURI.uri);
        }
    }

    private class URIWordComparator implements Comparator<URI> {
        String word;

        public URIWordComparator(String word) {
            this.word = word;
        }
        public int compare(URI uri, URI otherURI) {
            return Integer.compare(docStore.get(uri).wordCount(word), docStore.get(otherURI).wordCount(word));
        }
    }

    private class URIPrefixComparator implements Comparator<URI> {
        String prefix;

        public URIPrefixComparator(String prefix) {
            this.prefix = prefix;
        }

        public int compare(URI uri, URI otherURI) {
            int doc1PrefixCount = countPrefixOccurrences(uri);
            int doc2PrefixCount = countPrefixOccurrences(otherURI);
    
            return Integer.compare(doc1PrefixCount, doc2PrefixCount);
        }

        private int countPrefixOccurrences(URI uri) {
            int count = 0;
            for (String word : docStore.get(uri).getWords()) {
                if (word.startsWith(this.prefix)) {
                    count += docStore.get(uri).wordCount(word);
                }
            }
            return count;
        }
    }

    private BTreeImpl<URI, Document> docStore;
    private Set<URI> docsInMemURIs;
    private Set<URI> docsFromDiskURIs;
    private StackImpl<Undoable> undoableStack;
    private TrieImpl<URI> docTrie;
    private MinHeap<URIUseTimeComparator> docHeap;
    private DocumentPersistenceManager docPersistenceManager;

    private int docCount;
    private int docBytes;
    private int maxDocCount;
    private int maxDocBytes;

    public DocumentStoreImpl() {
        this.docStore = new BTreeImpl<URI, Document>();
        this.docsInMemURIs = new HashSet<URI>();
        this.docsFromDiskURIs = new HashSet<URI>();
        this.undoableStack = new StackImpl<Undoable>();
        this.docTrie = new TrieImpl<URI>();
        this.docHeap = new MinHeapImpl<URIUseTimeComparator>();
        this.docPersistenceManager = new DocumentPersistenceManager(null);
        this.docStore.setPersistenceManager(docPersistenceManager);

        this.docCount = 0;
        this.docBytes = 0;
        this.maxDocCount = Integer.MAX_VALUE;
        this.maxDocBytes = Integer.MAX_VALUE;
    }

    public DocumentStoreImpl(File baseDir) {
        this();
        this.docPersistenceManager = new DocumentPersistenceManager(baseDir);
        this.docStore.setPersistenceManager(docPersistenceManager);
    }

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document
     */
    public Document get(URI uri) {
        Document doc = this.docStore.get(uri);

        if (doc != null) {
            if (!this.docsInMemURIs.contains(uri)) {//doc can either be under limits or above
                putDocHeap(doc);
                enforceLimits();
            }
            else {
                doc.setLastUseTime(System.nanoTime());
                this.docHeap.reHeapify(new URIUseTimeComparator(uri));
            }
        }

        return doc;
    }

    /**
     * @param input the document being put
     * @param uri unique identifier for the document
     * @param format indicates which type of document format is being passed
     * @return if there is no previous doc at the given URI, return 0.
     * If there is a previous doc, return the hashCode of the previous doc.
     * If InputStream is null, this is a delete, and thus return either the hashCode of the deleted doc
     * or 0 if there is no doc to delete.
     * @throws IOException if there is an issue reading input
     * @throws IllegalArgumentException if uri or format are null
     */
    public int put(InputStream input, URI uri, DocumentFormat format) throws IOException {
        if (uri == null || format == null) {
            throw new IllegalArgumentException();
        }
        
        Document doc = input == null? null: format.equals(DocumentFormat.TXT)? (Document) new DocumentImpl(uri, toTXT(input), null): (Document) new DocumentImpl(uri, input.readAllBytes());
        if (input != null) {
            input.close();
            putDocHeap(uri);
        }

        addCommand(uri);

        if (!this.docStore.containsKey(uri)) {
            putDocTrie(doc);
            this.docStore.put(uri, doc);
            return 0;
        }

        removeHeapDoc(this.docStore.get(uri));
        putDocTrie(doc);
        return this.docStore.put(uri, doc).hashCode();
    }

    /**
     * @param uri the unique identifier of the document to delete
     * @return true if the document is deleted, false if no document exists with that URI
     */
    public boolean delete(URI uri) {
        //delete from heap and build in way for undo to add back to heap
        if (!this.docStore.containsKey(uri)) {
            return false;
        }

        Document doc = this.docStore.get(uri);

        for(String keyword: doc.getWords()) {
            this.docTrie.delete(keyword, doc);
        }

        addCommand(uri);

        this.docStore.put(uri, null);
        removeHeapDoc(doc);
        
        return true;
    }

    /**
     * undo the last put or delete command
     * @throws IllegalStateException if there are no actions to be undone, i.e. the command stack is empty
     */
    public void undo() throws IllegalStateException {
        if (this.undoableStack.size() == 0) {
            throw new IllegalStateException();
        }
        
        this.undoableStack.pop().undo();
    }

    /**
     * undo the last put or delete that was done with the given URI as its key
     * @param uri
     * @throws IllegalStateException if there are no actions on the command stack for the given URI
     */
    @SuppressWarnings("unchecked")
    public void undo(URI uri) throws IllegalStateException {
        StackImpl<Undoable> tempStack = new StackImpl<Undoable>();
        Boolean commandUndid = false;
        for (int i=0; i<this.undoableStack.size(); i++) {
            if (this.undoableStack.peek() instanceof CommandSet && ((CommandSet<URI>) this.undoableStack.peek()).containsTarget(uri)) {
                ((CommandSet<URI>) this.undoableStack.peek()).undo(uri);
                if (((CommandSet<URI>) this.undoableStack.peek()).size()==0) {
                    this.undoableStack.pop();
                }
                commandUndid = true;
                break;
            }
            else if (this.undoableStack.peek() instanceof GenericCommand && ((GenericCommand<URI>) this.undoableStack.peek()).getTarget().equals(uri)){
                this.undoableStack.pop().undo();
                commandUndid = true;
                break;
            }
            tempStack.push(this.undoableStack.pop());
        }

        if (!commandUndid) {
            throw new IllegalStateException();
        }

        for (int i=0; i<tempStack.size(); i++) {
            this.undoableStack.push(tempStack.pop());
        }
    }

    private Function<URI,Boolean> createUndo(URI commandURI) {
        Function<URI, Boolean> undo = uri -> {
            if (this.docStore.get(uri) != null) {
                for (String keyword: this.docStore.get(uri).getWords()) {
                    this.docTrie.delete(keyword, this.docStore.get(uri));
                }
                removeHeapDoc(this.docStore.get(uri));
            }
            StackImpl<HashMap<URI, Document>> tempArchive = new StackImpl<HashMap<URI, Document>>();
            Document doc = null;
            for (int i=0; i<archive.size(); i++) {
                if (archive.peek().containsKey(uri)) {
                    doc = archive.peek().get(uri);
                    archive.peek().remove(uri);
                    if (archive.peek().isEmpty()) archive.pop(); //normally would pop, now only pop if it is empty
                    else tempArchive.push(archive.pop());
                    break;
                }
                tempArchive.push(archive.pop());
            }
            for (int i=0; i<tempArchive.size(); i++){
                this.archive.push(tempArchive.pop());
            }
            putDocHeap(uri, doc);
            this.docStore.put(uri, doc);
            putDocTrie(doc);
            return true;
        };
        return undo;
    }

    private void addCommandSet(Set<URI> commandURIs) {
        HashMap<URI, Document> entry = new HashMap<URI, Document>();

        CommandSet<URI> newCommandSet = new CommandSet<URI>();

        for (URI uri: commandURIs) {
            entry.put(uri, this.docStore.get(uri));
        }

        this.archive.push(entry);

        for (URI uri: commandURIs) {
            newCommandSet.addCommand(new GenericCommand<URI>(uri, createUndo(uri)));
        }

        this.undoableStack.push(newCommandSet);
    }

    private void addCommand(URI commandURI) {
        HashMap<URI, Document> entry = new HashMap<URI, Document>();

        entry.put(commandURI, this.docStore.get(commandURI));

        GenericCommand<URI> newCommand = new GenericCommand<URI>(commandURI, createUndo(commandURI));
        this.undoableStack.push(newCommand);
    }

    private String toTXT(InputStream input) throws IOException {
        byte[] bytes = input.readAllBytes();

        String s = new String(bytes);
        if (input != null) input.close();
        return s;
    }

    /**
     * Retrieve all documents whose text contains the given keyword.
     * Documents are returned in sorted, descending order, sorted by the number of times the keyword appears in the document.
     * Search is CASE SENSITIVE.
     * @param keyword
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    public List<Document> search(String keyword) {
        URIWordComparator searchComparator = new URIWordComparator(keyword);
        List<URI> uris = this.docTrie.getAllSorted(keyword, searchComparator);
        List<Document> docs = new ArrayList<Document>();

        for (URI uri: uris) {
            docs.add(get(uri));
        }

        return docs;
    }

    /**
     * Retrieve all documents that contain text which starts with the given prefix
     * Documents are returned in sorted, descending order, sorted by the number of times the prefix appears in the document.
     * Search is CASE SENSITIVE.
     * @param keywordPrefix
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    public List<Document> searchByPrefix(String keywordPrefix) {
        URIPrefixComparator prefixComparator = new URIPrefixComparator(keywordPrefix);
        List<URI> uris = this.docTrie.getAllWithPrefixSorted(keywordPrefix, prefixComparator);
        List<Document> docs = new ArrayList<Document>();

        for (URI uri: uris) {
            docs.add(get(uri));
        }

        return docs;
    }

    /**
     * Completely remove any trace of any document which contains the given keyword
     * Search is CASE SENSITIVE.
     * @param keyword
     * @return a Set of URIs of the documents that were deleted.
     */
    public Set<URI> deleteAll(String keyword) {
        Set<URI> deletedURIs = new HashSet<URI>();
        Set<Document> documents = this.docTrie.deleteAll(keyword);

        for (Document doc: documents) {
            deletedURIs.add(doc.getKey());
            for (String word: doc.getWords()) {
                this.docTrie.delete(word, doc);
            }
        }


        addCommandSet(deletedURIs);

        for(URI uri: deletedURIs) {
            this.docStore.put(uri, null);
        }
        
        for (Document doc: documents) {
            removeHeapDoc(doc);
        }

        return deletedURIs;
    }

    /**
     * Completely remove any trace of any document which contains a word that has the given prefix
     * Search is CASE SENSITIVE.
     * @param keywordPrefix
     * @return a Set of URIs of the documents that were deleted.
     */
    public Set<URI> deleteAllWithPrefix(String keywordPrefix) {
        Set<URI> deletedURIs = new HashSet<URI>();
        Set<Document> documents = this.docTrie.deleteAllWithPrefix(keywordPrefix);

        for (Document doc: documents) {
            deletedURIs.add(doc.getKey());
            for (String word: doc.getWords()) {
                this.docTrie.delete(word, doc);
            }
        }

        addCommandSet(deletedURIs);

        for(URI uri: deletedURIs) {
            this.docStore.put(uri, null);
        }

        for (Document doc: documents) {
            removeHeapDoc(doc);
        }
        
        return deletedURIs;
    }

    @Override
    public void setMaxDocumentCount(int limit) {
        if (limit < 0) throw new IllegalArgumentException();

        this.maxDocCount = limit;

        while (this.docCount > this.maxDocCount) {
            removeHeapDoc();
        }
    }

    @Override
    public void setMaxDocumentBytes(int limit) {
        if (limit < 0) throw new IllegalArgumentException();

        this.maxDocBytes = limit;

        while (this.docBytes > this.maxDocBytes) {
            removeHeapDoc();
        }
    }

    @SuppressWarnings("unchecked")
    private void removeStack(URI uri) {
        StackImpl<Undoable> tempStack = new StackImpl<Undoable>();
        for (int i=0; i<this.undoableStack.size(); i++) {
            if (this.undoableStack.peek() instanceof CommandSet && ((CommandSet<URI>) this.undoableStack.peek()).containsTarget(uri)) {
                CommandSet<URI> commandSet = new CommandSet<URI>();
                for (GenericCommand<URI> command: (CommandSet<URI>) this.undoableStack.peek()) {
                    if (command.getTarget() == uri) {
                        break;
                    }
                    commandSet.addCommand(command);
                }
                this.undoableStack.pop();
                if (commandSet.size()!=0) this.undoableStack.push(commandSet);
                break;
            }
            else if (this.undoableStack.peek() instanceof GenericCommand && ((GenericCommand<URI>) this.undoableStack.peek()).getTarget().equals(uri)){
                this.undoableStack.pop();
                break;
            }
            tempStack.push(this.undoableStack.pop());
        }
        for (int i=0; i<tempStack.size(); i++) this.undoableStack.push(tempStack.pop());
    }

    private void putDocTrie(Document doc) {
        if (doc == null || doc.getDocumentTxt() == null) {
            return;
        }
        for (String word: doc.getWords()) {
            this.docTrie.put(word, doc.getKey());
        }
    }

    private void putDocHeap(Document doc) {
        if (doc == null) {
            return;
        }
        int bytes = doc.getDocumentBinaryData() == null? doc.getDocumentTxt().getBytes().length : doc.getDocumentBinaryData().length;

        doc.setLastUseTime(System.nanoTime());

        while (this.docBytes + bytes > this.maxDocBytes || this.docCount + 1 > maxDocCount) {
            removeHeapDoc();
        }
        
        this.docCount++;
        this.docBytes += bytes;
        this.docHeap.insert(new URIUseTimeComparator(doc.getKey()));
        this.docsInMemURIs.add(doc.getKey());
    }

    private void removeHeapDoc() {
        URI uri = this.docHeap.remove().uri;
        removeHeapDoc(uri);
    }

    private void removeHeapDoc(URI uri) {
        Document doc = this.docStore.get(uri);
        doc.setLastUseTime(0);
        docHeap.reHeapify(new URIUseTimeComparator(uri));
        this.docCount--;
        this.docBytes -= doc.getDocumentBinaryData() == null? doc.getDocumentTxt().getBytes().length : doc.getDocumentBinaryData().length;
        docHeap.remove();
        this.docStore.moveToDisk(uri);
        this.docsInMemURIs.remove(uri);
    }

    private void moveToDisk(URI uri) {
        if (this.docsInMemURIs.contains(uri)) {
            return;
        }
        docStore.moveToDisk(uri);
    }

    private void enforceLimits() {
        while (this.docBytes > this.maxDocBytes || this.docCount > maxDocCount) {

            removeHeapDoc();
        }
    }
}
