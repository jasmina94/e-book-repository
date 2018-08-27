package com.ftn.lucene;

import com.ftn.lucene.analyzer.SerbianAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

/**
 * Created by Jasmina on 28/07/2018.
 */
public class EBookIndexer {

    private static final Version version = Version.LUCENE_4_9;

    private Analyzer analyzer = new SerbianAnalyzer();

    private IndexWriterConfig indexWriterConfig = new IndexWriterConfig(version, analyzer);

    private IndexWriter indexWriter;

    private Directory indexDirectory;


    public EBookIndexer(String path){
        this(path, false);
    }

    /**
     *
     * @param path - address to directory where indexes are strored
     * @param restart - <b>true</b> if new index is going to be created, <b>false</b> to continue with using old index
     */
    public EBookIndexer(String path, boolean restart){
        try {
            indexDirectory = new SimpleFSDirectory(new File(path));
            if (restart) {
                indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
                indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
                indexWriter.commit();
                indexWriter.close();
            }

        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid path for storing index!");
        }
    }

    private void openIndexWriter() throws IOException {
        indexWriterConfig = new IndexWriterConfig(version, analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
    }

    public boolean addDocumentToIndex(Document document){
        boolean success;
        try {
            openIndexWriter();
            indexWriter.addDocument(document);
            indexWriter.commit();
            indexWriter.close();
            success = true;
        } catch (IOException e) {
            success = false;
        }
        return success;
    }

    /**
     *
     * @param document - the document which will be updated
     * @param fields - array of updating fields
     * @return true if update was successful, other false
     */
    public boolean updateDocumentInIndex(Document document, IndexableField... fields){
        boolean success;
        String id = document.get("id");
        replaceFields(document, fields);
        try{
            synchronized (this) {
                openIndexWriter();
                this.indexWriter.updateDocument(new Term("id", id), document);
                this.indexWriter.forceMergeDeletes();
                this.indexWriter.deleteUnusedFiles();
                this.indexWriter.commit();
                this.indexWriter.close();
            }
            return true;
        }catch(Exception ex){
            return false;
        }
    }

    private void replaceFields(Document document, IndexableField[] fields) {
        for(IndexableField field : fields){
            document.removeFields(field.name());
        }
        for(IndexableField field : fields){
            document.add(field);
        }
    }


    public boolean deleteDocument(Document document) {
        boolean success = false;
        if (document != null) {
            success = deleteDocument("id", document.get("id"));
        }
        return success;
    }

    public boolean deleteDocumentByIdField(String fieldValue) {
        return deleteDocument("id", fieldValue);
    }

    //Main delete method
    private boolean deleteDocument(String fieldName, String fieldValue) {
        boolean success;
        Term term = new Term(fieldName, fieldValue);
        success = deleteDocuments(term);
        return success;
    }

    private boolean deleteDocuments(Term... terms) {
        boolean success;
        try {
            synchronized (this) {
                openIndexWriter();
                indexWriter.deleteDocuments(terms);
                indexWriter.deleteUnusedFiles();
                indexWriter.forceMergeDeletes();
                indexWriter.commit();
                indexWriter.close();
            }
            success = true;
        }
        catch (IOException e) {
            success = false;
        }
        return success;
    }

    //Method getting all lucene indexed documents
    public Document[] getAllDocuments() {
        Document[] documents = null;
        try {
            DirectoryReader directoryReader = DirectoryReader.open(indexDirectory);
            documents = new Document[directoryReader.maxDoc()];
            for (int i = 0; i < directoryReader.maxDoc(); i++) {
                documents[i] = directoryReader.document(i);
            }
            directoryReader.close(); // TODO Check this
        }
        catch (IOException e) {
            documents = null;
        }
        return documents;
    }
}
