package edu.yu.cs.com1320.project.stage2.impl;

import edu.yu.cs.com1320.project.stage2.Document;
import java.util.Arrays;
import java.net.URI;

public class DocumentImpl implements Document{

    private URI uri;
    private String text;
    private byte[] binaryData;

    public DocumentImpl(URI uri, byte[] binaryData) {
        testURI(uri);
        if (binaryData == null || binaryData.length == 0) {
            throw new IllegalArgumentException();
        }
        this.uri = uri;
        this.binaryData = binaryData;
        this.text = null;
    }

    public DocumentImpl(URI uri, String txt) {
        testURI(uri);
        if (txt == null || txt.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.uri = uri;
        this.text = txt;
        this.binaryData = null;
    }

    /**
     * @return content of text document
     */
    public String getDocumentTxt() {
        return this.text;
    }

    /**
     * @return content of binary data document
     */
    public byte[] getDocumentBinaryData() {
        return this.binaryData;
    }

    /**
     * @return URI which uniquely identifies this document
     */
    public URI getKey() {
        return this.uri;
    }

    @Override
    public int hashCode() {
        int result = this.uri.hashCode();
        result = 31 * result + (this.text != null ? this.text.hashCode() : 0); result = 31 * result + Arrays.hashCode(this.binaryData);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Document)) {
            return false;
        }
        Document otherDoc = (Document) obj;
        return this.hashCode() == otherDoc.hashCode() ? true: false;
    }

    private void testURI(URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException();
        }
    }
}