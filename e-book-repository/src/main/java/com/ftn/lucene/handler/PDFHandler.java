package com.ftn.lucene.handler;

import com.ftn.exception.IncompleteIndexDocumentException;
import com.ftn.model.EBook;
import org.apache.lucene.document.*;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Jasmina on 24/07/2018.
 */
public class PDFHandler extends DocumentHandler {


    @Override
    public Document getDocument(EBook eBook, File file) throws IncompleteIndexDocumentException {
        Document document = new Document();
        String error = "";

        addDocMetadata(eBook, document);
        error = addDocContent(file, document);

        if(!error.isEmpty()) {
            System.out.println("PDF doc " + eBook.getTitle() + " error.");
            error += "Document is incomplete. An exception occurred";
            throw new IncompleteIndexDocumentException(error);
        }

        return document;
    }

    private void addDocMetadata(EBook eBook, Document document){
        document.add(new StringField("id", eBook.getId().toString(), Field.Store.YES));
        document.add(new TextField("title", eBook.getTitle(), Field.Store.YES));
        document.add(new TextField("filename", eBook.getFilename(), Field.Store.YES));
        document.add(new TextField("language", eBook.getLanguage().getName(), Field.Store.YES));
        document.add(new TextField("category", eBook.getCategory().getName(), Field.Store.YES));

        //Check non-required properties
        if(!eBook.getAuthor().trim().isEmpty() && eBook.getAuthor() != null){
            document.add(new TextField("author", eBook.getAuthor(), Field.Store.YES));
        }
        if(eBook.getPublicationYear() != null){
            document.add(new TextField("year", eBook.getPublicationYear().toString(), Field.Store.YES));
        }

        if(!eBook.getKeywords().trim().isEmpty() && eBook.getKeywords() != null){
            String[] keywords = eBook.getKeywords().trim().split(",");
            for (String keyword : keywords){
                if(!keyword.trim().isEmpty() && keyword != null){
                    document.add(new TextField("keyword", keyword.trim(), Field.Store.YES));
                }
            }
        }
    }

    private String addDocContent(File file, Document document){
        String error = "";
        String content = generateTextContentFromPDF(file);
        if(!content.isEmpty() && content != null){
            document.add(new TextField("content", content, Field.Store.YES));
        }else {
            error = "Document without content!\n";
        }
        return error;
    }

    private String generateTextContentFromPDF(File file) {
        String textContent = "";
        try {
            PDFParser parser = new PDFParser(new FileInputStream(file));
            parser.parse();

            PDDocument pdf = parser.getPDDocument();
            PDFTextStripper pdfTextStripper = new PDFTextStripper("UTF-8");

            textContent = pdfTextStripper.getText(pdf);
            pdf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textContent;
    }
}
