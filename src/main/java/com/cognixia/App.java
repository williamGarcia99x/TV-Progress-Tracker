package com.cognixia;

import com.cognixia.model.UserTvTracker;
import com.cognixia.model.WatchStatus;
import com.cognixia.service.UserTvTrackerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        var context = SpringApplication.run(App.class, args);
        UserTvTrackerService service = context.getBean(UserTvTrackerService.class);

        UserTvTracker tracker = new UserTvTracker();
        // Example of tracking a show, breaking bad
//        tracker.setUserId(1); // Assuming user ID 1 exists
//        tracker.setShowId(1396); // Breaking Bad
//        tracker.setStatus(WatchStatus.WATCHING);
//        tracker.setEpisodesWatched(5);
//        tracker.setCurrentSeason(1);
//        tracker.setUserRating(null); // No rating yet
//        tracker.setNotes("Great show so far!");
//        tracker.setDateStarted(java.sql.Date.valueOf(LocalDate.now())); // Example start date
//        tracker.setDateCompleted(null); // Not completed yet
//
//        // Act
//        service.trackShow(tracker);
//        System.out.println("Show tracked successfully!");

        //Test tracking a show with invalid data
        tracker.setUserId(1); // Assuming user ID 1 exists
        tracker.setShowId(1398); // Friends
        tracker.setStatus(WatchStatus.WATCHING);
        tracker.setEpisodesWatched(0); // valid, should be greater than or equal to 1
        tracker.setCurrentSeason(1); // Valid
        tracker.setUserRating(null); // No rating yet
        tracker.setNotes("Great show so far!");
        tracker.setDateStarted(java.sql.Date.valueOf(LocalDate.now())); // Example start date
        tracker.setDateCompleted(java.sql.Date.valueOf(LocalDate.now())); // Not completed yet
        try {
            service.trackShow(tracker);
            System.out.println("Show tracked successfully!");
        } catch (Exception e) {
            System.out.println("Failed to track show: " + e.getMessage());
        }

        //Tested: Date started is non-null and status is PLANNING, should throw exception
        //Tested: Status is WATCHING or COMPLETED, date started is null, should throw exception
        //Tested: Status is WATCHING, episodes watched is less than 1, should throw exception
        //Tested: Date Completed is non-null and status is either PLANNING or WATCHING, should throw exception

        //trackShow method has been tested for functionality, although further testing is recommended. For now,
        // the method is functional and can be used in the application.




    }




}
