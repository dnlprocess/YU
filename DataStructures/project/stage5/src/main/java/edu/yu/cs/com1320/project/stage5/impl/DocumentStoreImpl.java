package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.stage5.DocumentStore;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.impl.BTreeImpl;
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
import java.util.HashSet;
import java.util.Iterator;
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

        public URIUseTimeComparator(URI uri, long lastUseTime) {
            this.uri = uri;
            this.lastUseTime = lastUseTime;
        }

        @Override
        public int compareTo(URIUseTimeComparator otherURI) {
            return (int) (this.lastUseTime - otherURI.lastUseTime);
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
                this.docHeap.reHeapify(new URIUseTimeComparator(uri, doc.getLastUseTime()));
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
            putDocHeap(doc);
        }

        addCommand(uri, docStore.get(uri));

        if (this.docStore.get(uri) == null) {
            putDocTrie(doc);
            this.docStore.put(uri, doc);
            return 0;
        }

        removeHeapDoc(uri);
        putDocTrie(doc);
        return this.docStore.put(uri, doc).hashCode();
    }

    /**
     * @param uri the unique identifier of the document to delete
     * @return true if the document is deleted, false if no document exists with that URI
     */
    public boolean delete(URI uri) {
        Document doc = this.docStore.get(uri);
        
        if (doc == null) {
            return false;
        }

        removeDocTrie(this.docStore.get(uri));

        addCommand(uri, doc);

        this.docStore.put(uri, null);
        removeHeapDoc(uri);
        
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

    private Function<URI,Boolean> createUndo(URI commandURI, Document doc) {
        Function<URI, Boolean> undo = (URI uri) -> {
            if (this.docStore.get(uri) != null) {
                removeDocTrie(this.docStore.get(uri));
                removeHeapDoc(uri);
            }

            putDocHeap(doc);
            putDocTrie(doc);
            this.docStore.put(uri, doc);
            enforceLimits();
            return true;
        };
        return undo;
    }

    private void addCommandSet(List<URI> commandURIs, List<Document> docs) {
        CommandSet<URI> newCommandSet = new CommandSet<URI>();

        Iterator<URI> uriIterator = commandURIs.iterator();
        Iterator<Document> docIterator = docs.iterator();

        while (uriIterator.hasNext() && docIterator.hasNext()) {
            newCommandSet.addCommand(new GenericCommand<URI>(uriIterator.next(), createUndo(uriIterator.next(), docIterator.next())));
        }

        this.undoableStack.push(newCommandSet);
    }

    private void addCommand(URI commandURI, Document doc) {
        GenericCommand<URI> newCommand = new GenericCommand<URI>(commandURI, createUndo(commandURI, doc));
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
        Set<URI> deletedURIs = this.docTrie.deleteAll(keyword);
        List<URI> uris  = new ArrayList<URI>();
        List<Document> docs = new ArrayList<Document>();

        for (URI uri: deletedURIs) {
            removeDocTrie(this.docStore.get(uri));
            uris.add(uri);
            docs.add(this.docStore.get(uri));
        }

        addCommandSet(uris, docs);

        for(URI uri: deletedURIs) {
            this.docStore.put(uri, null);
            removeHeapDoc(uri);
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
        Set<URI> deletedURIs = this.docTrie.deleteAllWithPrefix(keywordPrefix);
        List<URI> uris  = new ArrayList<URI>();
        List<Document> docs = new ArrayList<Document>();

        for (URI uri: deletedURIs) {
            removeDocTrie(this.docStore.get(uri));
            uris.add(uri);
            docs.add(this.docStore.get(uri));
        }

        addCommandSet(uris, docs);

        for(URI uri: deletedURIs) {
            this.docStore.put(uri, null);
            removeHeapDoc(uri);
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

    private void removeDocTrie(Document doc) {
        for(String word: doc.getWords()) {
            this.docTrie.delete(word, doc.getKey());
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
        this.docHeap.insert(new URIUseTimeComparator(doc.getKey(), doc.getLastUseTime()));
        this.docsInMemURIs.add(doc.getKey());
    }

    private void removeHeapDoc() {
        URI uri = this.docHeap.remove().uri;
        removeHeapDoc(uri);
    }

    private void removeHeapDoc(URI uri) {
        Document doc = this.docStore.get(uri);
        doc.setLastUseTime(0);
        docHeap.reHeapify(new URIUseTimeComparator(uri, doc.getLastUseTime()));
        this.docCount--;
        this.docBytes -= doc.getDocumentBinaryData() == null? doc.getDocumentTxt().getBytes().length : doc.getDocumentBinaryData().length;
        docHeap.remove();
        try {
            this.docStore.moveToDisk(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.docsInMemURIs.remove(uri);
    }

    private void moveToDisk(URI uri) {
        if (this.docsInMemURIs.contains(uri)) {
            return;
        }
        try {
            docStore.moveToDisk(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enforceLimits() {
        while (this.docBytes > this.maxDocBytes || this.docCount > maxDocCount) {

            removeHeapDoc();
        }
    }
}
