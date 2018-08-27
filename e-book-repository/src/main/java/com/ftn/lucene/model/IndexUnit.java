package com.ftn.lucene.model;

import lombok.Data;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasmina on 29/07/2018.
 */
@Data
public class IndexUnit {

    private String text;
    private String title;
    private List<String> keywords = new ArrayList<>();
    private String fileName;
    private String fileDate;


    public Document getLuceneDocument(){
        Document retVal = new Document();
        retVal.add(new TextField("text", text, Field.Store.NO));
        retVal.add(new TextField("title", title, Field.Store.YES));
        for (String keyword : keywords) {
            retVal.add(new TextField("keyword", keyword, Field.Store.YES));
        }
        retVal.add(new StringField("filename", fileName, Field.Store.YES));
        retVal.add(new TextField("filedate", fileDate, Field.Store.YES));

        return retVal;
    }
}
