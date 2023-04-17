import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.stage3.Document;
import edu.yu.cs.com1320.project.stage3.DocumentStore;
import edu.yu.cs.com1320.project.stage3.DocumentStore.DocumentFormat;
import edu.yu.cs.com1320.project.stage3.impl.DocumentStoreImpl;
import edu.yu.cs.com1320.project.stage3.impl.DocumentImpl;
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

public class DocumentStoreImplTest {

    private DocumentStoreImpl store;
    private URI uri1, uri2, uri3, uri4, uri5;
    private Document doc1, doc2, doc3, doc4, doc5;
    private InputStream inputStream1, inputStream2, inputStream3, inputStream4, inputStream5;

    @Before
    public void setUp() throws URISyntaxException, IOException {
        store = new DocumentStoreImpl();
        uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        String txt1 = "This is the first test document.";
        byte[] binaryData1 = {0x00, 0x01, 0x02, 0x03, 0x04};
        inputStream1 = new ByteArrayInputStream(txt1.getBytes());
        doc1 = new DocumentImpl(uri1, txt1);
        store.put(inputStream1, uri1, DocumentFormat.TXT);
        
        
        uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        String txt2 = "This is the second test document.";
        byte[] binaryData2 = {0x05, 0x06, 0x07, 0x08, 0x09};
        inputStream2 = new ByteArrayInputStream(txt2.getBytes());
        doc2 = new DocumentImpl(uri2, txt2);
        store.put(inputStream2, uri2, DocumentFormat.TXT);

        uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        String txt3 = "This is the third test document.";
        byte[] binaryData3 = {0x0A, 0x0B, 0x0C, 0x0D, 0x0E};
        inputStream3 = new ByteArrayInputStream(binaryData3);
        doc3 = new DocumentImpl(uri3, inputStream3.readAllBytes());
        inputStream3 = new ByteArrayInputStream(binaryData3);
        store.put(inputStream3, uri3, DocumentFormat.BINARY);

        uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        String txt4 = "This is the fourth test document.";
        byte[] binaryData4 = {0x0F, 0x10, 0x11, 0x12, 0x13};
        inputStream4 = new ByteArrayInputStream(binaryData4);
        doc4 = new DocumentImpl(uri4, inputStream4.readAllBytes());
        inputStream4 = new ByteArrayInputStream(binaryData3);
        store.put(inputStream4, uri4, DocumentFormat.BINARY);
        
        uri5 = new URI("http://edu.yu.cs/com1320/project/doc5");
        String txt5 = "This is the fifth test document.";
        byte[] binaryData5 = {0x14, 0x15, 0x16, 0x17, 0x18};
        inputStream5 = new ByteArrayInputStream(txt5.getBytes());
        doc5 = new DocumentImpl(uri5, txt5);
        store.put(inputStream5, uri5, DocumentFormat.TXT);
    }

    @Test
    public void testPutAndGetTxtDocument() throws Exception {
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
    public void testSearch() throws Exception {
        // Test searching for documents
        List<Document> result = store.search("This");
        assertEquals(3, result.size());
        assertTrue(result.contains(uri1));
        assertTrue(result.contains(uri2));
        assertTrue(result.contains(uri5));
    }

    @Test
    public void testNullInputStream() throws Exception {
        // Test passing a null InputStream to put() meaning delete
        assertEquals(0, store.put(null, new URI("http://example.com/test"), DocumentFormat.TXT));
        
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullURI() throws Exception {
        // Test passing a null URI to put()
        InputStream stream = new ByteArrayInputStream("This is a test document.".getBytes());
        store.put(stream, null, DocumentFormat.TXT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormat() throws Exception {
        // Test passing a null format to put()
        InputStream stream = new ByteArrayInputStream("This is a test document.".getBytes());
        store.put(stream, new URI("http://example.com/test"), null);
    }

    @Test
    public void testTrie() throws Exception {
        List<Document> docs = store.searchByPrefix("t");
        assertEquals(6, docs.size());
        assertEquals(doc1, store.get(uri1));
        assertEquals(doc1.getWords(), store.get(uri1).getWords());
        
        store.deleteAllWithPrefix("t");
            docs = store.searchByPrefix("t");
            assertEquals(0, docs.size());
        store.undo();
            docs = store.searchByPrefix("t");
            assertEquals(6, docs.size());

        store.undo();
        docs = store.searchByPrefix("t");
        assertEquals(4, docs.size());
    }
}
