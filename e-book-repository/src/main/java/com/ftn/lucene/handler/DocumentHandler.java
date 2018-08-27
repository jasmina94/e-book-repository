package com.ftn.lucene.handler;

import com.ftn.exception.IncompleteIndexDocumentException;
import com.ftn.model.EBook;
import com.ftn.model.Language;
import org.apache.lucene.document.Document;

import java.io.File;

/**
 * Created by Jasmina on 24/07/2018.
 */
public abstract class DocumentHandler {

    public abstract Document getDocument(EBook eBook, File file) throws IncompleteIndexDocumentException;
}
