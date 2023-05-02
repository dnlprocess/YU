import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.stage4.Document;
import edu.yu.cs.com1320.project.stage4.DocumentStore;
import edu.yu.cs.com1320.project.stage4.DocumentStore.DocumentFormat;
import edu.yu.cs.com1320.project.stage4.impl.DocumentStoreImpl;
import edu.yu.cs.com1320.project.stage4.impl.DocumentImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;


import java.time.LocalDateTime;
import java.util.ArrayList;


public class DocumentStoreImplTest {

    private DocumentStoreImpl store;
    private URI uri1, uri2, uri3, uri4, uri5;
    private Document doc1, doc2, doc3, doc4, doc5;
    private InputStream inputStream1, inputStream2, inputStream3, inputStream4, inputStream5;

    /*@Before
    public void setUp() throws URISyntaxException, IOException {
        store = new DocumentStoreImpl();
        uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        String txt1 = "ta";
        byte[] binaryData1 = {0x00, 0x01, 0x02, 0x03, 0x04};
        inputStream1 = new ByteArrayInputStream(txt1.getBytes());
        doc1 = new DocumentImpl(uri1, txt1);
        store.put(inputStream1, uri1, DocumentFormat.TXT);
        
        
        uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        String txt2 = "tb tc";
        byte[] binaryData2 = {0x05, 0x06, 0x07, 0x08, 0x09};
        inputStream2 = new ByteArrayInputStream(txt2.getBytes());
        doc2 = new DocumentImpl(uri2, txt2);
        store.put(inputStream2, uri2, DocumentFormat.TXT);

        uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        String txt3 = "td te tf";
        byte[] binaryData3 = {0x0A, 0x0B, 0x0C, 0x0D, 0x0E};
        inputStream3 = new ByteArrayInputStream(txt3.getBytes());
        doc3 = new DocumentImpl(uri3, txt3);
        store.put(inputStream3, uri3, DocumentFormat.TXT);
        
        uri5 = new URI("http://edu.yu.cs/com1320/project/doc5");
        String txt5 = "ti ti ti ti";
        byte[] binaryData5 = {0x14, 0x15, 0x16, 0x17, 0x18};
        inputStream5 = new ByteArrayInputStream(txt5.getBytes());
        doc5 = new DocumentImpl(uri5, txt5);
        store.put(inputStream5, uri5, DocumentFormat.TXT);



        uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        String txt4 = "This is the fourth test document.";
        byte[] binaryData4 = {0x0F, 0x10, 0x11, 0x12, 0x13};
        inputStream4 = new ByteArrayInputStream(binaryData4);
        doc4 = new DocumentImpl(uri4, inputStream4.readAllBytes());
        inputStream4 = new ByteArrayInputStream(binaryData3);
        store.put(inputStream4, uri4, DocumentFormat.BINARY);
    }*/

    @Test
    public void testPutAndGetTxtDocument() throws Exception {
        store = new DocumentStoreImpl();
        uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        String txt1 = "ta";
        byte[] binaryData1 = {0x00, 0x01, 0x02, 0x03, 0x04};
        inputStream1 = new ByteArrayInputStream(txt1.getBytes());
        doc1 = new DocumentImpl(uri1, txt1);
        store.put(inputStream1, uri1, DocumentFormat.TXT);
        
        
        uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        String txt2 = "tb tc";
        byte[] binaryData2 = {0x05, 0x06, 0x07, 0x08, 0x09};
        inputStream2 = new ByteArrayInputStream(txt2.getBytes());
        doc2 = new DocumentImpl(uri2, txt2);
        store.put(inputStream2, uri2, DocumentFormat.TXT);

        uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        String txt3 = "td te tf";
        byte[] binaryData3 = {0x0A, 0x0B, 0x0C, 0x0D, 0x0E};
        inputStream3 = new ByteArrayInputStream(txt3.getBytes());
        doc3 = new DocumentImpl(uri3, txt3);
        store.put(inputStream3, uri3, DocumentFormat.TXT);
        
        uri5 = new URI("http://edu.yu.cs/com1320/project/doc5");
        String txt5 = "ti ti ti ti";
        byte[] binaryData5 = {0x14, 0x15, 0x16, 0x17, 0x18};
        inputStream5 = new ByteArrayInputStream(txt5.getBytes());
        doc5 = new DocumentImpl(uri5, txt5);
        store.put(inputStream5, uri5, DocumentFormat.TXT);



        uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        String txt4 = "This is the fourth test document.";
        byte[] binaryData4 = {0x0F, 0x10, 0x11, 0x12, 0x13};
        inputStream4 = new ByteArrayInputStream(binaryData4);
        doc4 = new DocumentImpl(uri4, inputStream4.readAllBytes());
        inputStream4 = new ByteArrayInputStream(binaryData3);
        store.put(inputStream4, uri4, DocumentFormat.BINARY);
        // Test putting and getting a TXT document
        String txt = "This is a test document.";
        URI uri = new URI("http://example.com/test");
        InputStream stream = new ByteArrayInputStream(txt.getBytes());
        DocumentFormat format = DocumentFormat.TXT;
        store.put(stream, uri, format);
        Document result = store.get(uri);
        assertNotNull(result);
        assertEquals(txt, result.getDocumentTxt());
    }

