package com.cognixia.controller;

import com.cognixia.model.User;
import com.cognixia.service.SessionService;
import com.cognixia.service.UserService;
import com.cognixia.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth") //base path for authentication-related endpoints
public class UserController {

    private record LoginRequest(String username, String password) {
    }
    private record AuthResponse(String token, int userId){}


    private final UserService userService;
    private final SessionService sessionService;

    @Autowired
    public UserController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req){

        //1. Look up user
        Optional<User> userOptional = userService.getUserByUsername(req.username);


        if(userOptional.isEmpty() || !PasswordUtil.checkPassword(req.password, userOptional.get().getPassword_hash())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        String token = sessionService.createSession(userOptional.get().getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(token, userOptional.get().getUserId()));
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // Log the exception (optional)
        e.printStackTrace();

        // Return a generic error response
        return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
    }


}
