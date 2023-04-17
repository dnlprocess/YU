package edu.yu.cs.com1320.project.stage3.impl;

import edu.yu.cs.com1320.project.stage3.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.net.URI;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashSet;
import java.util.HashMap;

public class DocumentImpl implements Document{

    private URI uri;
    private String text;
    private byte[] binaryData;
    private HashMap<String, Integer> wordCountMap;

    public DocumentImpl(URI uri, byte[] binaryData) {
        testURI(uri);
        if (binaryData == null || binaryData.length == 0) {
            throw new IllegalArgumentException();
        }
        this.uri = uri;
        this.binaryData = binaryData;
        this.text = null;
        this.wordCountMap = new HashMap<String, Integer>();
    }

    public DocumentImpl(URI uri, String txt) {
        testURI(uri);
        if (txt == null || txt.isEmpty()) {
            throw new IllegalArgumentException(txt);
        }
        this.uri = uri;
        this.text = txt;
        this.binaryData = null;
        this.wordCountMap = new HashMap<String, Integer>();
        map(txt);
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

    /**
     * how many times does the given word appear in the document?
     * @param word
     * @return the number of times the given words appears in the document. If it's a binary document, return 0.
     */
    public int wordCount(String word) {
        if(this.text == null) {
            return 0;
        }
        Integer count = this.wordCountMap.get(word);
        return count == null ? 0 : count.intValue();
    }

    /**
     * @return all the words that appear in the document
     */
    public Set<String> getWords() {
        Set<String> set = new HashSet<String>();
        if (this.text == null) {
            return set;
        }
        set = this.wordCountMap.keySet();
        return set;
    }

    private void map(String txt) {
        //string won't be empty
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
        ArrayList<String> words = new ArrayList<String>();
        Matcher matcher = pattern.matcher(txt);
        
        while (matcher.find()) {
            String word = matcher.group();
            words.add(word);
        }

        for (String word: words) {
            Integer count = this.wordCountMap.get(word);
            this.wordCountMap.put(word, (count == null? 0: count) +1);
        }
    }
}