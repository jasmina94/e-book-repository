package com.ftn.lucene.retriever;

import com.ftn.lucene.analyzer.SerbianAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasmina on 17/08/2018.
 */
public class DocumentRetriever {

    private static final Version version = Version.LUCENE_4_9;

    private Analyzer analyzer = new SerbianAnalyzer();

    private String indexDirectoryPath;

    private int maxHits = 10;

    public DocumentRetriever(String indexDirectoryPath, int maxHits) {
        this.indexDirectoryPath = indexDirectoryPath;
        this.maxHits = maxHits;
    }

    public DocumentRetriever(String indexDirectoryPath) {
        this.indexDirectoryPath = indexDirectoryPath;
    }

    public List<Document> retrieveDocuments(Query query, boolean analyze, Sort sort) {
        List<Document> documents = new ArrayList<>();
        if (query != null) {
            try {
                Directory indexDirectory = new SimpleFSDirectory(new File(indexDirectoryPath));
                DirectoryReader directoryReader = DirectoryReader.open(indexDirectory);

                IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
                ScoreDoc[] scoreDocs = null;

                if (analyze) {
                    QueryParser queryParser = new QueryParser(version, "", analyzer);
                    query = queryParser.parse(query.toString());
                }
                if (sort == null) {
                    sort = Sort.INDEXORDER;
                }

                scoreDocs = indexSearcher.search(query, this.maxHits, sort).scoreDocs;
                for (ScoreDoc scoreDoc : scoreDocs) {
                    documents.add(indexSearcher.doc(scoreDoc.doc));
                }
            } catch (ParseException | IOException exception) {
                exception.printStackTrace();
            }
        }
        return documents;
    }

    public String getIndexDirectoryPath() {
        return indexDirectoryPath;
    }

    public void setIndexDirectoryPath(String indexDirectoryPath) {
        this.indexDirectoryPath = indexDirectoryPath;
    }

    public int getMaxHits() {
        return maxHits;
    }

    public void setMaxHits(int maxHits) {
        this.maxHits = maxHits;
    }
}
