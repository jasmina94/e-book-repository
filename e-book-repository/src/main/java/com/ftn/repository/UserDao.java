package com.ftn.repository;

import com.ftn.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jasmina on 16/06/2018.
 */
@Repository
public interface UserDao extends JpaRepository<User, Long> {

    User findByUsername(String username);

    List<User> findByType(User.Type type);
}
