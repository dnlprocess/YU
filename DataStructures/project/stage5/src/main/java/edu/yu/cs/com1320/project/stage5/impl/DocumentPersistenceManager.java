package edu.yu.cs.com1320.project.stage5.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import com.google.gson.JsonSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;

/**
 * created by the document store and given to the BTree via a call to BTree.setPersistenceManager
 */
public class DocumentPersistenceManager implements PersistenceManager<URI, Document> {
    private File baseDir;

    /**
     * Use baseDir, or if null user.dir 
     */
    public DocumentPersistenceManager(File baseDir){
        this.baseDir = baseDir == null ? new File(System.getProperty("user.dir")) : baseDir;
    }

    @Override
    public void serialize(URI uri, Document val) throws IOException {
        String path = getFilePath(uri);
        File file = new File(path);
        createPathDirectories(file);

        try (Writer writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().registerTypeAdapter(Document.class, new DocumentSerializer()).create();
            gson.toJson(val, writer);
        }
    }

    @Override
    public Document deserialize(URI uri) throws IOException {
        String path = getFilePath(uri);
        File file = new File(path);

        if (!file.exists()) {
            return null;
        }

        Gson gson = new GsonBuilder().registerTypeAdapter(Document.class, new DocumentDeserializer()).create();
        
        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, Document.class);
        }
    }

    @Override
    public boolean delete(URI uri) throws IOException {
        String path = getFilePath(uri);
        File file = new File(path);
        return file.exists() && file.delete();
    }

    private String getFilePath(URI uri) {
        String uriString = uri.toString();
        int index = uriString.indexOf("://");
        if (index != -1) {
            uriString = uriString.substring(index + 3);
        }
        if (uriString.startsWith("/")) {
            uriString = uriString.substring(1);
        }
        return new File(baseDir, uriString + ".json").getPath();
    }

    private void createPathDirectories(File file) {
        File parentDirectory = file.getParentFile();
        if (!parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }
    }

    private class DocumentSerializer implements JsonSerializer<Document> {
        @Override
        public JsonElement serialize(Document document, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("uri", document.getKey().toString());

            if (document.getDocumentTxt() != null) {
                jsonObject.addProperty("text", document.getDocumentTxt());
                jsonObject.add("wordMap", context.serialize(document.getWordMap()));
            } else {
                jsonObject.add("binaryData", context.serialize(document.getDocumentBinaryData()));
            }

            return jsonObject;
        }
    }

    private class DocumentDeserializer implements JsonDeserializer<Document> {
        @Override
        public Document deserialize(JsonElement json, Type typeOfSrc, JsonDeserializationContext context) {
            JsonObject jsonObject = json.getAsJsonObject();

            URI uri = null;
            try {
                uri = new URI(jsonObject.get("uri").getAsString());
            } catch (URISyntaxException e) {
            }

            Document doc;

            if (jsonObject.has("text")) {
                doc = new DocumentImpl(uri, jsonObject.get("text").getAsString(), context.deserialize(jsonObject.get("wordMap"), Map.class));
            } else {
                doc = new DocumentImpl(uri, context.deserialize(jsonObject.get("binaryData"), byte[].class));
            }

            doc.setLastUseTime(System.nanoTime());

            return doc;
        }
    }
}
