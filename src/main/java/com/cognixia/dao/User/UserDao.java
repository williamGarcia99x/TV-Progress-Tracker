package com.cognixia.dao.User;

import com.cognixia.model.User;

import java.util.Optional;

public interface UserDao {


    Optional<User> createUser(User user);
    Optional<User> getUserById(int id);
    Optional<User> getUserByUsername(String username);

}
