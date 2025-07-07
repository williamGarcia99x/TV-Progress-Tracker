package com.cognixia.dao.User;


import com.cognixia.exception.ServerException;
import com.cognixia.exception.UserRegistrationException;
import com.cognixia.model.User;
import com.cognixia.util.ConnectionFactory;
import org.springframework.stereotype.Repository;


import java.sql.*;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao{

    // This method's logic has been transferred to UserService since it does not include any code directly
    //interacting with the database.


    @Override
    public Optional<User> createUser(User user) throws UserRegistrationException {
        // Use try-with-resources to ensure the connection is closed automatically
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(
                     "INSERT INTO users (username, password_hash) VALUES (?, ?);",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Set the parameters for the prepared statement
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword_hash());

            // Execute the update and retrieve the generated keys
            pstmt.executeUpdate();
            try (ResultSet generatedPk = pstmt.getGeneratedKeys()) {
                if (generatedPk.next()) {
                    // Return the created user with the generated ID
                    return Optional.of(
                            new User(generatedPk.getInt(1), user.getUsername(), user.getPassword_hash(), user.getCreatedAt()));
                }
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            // Handle unique constraint violations (e.g., duplicate username)
            throw new UserRegistrationException("Username already exists: " + user.getUsername());
        } catch (SQLException e) {
            // Handle other SQL exceptions
            throw new ServerException("Error while creating user: " + e.getMessage());
        }

        // Return an empty Optional if no user was created
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserById(int id) {
        return Optional.empty();
    }


    @Override
    public Optional<User> getUserByUsername(String username) {

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?;")) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = mapRowToUser(rs);
                    return Optional.of(user);
                }
            }

        } catch (SQLException e) {
            // Log the exception or handle it as needed
            throw new ServerException(e.getMessage());
        }
        // If no user is found, return an empty Optional
        return Optional.empty();
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User(rs.getInt("user_id"), rs.getString("username"),
                rs.getString("password_hash"), rs.getDate("created_at").toLocalDate());

        return user;
    }
}
