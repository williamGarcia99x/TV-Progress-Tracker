package com.cognixia.service;

import com.cognixia.dao.UserDao;
import com.cognixia.exception.UserAuthenticationException;
import com.cognixia.exception.UserRegistrationException;
import com.cognixia.model.User;
import com.cognixia.util.PasswordUtil;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.Optional;

@AllArgsConstructor
public class UserService {

    private UserDao userDao;



    // This class will contain methods to interact with UserDao
    // and perform business logic related to users.

    // Example method to authenticate a user
    // public Optional<User> authenticateUser(String username, String password) {
    //     // Logic to authenticate user using UserDao
    // }

    public Optional<User> createUser(String username, String password) throws UserRegistrationException{
        // Logic to create a user using UserDao
        // This would typically involve hashing the password and saving the user to the database.

        if(username.isEmpty() || password.isEmpty()){
            //consider throwing custom user exception here
            throw new UserRegistrationException("Username and password cannot be empty.");
        }

        String hashedPassword = PasswordUtil.hashPassword(password);
        // Dummy ID value
        return userDao.createUser(new User(-1, username, hashedPassword, new Date()));
    }
    
    // method for authenticating user
    public Optional<User> authenticateUser(String username, String password) throws UserAuthenticationException {
        // Authenticate user. Compare the stored hashed password for the user with the given username against the provided
        //password
        Optional<User> userOptional = userDao.getUserByUsername(username);

        //Username not found
        if(userOptional.isEmpty()){
            throw new UserAuthenticationException("Username not found: " + username);
        }

        //Password is incorrect, return user object
        if(!PasswordUtil.checkPassword(password,userOptional.get().getPassword_hash())){
            throw new UserAuthenticationException("Password is incorrect for username: " + username);
        }

        //Password is incorrect.
        return userOptional;
    }











}
