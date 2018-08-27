package com.ftn.exception;

/**
 * Created by Jasmina on 17/08/2018.
 */
public class ParseQueryException extends Exception {

    public ParseQueryException(String fieldName, String fieldValue) {
        super("Error happened while parsing field: " + fieldName + " with value: "+fieldValue);
    }

    public ParseQueryException(String message) {
        super(message);
    }
}
