import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.junit.Test;

import edu.yu.cs.com1320.project.stage3.DocumentStore;
import edu.yu.cs.com1320.project.stage3.Document;
import edu.yu.cs.com1320.project.stage3.DocumentStore.DocumentFormat;

import edu.yu.cs.com1320.project.stage3.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage3.impl.DocumentStoreImpl;

public class HashTableImplTest2 {
    public static void main(String[] args) throws IOException {
        DocumentStoreImplOld docStore = new DocumentStoreImplOld();
        URI uri = URI.create("http://www.example.com");

        byte[] bytes1 = "example1".getBytes();
        InputStream stream1 = new ByteArrayInputStream(bytes1);
        Document doc1 = new DocumentImpl(uri, bytes1);
        //docStore.put(stream1, uri, DocumentFormat.BINARY);

        byte[] bytes2 = "example2".getBytes();
        InputStream stream2 = new ByteArrayInputStream(bytes2);
        Document doc2 = new DocumentImpl(uri, bytes2);
        //docStore.put(stream2, uri, DocumentFormat.BINARY);


        //docStore.undo();
        //docStore.undo();


        docStore.put(stream1, uri, DocumentFormat.BINARY);
        docStore.put(stream2, uri, DocumentFormat.BINARY);
        docStore.put(stream1, uri, DocumentFormat.BINARY);
        
        byte[] bytes3 = "example3".getBytes();
        InputStream stream3 = new ByteArrayInputStream(bytes3);
        Document doc3 = new DocumentImpl(uri, bytes3);
        docStore.put(null, uri, DocumentFormat.BINARY);
        docStore.put(stream3, uri, DocumentFormat.BINARY);
        docStore.undo();
        docStore.undo();
        docStore.undo();
        if (!doc1.equals(docStore.get(uri))) {
            System.out.println("oh no!");
        }


    }
}
