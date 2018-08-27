package com.ftn.controller;

import com.ftn.constants.Auth;
import com.ftn.exception.BadRequestException;
import com.ftn.model.dto.LanguageDTO;
import com.ftn.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Jasmina on 16/06/2018.
 */
@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    @Autowired
    private LanguageService languageService;

    @Transactional
    @GetMapping
    public ResponseEntity read() {
        return new ResponseEntity<>(languageService.readAll(), HttpStatus.OK);
    }

    @Transactional
    @PreAuthorize(Auth.ADMIN)
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody LanguageDTO languageDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException();
        return new ResponseEntity<>(languageService.create(languageDTO), HttpStatus.OK);
    }

    @Transactional
    @PreAuthorize(Auth.ADMIN)
    @PatchMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable Long id,@Valid @RequestBody LanguageDTO languageDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException();
        return new ResponseEntity<>(languageService.update(id, languageDTO), HttpStatus.OK);
    }

    @Transactional
    @PreAuthorize(Auth.ADMIN)
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        languageService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
