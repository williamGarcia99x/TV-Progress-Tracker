package com.cognixia;

import com.cognixia.model.TvShow;
import com.cognixia.model.User;
import com.cognixia.util.ConnectionFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        // Test connection to the database
        try {
            ConnectionFactory.getConnection();
            System.out.println("Connection established successfully!");
        } catch (Exception e) {
            System.err.println("Failed to establish connection: " + e.getMessage());
        }



    }

}
