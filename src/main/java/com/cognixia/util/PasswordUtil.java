package com.cognixia.util;

import org.mindrot.jbcrypt.BCrypt;

// This class will contain utility methods for password hashing and validation.
public class PasswordUtil {

    //Private constructor to prevent instantiation
    private PasswordUtil(){}


    //hash a password using BCrypt
     public static String hashPassword(String password) {
         return BCrypt.hashpw(password, BCrypt.gensalt());
     }

    //check if a password matches the hashed password
     public static boolean checkPassword(String password, String hashed) {
         return BCrypt.checkpw(password, hashed);
     }
}
