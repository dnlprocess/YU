package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.net.URI;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class DocumentImpl implements Document {

    private URI uri;
    private String text;
    private byte[] binaryData;
    private Map<String, Integer> wordCountMap;
    private long lastUseTime;

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

    public DocumentImpl(URI uri, String text, Map<String, Integer> wordCountMap) {
        testURI(uri);
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException(text);
        }
        this.uri = uri;
        this.text = text;
        this.binaryData = null;
        this.wordCountMap = wordCountMap == null ? map(text) : wordCountMap;
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
        return Utils.calculateHashCode(this.uri, this.text, this.binaryData);
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

    private Map<String, Integer> map(String txt) {
        //string won't be empty
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
        ArrayList<String> words = new ArrayList<String>();
        Matcher matcher = pattern.matcher(txt);
        Map<String, Integer> wordCountMap = new HashMap<String, Integer>();
        
        while (matcher.find()) {
            String word = matcher.group();
            words.add(word);
        }

        for (String word: words) {
            Integer count = wordCountMap.get(word);
            wordCountMap.put(word, (count == null? 0: count) +1);
        }

        return wordCountMap;
    }

	@Override
	public int compareTo(Document o) {
        //add safeguards
        if (o == null) {
            throw new IllegalArgumentException();
        }
		return (int) ((int) this.getLastUseTime() - o.getLastUseTime());
	}

	@Override
	public long getLastUseTime() {
        return this.lastUseTime;
	}

	@Override
	public void setLastUseTime(long timeInNanoseconds) {
        this.lastUseTime = timeInNanoseconds;
	}

    @Override
    public Map<String, Integer> getWordMap() {
        return new HashMap<String, Integer>(this.wordCountMap);
    }

    @Override
    public void setWordMap(Map<String, Integer> wordMap) {
        this.wordCountMap = wordMap;
    }
}