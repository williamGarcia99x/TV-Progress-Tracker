package com.cognixia.controller;


import com.cognixia.dto.TrackShowRequest;
import com.cognixia.service.UserTvTrackerService;
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


    //Todo add authentication and authorization to this endpoint
    //Handle exception handling
    @PostMapping("/tracker")
    public ResponseEntity<Void> trackShow(@RequestBody TrackShowRequest request) {

        //Call the trackShow method from UserTvTrackerService
        userTvTrackerService.trackShow(request.getUserTvTracker(), request.getTvShow());




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