    @Test
    public void testPutAndGetBinaryDocument() throws Exception {
        store = new DocumentStoreImpl();
        uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        String txt1 = "ta";
        byte[] binaryData1 = {0x00, 0x01, 0x02, 0x03, 0x04};
        inputStream1 = new ByteArrayInputStream(txt1.getBytes());
        doc1 = new DocumentImpl(uri1, txt1);
        store.put(inputStream1, uri1, DocumentFormat.TXT);
        
        
        uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        String txt2 = "tb tc";
        byte[] binaryData2 = {0x05, 0x06, 0x07, 0x08, 0x09};
        inputStream2 = new ByteArrayInputStream(txt2.getBytes());
        doc2 = new DocumentImpl(uri2, txt2);
        store.put(inputStream2, uri2, DocumentFormat.TXT);

        uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        String txt3 = "td te tf";
        byte[] binaryData3 = {0x0A, 0x0B, 0x0C, 0x0D, 0x0E};
        inputStream3 = new ByteArrayInputStream(txt3.getBytes());
        doc3 = new DocumentImpl(uri3, txt3);
        store.put(inputStream3, uri3, DocumentFormat.TXT);
        
        uri5 = new URI("http://edu.yu.cs/com1320/project/doc5");
        String txt5 = "ti ti ti ti";
        byte[] binaryData5 = {0x14, 0x15, 0x16, 0x17, 0x18};
        inputStream5 = new ByteArrayInputStream(txt5.getBytes());
        doc5 = new DocumentImpl(uri5, txt5);
        store.put(inputStream5, uri5, DocumentFormat.TXT);



        uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        String txt4 = "This is the fourth test document.";
        byte[] binaryData4 = {0x0F, 0x10, 0x11, 0x12, 0x13};
        inputStream4 = new ByteArrayInputStream(binaryData4);
        doc4 = new DocumentImpl(uri4, inputStream4.readAllBytes());
        inputStream4 = new ByteArrayInputStream(binaryData3);
        store.put(inputStream4, uri4, DocumentFormat.BINARY);
        // Test putting and getting a binary document
        byte[] data = new byte[] {0x01, 0x02, 0x03};
        URI uri = new URI("http://example.com/test");
        InputStream stream = new ByteArrayInputStream(data);
        DocumentFormat format = DocumentFormat.BINARY;
        store.put(stream, uri, format);
        Document result = store.get(uri);
        assertNotNull(result);
        assertArrayEquals(data, result.getDocumentBinaryData());
    }

