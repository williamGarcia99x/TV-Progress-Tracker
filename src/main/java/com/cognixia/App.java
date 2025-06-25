package com.cognixia;

import com.cognixia.dao.UserDaoImpl;
import com.cognixia.model.TvShow;
import com.cognixia.model.User;
import com.cognixia.service.UserService;
import com.cognixia.util.ConnectionFactory;

import java.sql.Connection;
import java.util.Optional;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        UserService userService = new UserService(new UserDaoImpl());

        // Test connection to the database
        try {
            ConnectionFactory.getConnection();
            Optional<User> user = userService.createUser("williamfx99", "password");
            System.out.println(user.get().toString());





        } catch (Exception e) {
            System.err.println("Failed to establish connection: " + e.getMessage());
        }
        // Example usage of TvShow and User classes






    }

}
