package com.cognixia.dao;

import com.cognixia.model.User;

import java.util.Optional;

public class UserDaoImpl implements UserDao{

    @Override
    public Optional<User> authenticateUser(String username, String password) {
        return Optional.empty();
    }

    @Override
    public Optional<User> createUser(String username, String password) {

        return Optional.empty();
    }

    @Override
    public Optional<User> getUserById(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return Optional.empty();
    }
}