    @Test
    public void testDelete() throws Exception {
        store = new DocumentStoreImpl();
        uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        String txt1 = "ta";
        byte[] binaryData1 = {0x00, 0x01, 0x02, 0x03, 0x04};
        inputStream1 = new ByteArrayInputStream(txt1.getBytes());
        doc1 = new DocumentImpl(uri1, txt1);
        store.put(inputStream1, uri1, DocumentFormat.TXT);
        
        
        uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        String txt2 = "tb tc";
        byte[] binaryData2 = {0x05, 0x06, 0x07, 0x08, 0x09};
        inputStream2 = new ByteArrayInputStream(txt2.getBytes());
        doc2 = new DocumentImpl(uri2, txt2);
        store.put(inputStream2, uri2, DocumentFormat.TXT);

        uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        String txt3 = "td te tf";
        byte[] binaryData3 = {0x0A, 0x0B, 0x0C, 0x0D, 0x0E};
        inputStream3 = new ByteArrayInputStream(txt3.getBytes());
        doc3 = new DocumentImpl(uri3, txt3);
        store.put(inputStream3, uri3, DocumentFormat.TXT);
        
        uri5 = new URI("http://edu.yu.cs/com1320/project/doc5");
        String txt5 = "ti ti ti ti";
        byte[] binaryData5 = {0x14, 0x15, 0x16, 0x17, 0x18};
        inputStream5 = new ByteArrayInputStream(txt5.getBytes());
        doc5 = new DocumentImpl(uri5, txt5);
        store.put(inputStream5, uri5, DocumentFormat.TXT);



        uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        String txt4 = "This is the fourth test document.";
        byte[] binaryData4 = {0x0F, 0x10, 0x11, 0x12, 0x13};
        inputStream4 = new ByteArrayInputStream(binaryData4);
        doc4 = new DocumentImpl(uri4, inputStream4.readAllBytes());
        inputStream4 = new ByteArrayInputStream(binaryData3);
        store.put(inputStream4, uri4, DocumentFormat.BINARY);
        // Test deleting a document
        URI uri = new URI("http://example.com/test");
        InputStream stream = new ByteArrayInputStream("This is a test document.".getBytes());
        DocumentFormat format = DocumentFormat.TXT;
        store.put(stream, uri, format);
        store.delete(uri);
        Document result = store.get(uri);
        assertNull(result);
    }

    @Test
    public void testSearch1() throws Exception {
        store = new DocumentStoreImpl();
        uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        String txt1 = "ta";
        byte[] binaryData1 = {0x00, 0x01, 0x02, 0x03, 0x04};
        inputStream1 = new ByteArrayInputStream(txt1.getBytes());
        doc1 = new DocumentImpl(uri1, txt1);
        store.put(inputStream1, uri1, DocumentFormat.TXT);
        
        
        uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        String txt2 = "tb tc This";
        byte[] binaryData2 = {0x05, 0x06, 0x07, 0x08, 0x09};
        inputStream2 = new ByteArrayInputStream(txt2.getBytes());
        doc2 = new DocumentImpl(uri2, txt2);
        store.put(inputStream2, uri2, DocumentFormat.TXT);

        uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        String txt3 = "td te tf";
        byte[] binaryData3 = {0x0A, 0x0B, 0x0C, 0x0D, 0x0E};
        inputStream3 = new ByteArrayInputStream(txt3.getBytes());
        doc3 = new DocumentImpl(uri3, txt3);
        store.put(inputStream3, uri3, DocumentFormat.TXT);
        
        uri5 = new URI("http://edu.yu.cs/com1320/project/doc5");
        String txt5 = "ti ti ti ti";
        byte[] binaryData5 = {0x14, 0x15, 0x16, 0x17, 0x18};
        inputStream5 = new ByteArrayInputStream(txt5.getBytes());
        doc5 = new DocumentImpl(uri5, txt5);
        store.put(inputStream5, uri5, DocumentFormat.TXT);



        uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        String txt4 = "This is the fourth test document.";
        InputStream inputStream = new ByteArrayInputStream(txt4.getBytes());
        doc4 = new DocumentImpl(uri4, txt4);
        store.put(inputStream, uri4, DocumentFormat.TXT);
        // Test searching for documents
        List<Document> result = store.search("This");
        assertEquals(2, result.size());
        assertTrue(result.contains(doc4));
        assertTrue(result.contains(doc2));
    }

