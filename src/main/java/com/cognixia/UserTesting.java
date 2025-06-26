package com.cognixia;

import com.cognixia.dao.UserDaoImpl;
import com.cognixia.exception.UserAuthenticationException;
import com.cognixia.model.User;
import com.cognixia.service.UserService;
import com.cognixia.util.ConnectionFactory;

import java.util.Optional;

/**
 * Hello world!
 *
 */
public class UserTesting
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        UserService userService = new UserService(new UserDaoImpl());

        // Test connection to the database
        try {
            ConnectionFactory.getConnection();

            //Test user
            //Optional<User> user = userService.createUser("williamfx99", "password");
//          System.out.println(user.get().toString());

            //Test logging in
            try{
                Optional<User> userAuthenticationTest = userService.authenticateUser("williamfx99", "password");
                System.out.println(userAuthenticationTest.get().toString());
            } catch (UserAuthenticationException e) {
                System.err.println(e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Failed to establish connection: " + e.getMessage());
        }
        // Example usage of TvShow and User classes






    }

}
