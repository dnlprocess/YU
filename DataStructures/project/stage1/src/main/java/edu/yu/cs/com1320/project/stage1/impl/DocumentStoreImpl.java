package edu.yu.cs.com1320.project.stage1.impl;

import edu.yu.cs.com1320.project.stage1.DocumentStore;
import edu.yu.cs.com1320.project.stage1.Document;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.stage1.impl.DocumentImpl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class DocumentStoreImpl {

    enum DocumentFormat{
        TXT,BINARY
    }

    HashTableImpl<URI, Document> docStore;

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
    int put(InputStream input, URI uri, DocumentFormat format) throws IOException {
        if (uri == null || format == null) {
            throw new IllegalArgumentException();
        }

        this.docStore.get(uri);
        
        DocumentImpl doc = DocumentImpl(uri, input.readAllBytes());//format.equals("BINARY") ? DocumentImpl(uri, input.readAllBytes()): DocumentImpl(uri, input);


        if () {
            return 0;
        }
    }

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document
     */
    Document get(URI uri) {
        return this.docStore.get(uri);
    }

    /**
     * @param uri the unique identifier of the document to delete
     * @return true if the document is deleted, false if no document exists with that URI
     */
    boolean delete(URI uri) {
        if (this.docStore.containsKey(uri)) {
            this.docStore.put(uri, null);
            return true;
        }
        return false;
    }
    
}
