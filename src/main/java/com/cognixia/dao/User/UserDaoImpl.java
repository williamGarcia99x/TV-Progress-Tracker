package com.cognixia.dao.User;


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
    public Optional<User> authenticateUser(String username, String password) {
//        // Authenticate user. Compare the stored hashed password for the user with the given username against the provided
//        //password
//        Optional<User> userOptional = getUserByUsername(username);
//
//        //Username not found
//        if(userOptional.isEmpty()){
//            throw new UserAuthenticationException("Username not found: " + username);
//        }
//
//        //Password is incorrect, return user object
//        if(!PasswordUtil.checkPassword(password,userOptional.get().getPassword_hash())){
//            throw new UserAuthenticationException("Password is incorrect for username: " + username);
//        }
//
//        //Password is incorrect.
//        return userOptional;
        return Optional.empty();
    }

    @Override
    public Optional<User> createUser(User user) throws UserRegistrationException{

        Connection connection = null;
        try {
            connection = ConnectionFactory.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
            throw new UserRegistrationException("Username already exists: " + user.getUsername());

        } catch (SQLException e) {
            //Log the exception or handle it as needed
            //Consider  custom exception throwing here
            throw new RuntimeException(e);
        }


        // This should not execute under any circumstances, but if it does, return an empty Optional
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserById(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByUsername(String username) {

        Connection connection = null;
        try {
            connection = ConnectionFactory.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
