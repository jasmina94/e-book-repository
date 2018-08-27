package com.ftn.controller;

import com.ftn.constants.Auth;
import com.ftn.exception.BadRequestException;
import com.ftn.model.dto.CategoryDTO;
import com.ftn.service.CategoryService;
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
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Transactional
    @GetMapping
    public ResponseEntity read() {
        return new ResponseEntity<>(categoryService.readAll(), HttpStatus.OK);
    }

    @Transactional
    @PreAuthorize(Auth.ADMIN)
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException();
        return new ResponseEntity<>(categoryService.create(categoryDTO), HttpStatus.OK);
    }

    @Transactional
    @PreAuthorize(Auth.ADMIN)
    @PatchMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable Long id,@Valid @RequestBody CategoryDTO categoryDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException();
        return new ResponseEntity<>(categoryService.update(id, categoryDTO), HttpStatus.OK);
    }

    @Transactional
    @PreAuthorize(Auth.ADMIN)
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        boolean result = categoryService.delete(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
