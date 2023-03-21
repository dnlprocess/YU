import edu.yu.cs.com1320.project.stage1.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage1.Document;
import edu.yu.cs.com1320.project.stage1.impl.DocumentStoreImpl;
import edu.yu.cs.com1320.project.stage1.DocumentStore;


package edu.yu.cs.com1320.project.stage1.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.junit.Test;

import edu.yu.cs.com1320.project.stage1.Document;
import edu.yu.cs.com1320.project.stage1.DocumentFormat;

public class DocumentStoreImplTest {
    
    @Test
    public void testPutNewVersionOfDocumentBinary() throws IOException {
        DocumentStoreImpl store = new DocumentStoreImpl();
        URI uri = URI.create("http://www.yu.edu/doc1");
        byte[] data1 = {1,2,3,4,5};
        byte[] data2 = {5,4,3,2,1};
        InputStream stream1 = new ByteArrayInputStream(data1);
        InputStream stream2 = new ByteArrayInputStream(data2);
        assertEquals(0, store.put(stream1, uri, DocumentFormat.BINARY));
        assertEquals(data1, store.get(uri).getDocumentBinaryData());
        assertEquals(data1.hashCode(), store.put(stream2, uri, DocumentFormat.BINARY));
        assertEquals(data2, store.get(uri).getDocumentBinaryData());
    }
    
    @Test
    public void testPutBinaryDocumentNoPreviousDocAtURI() throws IOException {
        DocumentStoreImpl store = new DocumentStoreImpl();
        URI uri = URI.create("http://www.yu.edu/doc1");
        byte[] data = {1,2,3,4,5};
        InputStream stream = new ByteArrayInputStream(data);
        assertEquals(0, store.put(stream, uri, DocumentFormat.BINARY));
        assertEquals(data, store.get(uri).getDocumentBinaryData());
    }
    
    @Test
    public void testGetBinaryDocAsBinary() throws IOException {
        DocumentStoreImpl store = new DocumentStoreImpl();
        URI uri = URI.create("http://www.yu.edu/doc1");
        byte[] data = {1,2,3,4,5};
        InputStream stream = new ByteArrayInputStream(data);
        store.put(stream, uri, DocumentFormat.BINARY);
        assertEquals(data, store.get(uri).getDocumentBinaryData());
    }
    
    @Test
    public void testPutNewVersionOfDocumentTxt() throws IOException {
        DocumentStoreImpl store = new DocumentStoreImpl();
        URI uri = URI.create("http://www.yu.edu/doc1");
        String text1 = "this is some text";
        String text2 = "this is some new text";
        InputStream stream1 = new ByteArrayInputStream(text1.getBytes());
        InputStream stream2 = new ByteArrayInputStream(text2.getBytes());
        assertEquals(0, store.put(stream1, uri, DocumentFormat.TXT));
        assertEquals(text1, store.get(uri).getDocumentAsTxt());
        assertEquals(text1.hashCode(), store.put(stream2, uri, DocumentFormat.TXT));
        assertEquals(text2, store.get(uri).getDocumentAsTxt());
    }
    
    @Test
    public void testDeleteDoc() throws IOException {
        DocumentStoreImpl store = new DocumentStoreImpl();
        URI uri = URI.create("http://www.yu.edu/doc1");
        byte[] data = {1,2,3,4,5};
        InputStream stream = new ByteArrayInputStream(data);
        store.put(stream, uri, DocumentFormat.BINARY);
        assertTrue(store.delete(uri));
        assertNull(store.get(uri));
    }