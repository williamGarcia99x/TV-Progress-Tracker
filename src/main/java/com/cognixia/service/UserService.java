package com.cognixia.service;

import com.cognixia.dao.UserDao;
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

    public Optional<User> createUser(String username, String password){
        // Logic to create a user using UserDao
        // This would typically involve hashing the password and saving the user to the database.

        if(username.isEmpty() || password.isEmpty()){
            //consider throwing custom user exception here
            return Optional.empty();
        }

        String hashedPassword = PasswordUtil.hashPassword(password);
        return userDao.createUser(new User(-1, username, hashedPassword, new Date()));
    }


}
