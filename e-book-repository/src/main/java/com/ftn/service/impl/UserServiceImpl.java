package com.ftn.service.impl;

import com.ftn.exception.NotFoundException;
import com.ftn.model.User;
import com.ftn.model.dto.UserDTO;
import com.ftn.repository.UserDao;
import com.ftn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jasmina on 16/06/2018.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserDao userDao;


    @Override
    public UserDTO readSelf() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final User user = userDao.findByUsername(authentication.getName());
        return new UserDTO(user);
    }

    @Override
    public List<UserDTO> readAll() {
        return userDao.findAll().stream().map(user -> new UserDTO(user)).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> readSubscribers() {
        return userDao.findByType(User.Type.SUBSCRIBER).stream().map(user -> new UserDTO(user)).collect(Collectors.toList());
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        User user = new User();
        user.merge(userDTO);
        // TODO: Uncomment this when development ends
        //user.setPassword(encoder.encode(user.getPassword()));
        user.setEnabled(true);
        user = userDao.save(user);
        return new UserDTO(user);
    }

    @Override
    public UserDTO update(Long id, UserDTO userDTO) {
        User user = userDao.findById(id).orElseThrow(NotFoundException::new);
        user.merge(userDTO);
        user = userDao.save(user);
        return new UserDTO(user);
    }

    @Override
    public void delete(Long id) {
        final User user = userDao.findById(id).orElseThrow(NotFoundException::new);
        userDao.delete(user);
    }
}
