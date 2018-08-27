package com.ftn.controller;

import com.ftn.constants.Auth;
import com.ftn.exception.BadRequestException;
import com.ftn.model.dto.PasswordDTO;
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
    @GetMapping(value = "/subs")
    public ResponseEntity readSubs() {
        return new ResponseEntity<>(userService.readSubscribers(), HttpStatus.OK);
    }

    @Transactional
    @PreAuthorize(Auth.ADMIN)
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException();
        return new ResponseEntity<>(userService.create(userDTO), HttpStatus.OK);
    }

    @Transactional
    @PreAuthorize(Auth.AUTHENTICATED)
    @PatchMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable Long id,@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException();
        return new ResponseEntity<>(userService.update(id, userDTO), HttpStatus.OK);
    }

    @Transactional
    @PreAuthorize(Auth.AUTHENTICATED)
    @PatchMapping(value = "/pass/{id}")
    public ResponseEntity updatePass(@PathVariable Long id, @Valid @RequestBody PasswordDTO passwordDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException();
        return new ResponseEntity<>(userService.updatePassword(id, passwordDTO), HttpStatus.OK);
    }

    @Transactional
    @PreAuthorize(Auth.AUTHENTICATED)
    @PatchMapping(value = "/unique/{username}")
    public ResponseEntity uniqueUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.uniqueUsername(username), HttpStatus.OK);
    }

    @Transactional
    @PreAuthorize(Auth.ADMIN)
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Transactional
    @PreAuthorize(Auth.AUTHENTICATED)
    @PostMapping(value = "/{userId}/{categoryId}")
    public ResponseEntity subscribe(@PathVariable("userId") Long userId, @PathVariable("categoryId") Long categoryId){
        userService.subscribe(userId, categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
