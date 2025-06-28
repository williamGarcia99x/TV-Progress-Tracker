package com.cognixia.service;

import com.cognixia.dao.UserTvTrackerDao;
import com.cognixia.dto.TrackShowRequest;
import com.cognixia.exception.UserTvTrackerInsertionException;
import com.cognixia.model.TvShow;
import com.cognixia.model.UserTvTracker;
import com.cognixia.model.WatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//The methods contain herein are protected, meaning that they will be invoked only after the
//user has been authenticated and authorized to perform the action.

@Service
public class UserTvTrackerService {

    private UserTvTrackerDao userTvTrackerDao;
    private TvShowService tvShowService;

    @Autowired
    public UserTvTrackerService(UserTvTrackerDao userTvTrackerDao, TvShowService tvShowService) {
        this.userTvTrackerDao = userTvTrackerDao;
        this.tvShowService = tvShowService;
    }

    // Example method to get all tracked TV shows for a user
    // public List<TvShow> getTrackedTvShows(int userId) {
    //     // Logic to retrieve all tracked TV shows for a user using UserTvTrackerDao
    // }

    //TODO handle exception handling
    public void trackShow(TrackShowRequest.UserTvTrackerDto tracker, TrackShowRequest.TvShowDto tvShow) throws UserTvTrackerInsertionException {


        //If the watch status is WATCHING, then episodes watched must be greater than 0 and
        //current season must be greater than 0
        if (tracker.getStatus() == WatchStatus.WATCHING) {
            if (tracker.getEpisodesWatched() < 0) {
                throw new UserTvTrackerInsertionException("Episodes watched must be greater than 0 when status is WATCHING.");
            }
            if (tracker.getCurrentSeason() <= 0) {
                throw new UserTvTrackerInsertionException("Current season must be greater than 0 when status is WATCHING.");
            }

        } else {
            //If the watch status is not WATCHING, then episodes watched and current season can be null
            tracker.setEpisodesWatched(null);
            tracker.setCurrentSeason(null);
        }
        //User rating must be between 1 and 10, inclusive
        if (tracker.getUserRating() != null && (tracker.getUserRating() < 1 || tracker.getUserRating() > 10)) {
            throw new UserTvTrackerInsertionException("User rating must be between 1 and 10: " + tracker.getUserRating());
        }

        // If the watch status is WATCHING or COMPLETED, then start date must be set
        if ((tracker.getStatus() == WatchStatus.WATCHING || tracker.getStatus() == WatchStatus.COMPLETED) && tracker.getDateStarted() == null) {
            throw new UserTvTrackerInsertionException("Date started must be set when status is WATCHING or COMPLETED.");
        }

        //if both dateStarted and dateCompleted are not null, dateStarted must be before dateCompleted
        if (tracker.getDateStarted() != null && tracker.getDateCompleted() != null &&
                tracker.getDateStarted().after(tracker.getDateCompleted())) {
            throw new UserTvTrackerInsertionException("Date started cannot be after date completed.");
        }

        //If dateStarted is not null, then status must be WATCHING or COMPLETED
        if (tracker.getDateStarted() != null && tracker.getStatus() == WatchStatus.PLANNING) {
            throw new UserTvTrackerInsertionException("Date started cannot be set unless status is WATCHING or COMPLETED.");
        }

        //If dateCompleted is not null, then status must be COMPLETED
        if (tracker.getDateCompleted() != null && tracker.getStatus() != WatchStatus.COMPLETED) {
            throw new UserTvTrackerInsertionException("Date completed cannot be set unless status is COMPLETED.");
        }
        //done validation
        //Track the show using the UserTvTrackerDao
        userTvTrackerDao.trackShow(tracker, tvShow);

    }

//    public void trackShow(UserTvTracker tracker) {
//        // Validate the tracker object
//        //User is validated by the controller
//
//        //Show must exist
//        tvShowService.findTvShowById(tracker.getShowId())
//                .orElseThrow(() -> new UserTvTrackerInsertionException("TV Show with ID " + tracker.getShowId() + " does not exist."));
//
//        //If the watch status is WATCHING, then episodes watched must be greater than 0 and
//        //current season must be greater than 0
//        if (tracker.getStatus() == WatchStatus.WATCHING) {
//            if (tracker.getEpisodesWatched() < 0) {
//                throw new UserTvTrackerInsertionException("Episodes watched must be greater than 0 when status is WATCHING.");
//            }
//            if (tracker.getCurrentSeason() <= 0) {
//                throw new UserTvTrackerInsertionException("Current season must be greater than 0 when status is WATCHING.");
//            }
//
//        } else {
//            //If the watch status is not WATCHING, then episodes watched and current season can be null
//            tracker.setEpisodesWatched(null);
//            tracker.setCurrentSeason(null);
//        }
//
//
//
//        //User rating must be between 1 and 10, inclusive
//        if (tracker.getUserRating() != null && (tracker.getUserRating() < 1 || tracker.getUserRating() > 10)) {
//            throw new UserTvTrackerInsertionException("User rating must be between 1 and 10: " + tracker.getUserRating());
//        }
//
//        // If the watch status is WATCHING or COMPLETED, then start date must be set
//        if ((tracker.getStatus() == WatchStatus.WATCHING || tracker.getStatus() == WatchStatus.COMPLETED) && tracker.getDateStarted() == null) {
//            throw new UserTvTrackerInsertionException("Date started must be set when status is WATCHING or COMPLETED.");
//        }
//
//        //if both dateStarted and dateCompleted are not null, dateStarted must be before dateCompleted
//        if (tracker.getDateStarted() != null && tracker.getDateCompleted() != null &&
//                tracker.getDateStarted().after(tracker.getDateCompleted())) {
//            throw new UserTvTrackerInsertionException("Date started cannot be after date completed.");
//        }
//
//        //If dateStarted is not null, then status must be STARTED or COMPLETED
//        if (tracker.getDateStarted() != null && tracker.getStatus() == WatchStatus.PLANNING) {
//            throw new UserTvTrackerInsertionException("Date started cannot be set unless status is WATCHING or COMPLETED.");
//        }
//
//        //If dateCompleted is not null, then status must be COMPLETED
//        if (tracker.getDateCompleted() != null && tracker.getStatus() != WatchStatus.COMPLETED) {
//            throw new UserTvTrackerInsertionException("Date completed cannot be set unless status is COMPLETED.");
//        }
//        //done validation
//        //Track the show using the UserTvTrackerDao
//        userTvTrackerDao.trackShow(tracker);
//    }


}
