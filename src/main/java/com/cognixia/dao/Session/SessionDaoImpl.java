package com.cognixia.dao.Session;

import com.cognixia.exception.ServerException;
import com.cognixia.model.Session;
import com.cognixia.util.ConnectionFactory;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class SessionDaoImpl {

    public void createSession(Session session){

        String insertSession = "INSERT INTO sessions (token, user_id, expires_at) VALUES (?,?,?);";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(insertSession)){

            pstmt.setString(1, session.getToken());
            pstmt.setInt(2, session.getUserId());
            pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(session.getExpiresAt()));

            pstmt.executeUpdate();
        }catch (SQLException e){
            throw new ServerException("Error creating session: " + e.getMessage());
        }

    }

    public void deleteSession(String token){
        String deleteSession = "DELETE FROM sessions WHERE token = ?;";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(deleteSession)){

            pstmt.setString(1, token);
            pstmt.executeUpdate();
        }catch (SQLException e){
            throw new ServerException("Error deleting session: " + e.getMessage());
        }
    }

    public Optional<Session> getSessionByToken(String token) {

        //Look up a session by its token.
        String getSessionQuery = "SELECT * FROM sessions WHERE token = ?;";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(getSessionQuery)) {

            pstmt.setString(1, token);

            try(ResultSet rs = pstmt.executeQuery()) {
                // If a session is found, map it to a Session object and return it
                if (rs.next()) {
                    return Optional.of(mapRowToSession(rs));
                }
            }

        } catch (SQLException e) {
            throw new ServerException("Error retrieving session: " + e.getMessage());
        }

        // If no session is found. Check for expiration is down at higher levels.
        return Optional.empty();
    }

    public Session mapRowToSession(ResultSet rs) throws SQLException{
        // Map the ResultSet to a Session object
        String token = rs.getString("token");
        int userId = rs.getInt("user_id");
        LocalDateTime expirationTime = rs.getObject("expires_at", LocalDateTime.class);
        return new Session(token, userId, expirationTime);
    }

}




