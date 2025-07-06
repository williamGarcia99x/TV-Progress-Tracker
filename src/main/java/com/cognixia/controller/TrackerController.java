package com.cognixia.controller;


import com.cognixia.dto.ShowSummaryDTO;
import com.cognixia.dto.TrackShowRequest;
import com.cognixia.exception.ServerException;
import com.cognixia.exception.UserTvTrackerException;
import com.cognixia.model.TvShow;
import com.cognixia.model.User;
import com.cognixia.model.UserTvTracker;
import com.cognixia.model.WatchStatus;
import com.cognixia.service.UserTvTrackerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tracker") // Base URL for the controller
public class TrackerController {


    private UserTvTrackerService userTvTrackerService;

    @Autowired
    public TrackerController(UserTvTrackerService userTvTrackerService){
        this.userTvTrackerService = userTvTrackerService;
    }


//    @GetMapping("/")
//    @ResponseStatus(HttpStatus.OK) //HTTP status code for successful retrieval
//    public ResponseEntity<List<UserTvTracker>> getTrackers(HttpServletRequest request) {
//
//        Integer userIdOnRequest = (Integer) request.getAttribute("AUTH_USER_ID");
//
//        // Call the getTrackers method from UserTvTrackerService
//        List<UserTvTracker> trackers = userTvTrackerService.getTrackersByUserId(userIdOnRequest);
//
//        //regardless of user tracking shows or not, return an empty list or a filled list
//        return ResponseEntity.ok(trackers); // Return the list of trackers
//    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK) //HTTP status code for successful retrieval
    public ResponseEntity<List<ShowSummaryDTO>> getTrackedShows(HttpServletRequest request, @RequestParam String status) {

        Integer userIdOnRequest = (Integer) request.getAttribute("AUTH_USER_ID");

        if(!WatchStatus.isValidStatus(status)){
           throw new UserTvTrackerException("Invalid status");
        }


        // Call the getTrackers method from UserTvTrackerService
        List<ShowSummaryDTO> trackers = userTvTrackerService.getTrackedShows(userIdOnRequest, WatchStatus.valueOf(status));

        //regardless of user tracking shows or not, return an empty list or a filled list
        return ResponseEntity.ok(trackers); // Return the list of tracked shows
    }





    @GetMapping(params = {"user-id", "show-id"}) // Endpoint to get a specific tracker by ID
    public ResponseEntity<?> getTrackerByUserAndShow(HttpServletRequest request, @RequestParam("user-id") Integer userId, @RequestParam("show-id") Integer showId){

        Integer userIdOnRequest = (Integer) request.getAttribute("AUTH_USER_ID");

        if(userIdOnRequest != userId){
            //If the user ID in the request does not match the authenticated user ID, return a 403 Forbidden response
            return ResponseEntity.status(403).build(); // Forbidden
        }

        // Call the getTrackingByUserIdAndShowId method from UserTvTrackerService
        Optional<UserTvTracker> userTvTrackerOptional = userTvTrackerService.getTrackingByUserIdAndShowId(userId, showId);

       return ResponseEntity.ok(userTvTrackerOptional.isPresent() ? userTvTrackerOptional.get() : Collections.emptyMap());
    }



    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED) //HTTP status code for successful creation
    public ResponseEntity<Void> trackShow(HttpServletRequest request, @RequestBody TrackShowRequest trackShowRequest) {

        Integer userIdOnRequest = (Integer) request.getAttribute("AUTH_USER_ID");

        if(userIdOnRequest != trackShowRequest.getUserTvTracker().getUserId()){
            //If the user ID in the TrackShowRequest does not match the authenticated user ID, return a 403 Forbidden response
            return ResponseEntity.status(403).build(); // Forbidden
        }

        //Call the trackShow method from UserTvTrackerService
        userTvTrackerService.trackShow(trackShowRequest.getUserTvTracker(), trackShowRequest.getTvShow());
        // Return a 201 Created response with no content
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PutMapping("/{tracker-id}") // Endpoint to update tracking information
    @ResponseStatus(HttpStatus.OK) //HTTP status code for successful update
    public ResponseEntity<Void> updateTracking(HttpServletRequest request, @RequestBody UserTvTracker userTvTracker) {

        Integer userIdOnRequest = (Integer) request.getAttribute("AUTH_USER_ID");

        if(userIdOnRequest != userTvTracker.getUserId()){
            //If the user ID in the UserTvTrackerDto does not match the authenticated user ID, return a 403 Forbidden response
            return ResponseEntity.status(403).build(); // Forbidden
        }

        //Convert UserTvTrackerDto to UserTvTracker model
        userTvTrackerService.updateTracking(userTvTracker);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{tracker-id}") // Endpoint to delete tracking information
    @ResponseStatus(HttpStatus.NO_CONTENT) //HTTP status code for successful deletion
    public ResponseEntity<Void> deleteTracking(HttpServletRequest request, @PathVariable("tracker-id") int trackerId) {

        Integer userIdOnRequest = (Integer) request.getAttribute("AUTH_USER_ID");

        UserTvTracker userTvTracker = userTvTrackerService.getTrackingById(trackerId);
        if(userTvTracker.getUserId() != userIdOnRequest){
            //If the user ID in the UserTvTracker does not match the authenticated user ID, return a 403 Forbidden response
            return ResponseEntity.status(403).build(); // Forbidden
        }

        // Call the delete method from UserTvTrackerService
        userTvTrackerService.deleteTracking(trackerId, userIdOnRequest);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(UserTvTrackerException.class)
    public ResponseEntity<?> handleUserTvTrackerException(UserTvTrackerException e) {
        // Return a 400 Bad Request response with the error message
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<String> handleException(ServerException e) {
        // Log the exception (optional)
        e.printStackTrace();
        // Return a generic error response
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }

}
