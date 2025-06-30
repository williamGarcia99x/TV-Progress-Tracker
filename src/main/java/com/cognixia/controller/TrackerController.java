package com.cognixia.controller;


import com.cognixia.dto.TrackShowRequest;
import com.cognixia.service.UserTvTrackerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrackerController {


    private UserTvTrackerService userTvTrackerService;

    @Autowired
    public TrackerController(UserTvTrackerService userTvTrackerService){
        this.userTvTrackerService = userTvTrackerService;
    }




    @PostMapping("/tracker")
    public ResponseEntity<Void> trackShow(HttpServletRequest request, @RequestBody TrackShowRequest trackShowRequest) {

        Integer userIdOnRequest = (Integer) request.getAttribute("AUTH_USER_ID");

        if(userIdOnRequest != trackShowRequest.getUserTvTracker().getUserId()){
            //If the user ID in the TrackShowRequest does not match the authenticated user ID, return a 403 Forbidden response
            return ResponseEntity.status(403).build(); // Forbidden
        }

        //Call the trackShow method from UserTvTrackerService
        userTvTrackerService.trackShow(trackShowRequest.getUserTvTracker(), trackShowRequest.getTvShow());
        return ResponseEntity.ok().build();

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // Log the exception (optional)
        e.printStackTrace();

        // Return a generic error response
        return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
    }

}
