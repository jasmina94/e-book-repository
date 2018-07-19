package com.ftn.service;

import com.ftn.model.dto.PasswordDTO;
import com.ftn.model.dto.UserDTO;

import java.util.List;

/**
 * Created by Jasmina on 16/06/2018.
 */
public interface UserService {

    UserDTO readSelf();

    List<UserDTO> readAll();

    List<UserDTO> readSubscribers();

    List<UserDTO> readSubscribersTo(Long categoryId);

    UserDTO create(UserDTO userDTO);

    UserDTO update(Long id, UserDTO userDTO);

    boolean updatePassword(Long id, PasswordDTO passwordDTO);

    void subscribe(Long userId, Long categoryId);

    void cancelSubscription(Long id);

    void delete(Long id);

    boolean uniqueUsername(String username);
}
