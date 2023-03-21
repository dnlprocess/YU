package edu.yu.cs.com1320.project.stage1.impl;

import edu.yu.cs.com1320.project.stage1.DocumentStore;
import edu.yu.cs.com1320.project.stage1.Document;
import edu.yu.cs.com1320.project.impl.HashTableImpl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class DocumentStoreImpl implements DocumentStore{

    HashTableImpl<URI, Document> docStore;

    public DocumentStoreImpl() {
        this.docStore = new HashTableImpl<URI, Document>();
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

        Document doc = (Document) new DocumentImpl(uri, input.readAllBytes());//format.equals("BINARY") ? DocumentImpl(uri, input.readAllBytes()): DocumentImpl(uri, input);

        if (!this.docStore.containsKey(uri)) {
            return 0;
        }
        
        return this.docStore.put(uri, doc).hashCode();
    
    }

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document
     */
    public Document get(URI uri) {
        return this.docStore.get(uri);
    }

    /**
     * @param uri the unique identifier of the document to delete
     * @return true if the document is deleted, false if no document exists with that URI
     */
    public boolean delete(URI uri) {
        if (this.docStore.containsKey(uri)) {
            this.docStore.put(uri, null);
            return true;
        }
        return false;
    }
    
}
