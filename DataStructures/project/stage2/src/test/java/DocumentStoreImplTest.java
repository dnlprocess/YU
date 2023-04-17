import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.junit.Test;

import edu.yu.cs.com1320.project.stage2.DocumentStore;
import edu.yu.cs.com1320.project.stage2.Document;
import edu.yu.cs.com1320.project.stage2.DocumentStore.DocumentFormat;

import edu.yu.cs.com1320.project.stage2.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage2.impl.DocumentStoreImpl;

public class DocumentStoreImplTest {
    
    @Test
    public void testPutDocBytes() throws IOException {
        DocumentStoreImpl docStore = new DocumentStoreImpl();
        URI uri = URI.create("http://www.example.com");

        byte[] bytes1 = "example1".getBytes();
        InputStream stream1 = new ByteArrayInputStream(bytes1);
        Document doc1 = new DocumentImpl(uri, bytes1);
        assertEquals(0, docStore.put(stream1, uri, DocumentFormat.BINARY));
        assertEquals(doc1, docStore.get(uri));

        byte[] bytes2 = "example2".getBytes();
        InputStream stream2 = new ByteArrayInputStream(bytes2);
        Document doc2 = new DocumentImpl(uri, bytes2);
        assertEquals(doc1.hashCode(), docStore.put(stream2, uri, DocumentFormat.BINARY));
        assertEquals(doc2, docStore.get(uri));

        docStore.undo();
        assertEquals(doc1, docStore.get(uri));
        docStore.undo();
        assertEquals(null, docStore.get(uri));

        byte[] bytes3 = "example3".getBytes();
        InputStream stream3 = new ByteArrayInputStream(bytes3);
        int yu = docStore.put(stream3, uri, DocumentFormat.BINARY);
        assertEquals(0, yu);
        assertEquals(doc1.hashCode(), docStore.put(stream2, uri, DocumentFormat.BINARY));
        
        Document doc3 = new DocumentImpl(uri, bytes3);
        docStore.put(null, uri, DocumentFormat.BINARY);
        docStore.put(stream3, uri, DocumentFormat.BINARY);
        assertEquals(doc3, docStore.get(uri));
        docStore.undo();
        assertEquals(null, docStore.get(uri));
        docStore.undo();
        assertEquals(doc2, docStore.get(uri));
        docStore.undo();
        assertEquals(doc1, docStore.get(uri));



    }
    
    /*
    @Test
    public void testPutBinaryDocumentNoPreviousDocAtURI() throws IOException {
        DocumentStoreImpl store = new DocumentStoreImpl();
        URI uri = URI.create("http://www.example.com");
        byte[] data = {1,2,3,4,5};
        InputStream stream = new ByteArrayInputStream(data);
        assertEquals(0, store.put(stream, uri, DocumentFormat.BINARY));
        assertEquals(data, store.get(uri).getDocumentBinaryData());
    }
    
    @Test
    public void testGetBinaryDocAsBinary() throws IOException {
        DocumentStoreImpl store = new DocumentStoreImpl();
        URI uri = URI.create("http://www.example.com");
        byte[] data = {1,2,3,4,5};
        InputStream stream = new ByteArrayInputStream(data);
        store.put(stream, uri, DocumentFormat.BINARY);
        assertEquals(data, store.get(uri).getDocumentBinaryData());
    }
    
    @Test
    public void testPutNewVersionOfDocumentTxt() throws IOException {
        DocumentStoreImpl store = new DocumentStoreImpl();
        URI uri = URI.create("http://www.example.com");
        String text1 = "text";
        String text2 = "new text";
        InputStream stream1 = new ByteArrayInputStream(text1.getBytes());
        InputStream stream2 = new ByteArrayInputStream(text2.getBytes());
        Document doc1 = new DocumentImpl(uri, text1);
        Document doc2 = new DocumentImpl(uri, text1);
        assertEquals(0, store.put(stream1, uri, DocumentFormat.TXT));
        assertEquals(text1, store.get(uri));
        assertEquals(text1.hashCode(), store.put(stream2, uri, DocumentFormat.TXT));
        assertEquals(text2, store.get(uri));
    }
    
    @Test
    public void testDeleteDoc() throws IOException {
        DocumentStoreImpl store = new DocumentStoreImpl();
        URI uri = URI.create("http://www.example.com");
        byte[] data = {1,2,3,4,5};
        InputStream stream = new ByteArrayInputStream(data);
        store.put(stream, uri, DocumentFormat.BINARY);
        assertTrue(store.delete(uri));
        assertNull(store.get(uri));
    }
    */
}