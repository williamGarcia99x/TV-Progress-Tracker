package com.cognixia.util;

import lombok.Cleanup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionFactory {


    private static String url = "jdbc:mysql://localhost:3306/tv_progress_tracker";
    private static Connection connection = null;

    private ConnectionFactory(){}

    /**
     * Default username for connecting to mysql
     */
    private static String username = "root";
    /**
     * Default password for connecting to mysql
     */
    private static String password = "password";

    /**
     * @return an active connection to the database
     */
    public static Connection getConnection() {
        if(connection == null){
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return connection;
    }

}