    @Test
    public void testNullInputStream() throws Exception {
        store = new DocumentStoreImpl();
        uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        String txt1 = "ta";
        byte[] binaryData1 = {0x00, 0x01, 0x02, 0x03, 0x04};
        inputStream1 = new ByteArrayInputStream(txt1.getBytes());
        doc1 = new DocumentImpl(uri1, txt1);
        store.put(inputStream1, uri1, DocumentFormat.TXT);
        
        
        uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        String txt2 = "tb tc";
        byte[] binaryData2 = {0x05, 0x06, 0x07, 0x08, 0x09};
        inputStream2 = new ByteArrayInputStream(txt2.getBytes());
        doc2 = new DocumentImpl(uri2, txt2);
        store.put(inputStream2, uri2, DocumentFormat.TXT);

        uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        String txt3 = "td te tf";
        byte[] binaryData3 = {0x0A, 0x0B, 0x0C, 0x0D, 0x0E};
        inputStream3 = new ByteArrayInputStream(txt3.getBytes());
        doc3 = new DocumentImpl(uri3, txt3);
        store.put(inputStream3, uri3, DocumentFormat.TXT);
        
        uri5 = new URI("http://edu.yu.cs/com1320/project/doc5");
        String txt5 = "ti ti ti ti";
        byte[] binaryData5 = {0x14, 0x15, 0x16, 0x17, 0x18};
        inputStream5 = new ByteArrayInputStream(txt5.getBytes());
        doc5 = new DocumentImpl(uri5, txt5);
        store.put(inputStream5, uri5, DocumentFormat.TXT);



        uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        String txt4 = "This is the fourth test document.";
        byte[] binaryData4 = {0x0F, 0x10, 0x11, 0x12, 0x13};
        inputStream4 = new ByteArrayInputStream(binaryData4);
        doc4 = new DocumentImpl(uri4, inputStream4.readAllBytes());
        inputStream4 = new ByteArrayInputStream(binaryData3);
        store.put(inputStream4, uri4, DocumentFormat.BINARY);
        // Test passing a null InputStream to put() meaning delete
        assertEquals(0, store.put(null, new URI("http://example.com/test"), DocumentFormat.TXT));
        
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullURI() throws Exception {
        store = new DocumentStoreImpl();
        uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        String txt1 = "ta";
        byte[] binaryData1 = {0x00, 0x01, 0x02, 0x03, 0x04};
        inputStream1 = new ByteArrayInputStream(txt1.getBytes());
        doc1 = new DocumentImpl(uri1, txt1);
        store.put(inputStream1, uri1, DocumentFormat.TXT);
        
        
        uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        String txt2 = "tb tc";
        byte[] binaryData2 = {0x05, 0x06, 0x07, 0x08, 0x09};
        inputStream2 = new ByteArrayInputStream(txt2.getBytes());
        doc2 = new DocumentImpl(uri2, txt2);
        store.put(inputStream2, uri2, DocumentFormat.TXT);

        uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        String txt3 = "td te tf";
        byte[] binaryData3 = {0x0A, 0x0B, 0x0C, 0x0D, 0x0E};
        inputStream3 = new ByteArrayInputStream(txt3.getBytes());
        doc3 = new DocumentImpl(uri3, txt3);
        store.put(inputStream3, uri3, DocumentFormat.TXT);
        
        uri5 = new URI("http://edu.yu.cs/com1320/project/doc5");
        String txt5 = "ti ti ti ti";
        byte[] binaryData5 = {0x14, 0x15, 0x16, 0x17, 0x18};
        inputStream5 = new ByteArrayInputStream(txt5.getBytes());
        doc5 = new DocumentImpl(uri5, txt5);
        store.put(inputStream5, uri5, DocumentFormat.TXT);



        uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        String txt4 = "This is the fourth test document.";
        byte[] binaryData4 = {0x0F, 0x10, 0x11, 0x12, 0x13};
        inputStream4 = new ByteArrayInputStream(binaryData4);
        doc4 = new DocumentImpl(uri4, inputStream4.readAllBytes());
        inputStream4 = new ByteArrayInputStream(binaryData3);
        store.put(inputStream4, uri4, DocumentFormat.BINARY);
        // Test passing a null URI to put()
        InputStream stream = new ByteArrayInputStream("This is a test document.".getBytes());
        store.put(stream, null, DocumentFormat.TXT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormat() throws Exception {
        store = new DocumentStoreImpl();
        uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        String txt1 = "ta";
        byte[] binaryData1 = {0x00, 0x01, 0x02, 0x03, 0x04};
        inputStream1 = new ByteArrayInputStream(txt1.getBytes());
        doc1 = new DocumentImpl(uri1, txt1);
        store.put(inputStream1, uri1, DocumentFormat.TXT);
        
        
        uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        String txt2 = "tb tc";
        byte[] binaryData2 = {0x05, 0x06, 0x07, 0x08, 0x09};
        inputStream2 = new ByteArrayInputStream(txt2.getBytes());
        doc2 = new DocumentImpl(uri2, txt2);
        store.put(inputStream2, uri2, DocumentFormat.TXT);

        uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        String txt3 = "td te tf";
        byte[] binaryData3 = {0x0A, 0x0B, 0x0C, 0x0D, 0x0E};
        inputStream3 = new ByteArrayInputStream(txt3.getBytes());
        doc3 = new DocumentImpl(uri3, txt3);
        store.put(inputStream3, uri3, DocumentFormat.TXT);
        
        uri5 = new URI("http://edu.yu.cs/com1320/project/doc5");
        String txt5 = "ti ti ti ti";
        byte[] binaryData5 = {0x14, 0x15, 0x16, 0x17, 0x18};
        inputStream5 = new ByteArrayInputStream(txt5.getBytes());
        doc5 = new DocumentImpl(uri5, txt5);
        store.put(inputStream5, uri5, DocumentFormat.TXT);



        uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        String txt4 = "This is the fourth test document.";
        byte[] binaryData4 = {0x0F, 0x10, 0x11, 0x12, 0x13};
        inputStream4 = new ByteArrayInputStream(binaryData4);
        doc4 = new DocumentImpl(uri4, inputStream4.readAllBytes());
        inputStream4 = new ByteArrayInputStream(binaryData3);
        store.put(inputStream4, uri4, DocumentFormat.BINARY);
        // Test passing a null format to put()
        InputStream stream = new ByteArrayInputStream("This is a test document.".getBytes());
        store.put(stream, new URI("http://example.com/test"), null);
    }

    @Test
    public void testTrie() throws Exception {
        store = new DocumentStoreImpl();
        uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        String txt1 = "ta";
        byte[] binaryData1 = {0x00, 0x01, 0x02, 0x03, 0x04};
        inputStream1 = new ByteArrayInputStream(txt1.getBytes());
        doc1 = new DocumentImpl(uri1, txt1);
        store.put(inputStream1, uri1, DocumentFormat.TXT);
        
        
        uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        String txt2 = "tb tc";
        byte[] binaryData2 = {0x05, 0x06, 0x07, 0x08, 0x09};
        inputStream2 = new ByteArrayInputStream(txt2.getBytes());
        doc2 = new DocumentImpl(uri2, txt2);
        store.put(inputStream2, uri2, DocumentFormat.TXT);

        uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        String txt3 = "td te tf";
        byte[] binaryData3 = {0x0A, 0x0B, 0x0C, 0x0D, 0x0E};
        inputStream3 = new ByteArrayInputStream(txt3.getBytes());
        doc3 = new DocumentImpl(uri3, txt3);
        store.put(inputStream3, uri3, DocumentFormat.TXT);
        
        uri5 = new URI("http://edu.yu.cs/com1320/project/doc5");
        String txt5 = "ti ti ti ti";
        byte[] binaryData5 = {0x14, 0x15, 0x16, 0x17, 0x18};
        inputStream5 = new ByteArrayInputStream(txt5.getBytes());
        doc5 = new DocumentImpl(uri5, txt5);
        store.put(inputStream5, uri5, DocumentFormat.TXT);



        uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        String txt4 = "This is the fourth test document.";
        byte[] binaryData4 = {0x0F, 0x10, 0x11, 0x12, 0x13};
        inputStream4 = new ByteArrayInputStream(binaryData4);
        doc4 = new DocumentImpl(uri4, inputStream4.readAllBytes());
        inputStream4 = new ByteArrayInputStream(binaryData3);
        store.put(inputStream4, uri4, DocumentFormat.BINARY);

        List<Document> docs = store.searchByPrefix("t");
        assertEquals(7, docs.size());
        assertEquals(doc1, store.get(uri1));
        assertEquals(doc1.getWords(), store.get(uri1).getWords());
        
        store.deleteAllWithPrefix("t");
            docs = store.searchByPrefix("t");
            assertEquals(0, docs.size());
        store.undo();
            docs = store.searchByPrefix("t");
            assertEquals(7, docs.size());

        store.delete(uri5);
        docs = store.search("last");
        assertEquals(0, docs.size());
    }

    @Test
    public void testSearchOrder() throws Exception {
        store = new DocumentStoreImpl();
        uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        String txt1 = "ta";
        byte[] binaryData1 = {0x00, 0x01, 0x02, 0x03, 0x04};
        inputStream1 = new ByteArrayInputStream(txt1.getBytes());
        doc1 = new DocumentImpl(uri1, txt1);
        store.put(inputStream1, uri1, DocumentFormat.TXT);
        
        
        uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        String txt2 = "tb tc";
        byte[] binaryData2 = {0x05, 0x06, 0x07, 0x08, 0x09};
        inputStream2 = new ByteArrayInputStream(txt2.getBytes());
        doc2 = new DocumentImpl(uri2, txt2);
        store.put(inputStream2, uri2, DocumentFormat.TXT);

        uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        String txt3 = "td te tf";
        byte[] binaryData3 = {0x0A, 0x0B, 0x0C, 0x0D, 0x0E};
        inputStream3 = new ByteArrayInputStream(txt3.getBytes());
        doc3 = new DocumentImpl(uri3, txt3);
        store.put(inputStream3, uri3, DocumentFormat.TXT);
        
        uri5 = new URI("http://edu.yu.cs/com1320/project/doc5");
        String txt5 = "ti td tg tm to";
        byte[] binaryData5 = {0x14, 0x15, 0x16, 0x17, 0x18};
        inputStream5 = new ByteArrayInputStream(txt5.getBytes());
        doc5 = new DocumentImpl(uri5, txt5);
        store.put(inputStream5, uri5, DocumentFormat.TXT);



        uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        String txt4 = "This is the fourth test document.";
        byte[] binaryData4 = {0x0F, 0x10, 0x11, 0x12, 0x13};
        inputStream4 = new ByteArrayInputStream(binaryData4);
        doc4 = new DocumentImpl(uri4, inputStream4.readAllBytes());
        inputStream4 = new ByteArrayInputStream(binaryData3);
        store.put(inputStream4, uri4, DocumentFormat.BINARY);

        List<Document> docs = store.searchByPrefix("t");
        assertEquals(txt5, docs.get(4).getDocumentTxt());
        //assertEquals(true, docs.get(0).getWords().contains("ti"));
        assertEquals(true, docs.get(docs.size()-1).getWords().contains("ta"));
    }

    @Test
    public void testUndo1() throws Exception {
        store = new DocumentStoreImpl();
        uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        String txt1 = "ta";
        byte[] binaryData1 = {0x00, 0x01, 0x02, 0x03, 0x04};
        inputStream1 = new ByteArrayInputStream(txt1.getBytes());
        doc1 = new DocumentImpl(uri1, txt1);
        store.put(inputStream1, uri1, DocumentFormat.TXT);
        
        
        uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        String txt2 = "tb tc";
        byte[] binaryData2 = {0x05, 0x06, 0x07, 0x08, 0x09};
        inputStream2 = new ByteArrayInputStream(txt2.getBytes());
        doc2 = new DocumentImpl(uri2, txt2);
        store.put(inputStream2, uri2, DocumentFormat.TXT);

        uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        String txt3 = "td te tf";
        byte[] binaryData3 = {0x0A, 0x0B, 0x0C, 0x0D, 0x0E};
        inputStream3 = new ByteArrayInputStream(txt3.getBytes());
        doc3 = new DocumentImpl(uri3, txt3);
        store.put(inputStream3, uri3, DocumentFormat.TXT);
        
        uri5 = new URI("http://edu.yu.cs/com1320/project/doc5");
        String txt5 = "ti ti ti ti";
        byte[] binaryData5 = {0x14, 0x15, 0x16, 0x17, 0x18};
        inputStream5 = new ByteArrayInputStream(txt5.getBytes());
        doc5 = new DocumentImpl(uri5, txt5);
        store.put(inputStream5, uri5, DocumentFormat.TXT);



        uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        String txt4 = "This is the fourth test document.";
        byte[] binaryData4 = {0x0F, 0x10, 0x11, 0x12, 0x13};
        inputStream4 = new ByteArrayInputStream(binaryData4);
        doc4 = new DocumentImpl(uri4, inputStream4.readAllBytes());
        inputStream4 = new ByteArrayInputStream(binaryData3);
        store.put(inputStream4, uri4, DocumentFormat.BINARY);

        store.deleteAllWithPrefix("t");
        assertEquals(null, store.get(uri1));

        store.undo(uri3);
        List<Document> docs = store.searchByPrefix("td");
        assertEquals(true, docs.get(0).getWords().contains("td"));
        assertEquals(false, docs.get(docs.size()-1).getWords().contains("ti"));
    
    }

    @Test
    public void testUndoMostRecentWhenMostRecentDeletedMultipleDocuments() throws Exception {
        DocumentStoreImpl documentStore = new DocumentStoreImpl();
        /*URI uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        URI uri2 = new URI("http://edu.yu.cs/com1320/2");
        URI uri3 = new URI("http://edu.yu.cs/com1320/project/3");
        URI uri4 = new URI("http://edu.yu.cs/com1320/project/4");
        URI uri5 = new URI("http://edu.yu.5");
        URI uri6 = new URI("http://edu.yu.cs/6");*/

        URI uri1 = new URI("ZZZZZ");
        URI uri2 = new URI("PPPPP");
        URI uri3 = new URI("MMMMM");
        URI uri4 = new URI("CCCCC");
        URI uri5 = new URI("BBBBB");
        URI uri6 = new URI("AAAAA");

        String content1 = "6";
        String content2 = "5";
        String content3 = "4";
        String content4 = "3";
        String content5 = "2";
        String content6 = "1";

        // add all documents
        assertEquals(0, documentStore.put(new ByteArrayInputStream(content6.getBytes()), uri6, DocumentStore.DocumentFormat.TXT));
        assertEquals(0, documentStore.put(new ByteArrayInputStream(content2.getBytes()), uri2, DocumentStore.DocumentFormat.TXT));
        assertEquals(0, documentStore.put(new ByteArrayInputStream(content3.getBytes()), uri3, DocumentStore.DocumentFormat.TXT));
        assertEquals(0, documentStore.put(new ByteArrayInputStream(content4.getBytes()), uri4, DocumentStore.DocumentFormat.TXT));
        assertEquals(uri4, documentStore.get(uri4).getKey());
        assertEquals(0, documentStore.put(new ByteArrayInputStream(content5.getBytes()), uri5, DocumentStore.DocumentFormat.TXT));
        assertEquals(0, documentStore.put(new ByteArrayInputStream(content1.getBytes()), uri1, DocumentStore.DocumentFormat.TXT));
        assertEquals(uri2, documentStore.get(uri2).getKey());


        // delete multiple documents
        assertEquals(uri6, documentStore.get(uri6).getKey());
        assertEquals(uri4, documentStore.get(uri4).getKey());
        assertEquals(true, documentStore.delete(uri6));
        //assertEquals(uri4, documentStore.get(uri4).getKey());
        //assertEquals(true, documentStore.delete(uri4));
        documentStore.delete(uri5);
        documentStore.delete(uri3);
        documentStore.delete(uri1);
        //assertEquals(uri2, documentStore.get(uri2).getKey());
        //assertEquals(true, documentStore.delete(uri2));
        //uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        
       //assertEquals(uri2, documentStore.get(uri2).getKey());
       // assertEquals(true, documentStore.delete(uri2));

        // verify that the most recent document was deleted
        assertNull(documentStore.get(uri4));
        assertNull(documentStore.get(uri6));
        
        // undo most recent deletion
        documentStore.undo();
        documentStore.undo();
        documentStore.undo();
        documentStore.undo();
        documentStore.undo();
        documentStore.undo();
        //assertEquals(, ((GenericCommand) documentStore.undoableStack.peek()).getTarget().toString());

        // verify that the most recent deletion was undone
        assertNotNull(documentStore.get(uri6));
        //assertNotNull(documentStore.get(uri1));
        //assertNotNull(documentStore.get(uri2));
        //assertNotNull(documentStore.get(uri3));

        // undo again to verify that the second most recent deletion was undone
        //documentStore.undo();
        //assertNotNull(documentStore.get(uri4));
        //assertNotNull(documentStore.get(uri5));
        
    }

    @Test
    public void testUndo() throws Exception {
        DocumentStore documentStore = new DocumentStoreImpl();
        // add a document to the store
        URI uri1 = new URI("AAAAA");
        String text = "Hello, world!";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes());
        documentStore.put(inputStream, uri1, DocumentFormat.TXT);
        assertEquals(text, documentStore.get(uri1).getDocumentTxt());

        // modify the document
        String newText = "Goodbye, world!";
        InputStream newInputStream = new ByteArrayInputStream(newText.getBytes());
        documentStore.put(newInputStream, uri1, DocumentFormat.TXT);

        // undo the modification
        documentStore.undo();
        assertEquals(text, documentStore.get(uri1).getDocumentTxt());
    }

    @Test
    public void testSearch() throws Exception {
        DocumentStore documentStore = new DocumentStoreImpl();
        // add some documents to the store
        String[] texts = {"Hello, world!", "Goodbye, world word world", "The quick brown fox jumps over the lazy dog.", "The early bird catches the worm"};
        URI[] uris = {new URI("https://URI1"), new URI("https://URI2"), new URI("https://URI3"), new URI("https://URI4")};
        List<Document> documents = new ArrayList<>();
        for (int i=0; i<texts.length; i++) {
            InputStream inputStream = new ByteArrayInputStream(texts[i].getBytes());
            Document doc = new DocumentImpl(uris[i], texts[i]);
            documents.add(doc);
            documentStore.put(inputStream, uris[i], DocumentFormat.TXT);
        }

        // search for a document that contains "hello"
        List<Document> searchResults = documentStore.search("Hello");
        assertEquals(1, searchResults.size());
        assertEquals(texts[0], searchResults.get(0).getDocumentTxt());

        // search for a document that contains "world"
        searchResults = documentStore.search("world");
        assertEquals(2, searchResults.size());
        assertEquals(texts[1], searchResults.get(0).getDocumentTxt());
        assertEquals(texts[0], searchResults.get(1).getDocumentTxt());

        // search for a document that contains "fox"
        searchResults = documentStore.search("fox");
        assertEquals(1, searchResults.size());
        assertEquals(texts[2], searchResults.get(0).getDocumentTxt());

        // search for a document that contains "cat" (not found)
        searchResults = documentStore.search("cat");
        assertEquals(0, searchResults.size());
    }

    @Test
    public void testSetMaxDocumentCount() throws Exception {
        DocumentStore documentStore = new DocumentStoreImpl();
        // add some documents to the store
        String[] texts = {"Document 1", "Document 2", "Document 3", "Document 4"};
        URI[] uris = {new URI("https://URI1"), new URI("https://URI2"), new URI("https://URI3"), new URI("https://URI4")};
        for (int i=0; i<texts.length; i++) {
            InputStream inputStream = new ByteArrayInputStream(texts[i].getBytes());
            documentStore.put(inputStream, uris[i], DocumentFormat.TXT);
        }

        // set max document count to 3
        documentStore.setMaxDocumentCount(3);

        // add another document
        String text = "Document 5";
        URI uri = new URI("https://URI5");
        InputStream inputStream = new ByteArrayInputStream(text.getBytes());
        documentStore.put(inputStream, uri, DocumentFormat.TXT);
        Document document = new DocumentImpl(uri, text);

        // check that the first document was removed
        List<Document> allDocuments = documentStore.search("Document");
        assertEquals(3, allDocuments.size());
        assertTrue(allDocuments.contains(document));
        assertFalse(allDocuments.contains(new DocumentImpl(uris[0], texts[0])));
        assertNull(documentStore.get(uris[0]));
        assertNull(documentStore.get(uris[1]));
        assertNotNull(documentStore.get(uris[2]));
    }

    @Test
    public void testSetMaxDocumentBytes() throws IOException {
        DocumentStore documentStore = new DocumentStoreImpl();
        // add some documents to the store
        String[] texts = {"This is a"};
    }
}