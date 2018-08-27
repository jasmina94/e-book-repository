package com.ftn.exception;

/**
 * Created by Jasmina on 24/07/2018.
 */
public class IncompleteIndexDocumentException extends Exception {

    public IncompleteIndexDocumentException(){
        super("Document is incomplete. Default metadata is not present");
    }

    public IncompleteIndexDocumentException(String message){
        super(message);
    }
}
