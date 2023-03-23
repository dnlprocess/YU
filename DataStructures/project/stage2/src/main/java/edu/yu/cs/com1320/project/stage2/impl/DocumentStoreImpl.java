package edu.yu.cs.com1320.project.stage2.impl;

import edu.yu.cs.com1320.project.stage2.DocumentStore;
import edu.yu.cs.com1320.project.stage2.Document;
import edu.yu.cs.com1320.project.stage2.impl.DocumentImpl;
import edu.yu.cs.com1320.project.Command;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;

import java.io.IOException;
import java.util.Scanner;
import java.io.InputStream;
import java.net.URI;
import java.util.function.Function;

public class DocumentStoreImpl implements DocumentStore{

    private class Entry<URI, Document> {
        URI uri;
        Document doc;
        Entry(URI uri, Document doc) {
            this.uri = uri;
            this.doc = doc;
        }
    }

    private HashTableImpl<URI, Document> docStore;
    private StackImpl<Entry<URI, Document>> storage;
    private StackImpl<Command> commandStack;

    public DocumentStoreImpl() {
        this.docStore = new HashTableImpl<URI, Document>();
        this.storage = new StackImpl<Entry<URI, Document>>();
        this.commandStack = new StackImpl<Command>();
    }

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document
     */
    public Document get(URI uri) {
        return this.docStore.get(uri);
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

        Document doc = input == null? null: format.equals(DocumentFormat.BINARY)? (Document) new DocumentImpl(uri, input.readAllBytes()): (Document) new DocumentImpl(uri, toTXT(input));

        Entry<URI, Document> entry = new Entry<URI, Document>(uri, this.docStore.get(uri));
        this.storage.push(entry);
        addCommand(uri);

        if (!this.docStore.containsKey(uri)) {
            this.docStore.put(uri, doc);
            return 0;
        }

        return input == null? this.docStore.put(uri, null).hashCode() : this.docStore.put(uri, doc).hashCode();
    
    }

    /**
     * @param uri the unique identifier of the document to delete
     * @return true if the document is deleted, false if no document exists with that URI
     */
    public boolean delete(URI uri) {
        if (!this.docStore.containsKey(uri)) {
            return false;
        }

        Entry<URI, Document> entry = new Entry<URI, Document>(uri, this.docStore.get(uri));
        this.storage.push(entry);
        this.docStore.put(uri, null);

        addCommand(uri);

        return true;
    }

    /**
     * undo the last put or delete command
     * @throws IllegalStateException if there are no actions to be undone, i.e. the command stack is empty
     */
    public void undo() throws IllegalStateException {
        if (this.commandStack.size() == 0) {
            throw new IllegalStateException();
        }
        
        this.commandStack.pop().undo();
    }

    /**
     * undo the last put or delete that was done with the given URI as its key
     * @param uri
     * @throws IllegalStateException if there are no actions on the command stack for the given URI
     */
    public void undo(URI uri) throws IllegalStateException {
        StackImpl<Command> tempStack = new StackImpl<Command>();
        Command command = null;
        for (int i=0; i<this.commandStack.size(); i++) {
            if (this.commandStack.peek().getUri() == uri){
                command = this.commandStack.pop();
                break;
            }
            tempStack.push(this.commandStack.pop());
        }

        if (command == null) {
            throw new IllegalStateException();
        }
        command.undo();

        for (int i=0; i<tempStack.size(); i++) {
            this.commandStack.push(tempStack.pop());
        }
    }

    private void addCommand(URI uri1) {
        Function<URI, Boolean> undo = uri -> {
            StackImpl<Entry<URI, Document>> tempStack = new StackImpl<Entry<URI, Document>>();
            Document doc = null;
            for (int i=0; i<storage.size(); i++) {
                if (storage.peek().uri.equals(uri)) {
                    doc = storage.pop().doc;
                    break;
                }
                tempStack.push(storage.pop());
            }
            for (int i=0; i<tempStack.size(); i++){
                this.storage.push(tempStack.pop());
            }
            return this.docStore.put(uri, doc) == null? false: true;
        };

        Command newCommand = new Command(uri1, undo);
        this.commandStack.push(newCommand);
    }

    private String toTXT(InputStream input) throws IOException {
        String s = "";
        try (Scanner scanner = new Scanner(input).useDelimiter("\\A")) {
            if (scanner.hasNext()) {
                s = scanner.next();
            }
            scanner.close();
        }
        
        return s;
    }
}
