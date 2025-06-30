package com.cognixia.util;

import com.cognixia.dao.Session.SessionDaoImpl;
import com.cognixia.model.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class TokenAuthInterceptor implements HandlerInterceptor {

    private SessionDaoImpl sessionDao;

    @Autowired
    public TokenAuthInterceptor(SessionDaoImpl sessionDao){
        this.sessionDao = sessionDao;
    }


    /**
     * Pre-handle method for intercepting requests to verify session tokens.
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
         String authHeader = request.getHeader("Authorization");




         if(authHeader == null || !authHeader.startsWith("Bearer ")) {
             response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
             return false; // Unauthorized access
         }

         //Valid authorization header. Proceed with verifying session

         String token = authHeader.substring(7); //Obtain the token that's after the "Bearer

         //Look up the session based on the token
         Optional<Session> sessionOptional = sessionDao.getSessionByToken(token);

         //Token is invalid.
         if(sessionOptional.isEmpty()){
             response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid session token");
             return false; // Unauthorized access
         }
         Session session = sessionOptional.get();


         if(!LocalDateTime.now().isBefore(session.getExpiresAt())){
             response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session token has expired");
             return false; // Unauthorized access
         }

         //Token is valid. Set the user ID in the request attribute for further processing
         request.setAttribute("AUTH_USER_ID", session.getUserId());
         return true;
     }
}
