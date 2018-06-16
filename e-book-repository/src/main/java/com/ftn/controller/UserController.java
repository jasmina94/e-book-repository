package com.ftn.controller;

import com.ftn.constants.Auth;
import com.ftn.exception.BadRequestException;
import com.ftn.model.dto.UserDTO;
import com.ftn.service.UserService;
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
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    @Transactional
    @PreAuthorize(Auth.AUTHENTICATED)
    @GetMapping(value = "/me")
    public ResponseEntity readMe() {
        return new ResponseEntity<>(userService.readSelf(), HttpStatus.OK);
    }

    @Transactional
    @GetMapping
    public ResponseEntity read() {
        return new ResponseEntity<>(userService.readAll(), HttpStatus.OK);
    }

    @Transactional
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException();
        return new ResponseEntity<>(userService.create(userDTO), HttpStatus.OK);
    }

    @Transactional
    @PatchMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable Long id,@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException();
        return new ResponseEntity<>(userService.update(id, userDTO), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
