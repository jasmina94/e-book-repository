package com.ftn.controller;

import com.ftn.exception.BadRequestException;
import com.ftn.model.dto.EBookDTO;
import com.ftn.service.EBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody EBookDTO eBookDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException();
        return new ResponseEntity<>(eBookService.create(eBookDTO), HttpStatus.OK);
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
    public ResponseEntity update(@PathVariable Long id) {
        eBookService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
