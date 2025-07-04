package com.cognixia.dao;

import com.cognixia.model.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> authenticateUser(String username, String password);
    Optional<User> createUser(User user);
    Optional<User> getUserById(int id);
    Optional<User> getUserByUsername(String username);

}
