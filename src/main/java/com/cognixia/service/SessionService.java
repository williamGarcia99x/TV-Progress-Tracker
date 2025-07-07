package com.cognixia.service;


import com.cognixia.dao.Session.SessionDaoImpl;
import com.cognixia.exception.ServerException;
import com.cognixia.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {

    public static record SessionData(String token, LocalDateTime expiresAt) {}

    private final SessionDaoImpl sessionDao;

    @Autowired
    public SessionService(SessionDaoImpl sessionDao) {
        this.sessionDao = sessionDao;
    }


    //create a session for a user
    //this will generate a token and set the expiration time. UserId is checked at higher levels
    public SessionData createSession(int userId) throws ServerException{

        // Using UUID for simplicity
        String token = UUID.randomUUID().toString();
        // Set the expiration time (e.g., 48 hours from now)
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(48);

        // Store the session (this is a placeholder, implement your own storage logic)
        // For example, you could store it in a database or an in-memory cache
        sessionDao.createSession(new Session(token, userId, expirationTime));

        return new SessionData(token, expirationTime);
    }


    public void deleteSession(String token) throws ServerException {
        // Delete the session based on the token
        sessionDao.deleteSession(token);
    }


    public Optional<Session> getSessionByToken(String token) throws ServerException {
        // Retrieve the session based on the token
        return sessionDao.getSessionByToken(token);
    }
}
