package com.ftn.service;

import com.ftn.model.dto.UserDTO;

import java.util.List;

/**
 * Created by Jasmina on 16/06/2018.
 */
public interface UserService {

    UserDTO readSelf();

    List<UserDTO> readAll();

    List<UserDTO> readSubscribers();

    UserDTO create(UserDTO userDTO);

    UserDTO update(Long id, UserDTO userDTO);

    void delete(Long id);
}
