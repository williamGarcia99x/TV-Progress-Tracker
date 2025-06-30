package com.cognixia.controller;

import com.cognixia.exception.UserRegistrationException;
import com.cognixia.model.User;
import com.cognixia.service.SessionService;
import com.cognixia.service.UserService;
import com.cognixia.util.PasswordUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth") //base path for authentication-related endpoints
public class UserController {

    private record AuthRequest(String username, String password) {}
    private record AuthResponse(String token, int userId){}


    private final UserService userService;
    private final SessionService sessionService;

    @Autowired
    public UserController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req){

        //1. Look up user
        Optional<User> userOptional = userService.getUserByUsername(req.username);


        if(userOptional.isEmpty() || !PasswordUtil.checkPassword(req.password, userOptional.get().getPassword_hash())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        String token = sessionService.createSession(userOptional.get().getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(token, userOptional.get().getUserId()));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/register")
    @ResponseStatus(HttpStatus.CREATED) //HTTP status code for successful creation
    public ResponseEntity<?> register(@RequestBody AuthRequest userRequest) {
        //Service layers are responsible for checking if user already exists
        User createdUser = userService.createUser(userRequest.username, userRequest.password);

        //If the registration is unsuccessful, an exception is thrown and handled by the exception handler

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser.getUserId());
    }


    //This is a protected method. The session can only be deleted if it's valid, which is what the
    // TokenAuthInterceptor checks for.
    @RequestMapping(method = RequestMethod.DELETE, path = "/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT) //HTTP status code for successful deletion
    public ResponseEntity<?> logout(HttpServletRequest request){
        //At this point, the session is valid and can be safely deleted
        String authHeader = request.getHeader("Authorization");

        String token = authHeader.substring(7); //Obtain the token after the "Bearer "
        sessionService.deleteSession(token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // No content response
    }

    @ExceptionHandler(UserRegistrationException.class)
    public ResponseEntity<String> handleUserRegistrationException(UserRegistrationException e) {
        // Return a 400 Bad Request response with the error message
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // Log the exception (optional)
        e.printStackTrace();

        // Return a generic error response
        return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
    }


}
