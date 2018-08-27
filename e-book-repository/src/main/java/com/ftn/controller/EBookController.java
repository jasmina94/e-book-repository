package com.ftn.controller;

import com.ftn.constants.Auth;
import com.ftn.exception.BadRequestException;
import com.ftn.model.EBook;
import com.ftn.model.dto.EBookBaseDTO;
import com.ftn.model.dto.EBookDTO;
import com.ftn.model.dto.MultipleFieldSearchDTO;
import com.ftn.model.dto.SingleFieldSearchDTO;
import com.ftn.service.EBookService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Jasmina on 16/06/2018.
 */
@RestController
@RequestMapping("/api/ebooks")
public class EBookController {


    @Autowired
    private EBookService eBookService;

    @Transactional
    @GetMapping
    public ResponseEntity read() {
        return new ResponseEntity<>(eBookService.readAll(), HttpStatus.OK);
    }

    @Transactional
    @GetMapping(value = "/{id}")
    public ResponseEntity readOne(@PathVariable Long id){
        return new ResponseEntity<>(eBookService.readById(id), HttpStatus.OK);
    }

    @Transactional
    @GetMapping(value = "/category/{id}")
    public ResponseEntity readForCategory(@PathVariable Long id){
        return new ResponseEntity<>(eBookService.readByCategory(id), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/upload")
    public ResponseEntity addEBookFile(@RequestParam("file") MultipartFile file) {
        ResponseEntity responseEntity;
        if (!file.isEmpty()) {
            try {
                File eBookFile = eBookService.saveEBookFile(file.getBytes());
                EBookBaseDTO eBookBaseDTO = eBookService.prepareBase(eBookFile);
                if(eBookBaseDTO != null){
                    responseEntity = new ResponseEntity<>(eBookBaseDTO, HttpStatus.OK);
                }else {
                    responseEntity = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            catch (IOException e) {
                responseEntity = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @Transactional
    @PostMapping
    public ResponseEntity save(@Valid @RequestBody EBookDTO eBookDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new BadRequestException();
        return new ResponseEntity(eBookService.saveAndIndex(eBookDTO),HttpStatus.OK);
    }

    @Transactional
    @PatchMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable Long id,@Valid @RequestBody EBookDTO eBookDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException();
        return new ResponseEntity<>(eBookService.update(id, eBookDTO), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        return new ResponseEntity<>(eBookService.delete(id), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping(value = "/file/{fileName}")
    public ResponseEntity deleteFile(@PathVariable String fileName) {
        return new ResponseEntity<>(eBookService.deleteFile(fileName), HttpStatus.OK);
    }

    @Transactional
    @GetMapping(value = "/download/{id}")
    public ResponseEntity download(@PathVariable Long id){
        ResponseEntity responseEntity;
        String mime = "application/pdf";
        Resource resource = eBookService.loadFile(id);
        String filename = eBookService.getFileName(id);
        try {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("filename", filename);
            responseEntity = ResponseEntity.ok()
                    .headers(responseHeaders)
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.parseMediaType(mime))
                    .body(resource);
        } catch (IOException e) {
            responseEntity = new ResponseEntity<Resource>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @Transactional
    @PostMapping(value = "/ssearch")
    public ResponseEntity searchSingle(@Valid @RequestBody SingleFieldSearchDTO singleFieldSearchDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new BadRequestException();
        return new ResponseEntity(eBookService.search(singleFieldSearchDTO), HttpStatus.OK);
    }

    @Transactional
    @PostMapping(value = "/msearch")
    public ResponseEntity searchMultiple(@Valid @RequestBody MultipleFieldSearchDTO multipleFieldSearchDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new BadRequestException();
        return new ResponseEntity(eBookService.search(multipleFieldSearchDTO), HttpStatus.OK);
    }
}
