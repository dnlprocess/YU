//recent

package edu.yu.cs.com1320.project.stage2.impl;

import edu.yu.cs.com1320.project.stage2.DocumentStore;
import edu.yu.cs.com1320.project.stage2.Document;
import edu.yu.cs.com1320.project.Command;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.function.Function;

public class DocumentStoreImpl implements DocumentStore{


    HashTableImpl<URI, Document> docStore;
    StackImpl<Command> commandStack;

    public DocumentStoreImpl() {
        this.docStore = new HashTableImpl<URI, Document>();
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

        //add command

        Document doc = (Document) new DocumentImpl(uri, input.readAllBytes());//format.equals("BINARY") ? DocumentImpl(uri, input.readAllBytes()): DocumentImpl(uri, input);

        if (!this.docStore.containsKey(uri)) {
            return 0;
        }
        
        return this.docStore.put(uri, doc).hashCode();
    
    }

    /**
     * @param uri the unique identifier of the document to delete
     * @return true if the document is deleted, false if no document exists with that URI
     */
    public boolean delete(URI uri) {
        if (!this.docStore.containsKey(uri)) {
            return false;
        }
        this.docStore.put(uri, null);

        //add command

        return true;
    }

    /**
     * undo the last put or delete command
     * @throws IllegalStateException if there are no actions to be undone, i.e. the command stack is empty
     */
    public void undo() throws IllegalStateException {
        this.commandStack.peek().undo();
        this.commandStack.pop();
    }

    /**
     * undo the last put or delete that was done with the given URI as its key
     * @param uri
     * @throws IllegalStateException if there are no actions on the command stack for the given URI
     */
    void undo(URI uri) throws IllegalStateException {
        StackImpl<Command> tempStack = new StackImpl<Command>();
        for (int i=0; i<this.commandStack.size(); i++) {
            if (this.commandStack.peek().getUri() == uri){
                this.commandStack.peek().undo();
                this.commandStack.pop();
            }
            else {
                tempStack.push(this.commandStack.pop());
            }
        }
    }

    void addCommand() {
        Function<URI, Boolean> undo = () -> {};



        Command newCommand = new Command(uri, );
        this.commandStack.push(newCommand);
    }
}
