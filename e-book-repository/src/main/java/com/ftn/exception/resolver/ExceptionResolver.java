package com.ftn.exception.resolver;

import com.ftn.exception.*;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@ControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity notFoundException(HttpServletRequest request, NotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity badRequestException(HttpServletRequest request, BadRequestException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity forbiddenException(HttpServletRequest request, ForbiddenException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity authenticationException(HttpServletRequest request, AuthenticationException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity methodNotAllowedException(HttpServletRequest request, MethodNotAllowedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity dataAccessException(HttpServletRequest request, DataAccessException exception) {
        return new ResponseEntity<>("{\"message\": \"" + exception.getMessage() + "\"}", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IncompleteIndexDocumentException.class)
    public ResponseEntity incompleteIndexDocumentException(HttpServletRequest request, IncompleteIndexDocumentException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ParseQueryException.class)
    public ResponseEntity parseQueryException(HttpServletRequest request, ParseQueryException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity illegalArgument(HttpServletRequest request, IllegalArgumentException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}