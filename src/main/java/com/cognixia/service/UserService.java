package com.cognixia.service;

import com.cognixia.dao.User.UserDao;
import com.cognixia.exception.UserAuthenticationException;
import com.cognixia.exception.UserRegistrationException;
import com.cognixia.model.User;
import com.cognixia.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao){
        this.userDao = userDao;
    }


    public User createUser(String username, String password) throws UserRegistrationException{
        // Logic to create a user using UserDao
        // This would typically involve hashing the password and saving the user to the database.

        if(username.isEmpty() || password.isEmpty()){
            //consider throwing custom user exception here
            throw new UserRegistrationException("Username and password cannot be empty.");
        }

        String hashedPassword = PasswordUtil.hashPassword(password);
        // Test fake ID value

        Optional<User> createdUser = userDao.createUser(new User(-1, username, hashedPassword, new Date()));

        if(createdUser.isEmpty()){
            // If the user creation fails, throw an exception
            throw new UserRegistrationException("Failed to create user with username: " + username);
        }

        return createdUser.get();
    }

    //create a getUserByUsername method
    public Optional<User> getUserByUsername(String username){
        return userDao.getUserByUsername(username);
    }











}
