package com.ftn.lucene.retriever;

import com.ftn.lucene.analyzer.SerbianAnalyzer;
import com.ftn.lucene.model.ResultData;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
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
public class InformationRetriever {

    private static final Version version = Version.LUCENE_4_9;

    private String rawDirectoryPath;

    private String indexDirectoryPath;

    private Analyzer analyzer = new SerbianAnalyzer();

    private DocumentRetriever documentRetriever;

    public InformationRetriever(String rawDirectoryPath, String indexDirectoryPath, int maxHits) {
        this.rawDirectoryPath = rawDirectoryPath;
        this.indexDirectoryPath = indexDirectoryPath;
        this.documentRetriever = new DocumentRetriever(indexDirectoryPath, maxHits);
    }

    public InformationRetriever(String rawDirectoryPath, String indexDirectoryPath) {
        this.rawDirectoryPath = rawDirectoryPath;
        this.indexDirectoryPath = indexDirectoryPath;
        this.documentRetriever = new DocumentRetriever(indexDirectoryPath);
    }

    //TODO: Check query before call
    //TODO: Set sort properly
    public List<ResultData> retrieveEBooks(Query query, List<String> fieldNames, Sort sort) {
        List<ResultData> results = new ArrayList<>();
        List<Document> documents = this.documentRetriever.retrieveDocuments(query,true, sort);

        for(Document document : documents){
            ResultData resultData = generateResultData(document);
            String highlights = generateHighlights(query, document, fieldNames);
            resultData.setHighlights(highlights);
            results.add(resultData);
        }

        return results;
    }

    private ResultData generateResultData(Document document){
        ResultData resultData = new ResultData();
        resultData.setId(Long.parseLong(document.get("id")));
        resultData.setTitle(document.get("title"));
        resultData.setLanguage(document.get("language"));
        resultData.setCategory(document.get("category"));
        resultData.setFileName(document.get("filename"));
        resultData.setAuthor(document.get("author"));
        resultData.setKeywords(generateKeywords(document));
        resultData.setYear(document.get("year"));
        return resultData;
    }

    private String generateKeywords(Document document){
        String result = "";
        StringBuilder stringBuilder = new StringBuilder("");
        String[] keywords = document.getValues("keyword");
        for (String keyword : keywords){
            stringBuilder.append(keyword).append(",");
        }
        result = stringBuilder.toString().trim();
        result = result.substring(0, result.length()-1);
        return result;
    }

    private String generateHighlights(Query query, Document document, List<String> fieldNames){
        String highlights = "";
        StringBuilder stringBuilder = new StringBuilder("");
        for (String fieldName : fieldNames){
            try {
                Directory directory = new SimpleFSDirectory(new File(indexDirectoryPath));
                DirectoryReader directoryReader = DirectoryReader.open(directory);
                Highlighter highlighter = new Highlighter(new QueryScorer(query, directoryReader, fieldName));
                String fieldValue = document.get(fieldName);
                String highlight = highlighter.getBestFragment(analyzer, fieldName, fieldValue);

                if (highlight != null && !highlight.isEmpty()) {
                    stringBuilder.append(fieldName).append(": ").append(highlight.trim()).append("...");
                    highlights = stringBuilder.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidTokenOffsetsException e) {
                e.printStackTrace();
            }
        }
        return highlights;
    }
}
