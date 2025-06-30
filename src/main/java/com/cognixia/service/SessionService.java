package com.cognixia.service;


import com.cognixia.dao.Session.SessionDaoImpl;
import com.cognixia.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SessionService {


    private final SessionDaoImpl sessionDao;

    @Autowired
    public SessionService(SessionDaoImpl sessionDao) {
        this.sessionDao = sessionDao;
    }


    //create a session for a user
    //this will generate a token and set the expiration time. UserId is checked at higher levels
    public String createSession(int userId) {

        // Using UUID for simplicity
        String token = UUID.randomUUID().toString();
        // Set the expiration time (e.g., 1 hour from now)
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(1);

        // Store the session (this is a placeholder, implement your own storage logic)
        // For example, you could store it in a database or an in-memory cache
        sessionDao.createSession(new Session(token, userId, expirationTime));

        return token; // Return the generated token
    }
}
