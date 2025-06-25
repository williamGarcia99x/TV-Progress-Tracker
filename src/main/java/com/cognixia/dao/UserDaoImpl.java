package com.cognixia.dao;

import com.cognixia.model.User;
import com.cognixia.util.ConnectionFactory;


import java.sql.*;
import java.util.Optional;

public class UserDaoImpl implements UserDao{

    @Override
    public Optional<User> authenticateUser(User user) {
        return Optional.empty();
    }

    @Override
    public Optional<User> createUser(User user) {

        // In a real implementation, you would save the newUser to the database and return it.
        // For now, we will just return an empty Optional.

        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO users (username, password_hash) VALUES (?,?);",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword_hash());

            pstmt.executeUpdate();

            ResultSet generatedPk = pstmt.getGeneratedKeys();
            if(generatedPk.next()){
                return Optional.of(new User(generatedPk.getInt(1), user.getUsername(),user.getPassword_hash(), user.getCreatedAt()));
            }

        } catch (SQLIntegrityConstraintViolationException e){
            // This exception is thrown when there is a unique constraint violation, such as duplicate username
            // Log the exception or handle it as needed
            System.err.println("Username already exists: " + user.getUsername());
            // Consider throwing a custom exception here
            return Optional.empty();
        } catch (SQLException e) {
            // Log the exception or handle it as needed
            //Consider  custom exception throwing here
            throw new RuntimeException(e);
        }


        // If the user creation fails or no user is created, return an empty Optional
        // When would this execute?
        // This would execute if the insert fails or no rows are affected
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserById(int id) {


        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByUsername(String username) {

        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?;");
            pstmt.setString(1, username);

            ResultSet rs  = pstmt.executeQuery();
            if(rs.next()){
                User user = mapRowToUser(rs);
                return Optional.of(user);
            }

        } catch (SQLException e) {
            // Log the exception or handle it as needed
            throw new RuntimeException(e);
        }
        // If no user is found, return an empty Optional
        return Optional.empty();
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User(rs.getInt("user_id"), rs.getString("username"),
                rs.getString("password_hash"), rs.getDate("created_at"));

        return user;
    }
}
