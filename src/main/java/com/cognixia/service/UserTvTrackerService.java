package com.cognixia.service;

import com.cognixia.dao.UserTvTracker.UserTvTrackerDao;
import com.cognixia.dto.TrackShowRequest;
import com.cognixia.exception.ServerException;
import com.cognixia.exception.UserTvTrackerException;
import com.cognixia.model.UserTvTracker;
import com.cognixia.model.WatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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



    public void trackShow(TrackShowRequest.UserTvTrackerDto tracker, TrackShowRequest.TvShowDto tvShow) throws UserTvTrackerException, ServerException {


        UserTvTracker userTvTracker = TrackShowRequest.toUserTvTracker(tracker);
        validateTracker(userTvTracker);

        //done validation
        //Track the show using the UserTvTrackerDao
        userTvTrackerDao.trackShow(tvShow, userTvTracker);

    }




    public void updateTracking(UserTvTracker tracker) throws UserTvTrackerException, ServerException{

        validateTracker(tracker);
        //done validation
        userTvTrackerDao.updateTracking(tracker);

    }



    public Optional<UserTvTracker> getTrackingByUserIdAndShowId(Integer userId, Integer showId) throws UserTvTrackerException, ServerException{

        if(userId == null || showId == null){
            throw new UserTvTrackerException("User ID and Show ID must not be null.");
        }

        return userTvTrackerDao.getTrackingByUserIdAndShowID(userId, showId);
    }

    public UserTvTracker getTrackingById(int trackerId) throws UserTvTrackerException, ServerException {
        //Check if the tracker exists
        Optional<UserTvTracker> tracker = userTvTrackerDao.getTrackerById(trackerId);

        if(tracker.isEmpty()){
            throw new UserTvTrackerException("Tracker with ID " + trackerId + " does not exist.");
        }

        //If it exists, return the tracker
        return tracker.get();
    }


    public void deleteTracking(int trackerId, int userId) throws UserTvTrackerException, ServerException {
        userTvTrackerDao.deleteTracking(trackerId);
    }

    //Validate tracker before tracking or updating
    public void validateTracker(UserTvTracker tracker) throws UserTvTrackerException {
        //If the watch status is WATCHING, then episodes watched must be greater than 0 and
        //current season must be greater than 0
        if (tracker.getStatus() == WatchStatus.WATCHING) {
            if(tracker.getEpisodesWatched() == null || tracker.getCurrentSeason() == null){
                throw new UserTvTrackerException("Episodes watched and seasons watched must both be greater than 0 when" +
                        "you are watching a show.");
            }
            if (tracker.getEpisodesWatched() < 0) {
                throw new UserTvTrackerException("Episodes watched must be greater than 0 when you are watching a show.");
            }
            if (tracker.getCurrentSeason() <= 0) {
                throw new UserTvTrackerException("Current season must be greater than 0 when you are watching a show.");
            }

        } else {
            //If the watch status is not WATCHING, then episodes watched and current season can be null
            tracker.setEpisodesWatched(null);
            tracker.setCurrentSeason(null);
        }

        if(tracker.getUserRating() != null){
            //if user is planning on watching the show, they cannot rate it.
            if(tracker.getStatus() == WatchStatus.PLANNING){
                throw new UserTvTrackerException("You cannot rate a show you're not watching or haven't watched.");
            }

            if(tracker.getUserRating() < 1 || tracker.getUserRating() > 10){
                throw new UserTvTrackerException("User rating must be between 1 and 10: " + tracker.getUserRating());
            }
        }


        // If the watch status is WATCHING or COMPLETED, then start date must be set
        if ((tracker.getStatus() == WatchStatus.WATCHING || tracker.getStatus() == WatchStatus.COMPLETED) && tracker.getDateStarted() == null) {
            throw new UserTvTrackerException("Date started must be set when you're watching the show or have completed it.");
        }

        //if the watch status is COMPLETED, then date completed must be set
        if (tracker.getStatus() == WatchStatus.COMPLETED && tracker.getDateCompleted() == null) {
            throw new UserTvTrackerException("Date completed must be set when you're watching the show or have completed it.");
        }

        //if both dateStarted and dateCompleted are not null, dateStarted must be before dateCompleted
        if (tracker.getDateStarted() != null && tracker.getDateCompleted() != null &&
                tracker.getDateStarted().after(tracker.getDateCompleted())) {
            throw new UserTvTrackerException("Date started cannot be after date completed.");
        }

        //If dateStarted is not null, then status must be WATCHING or COMPLETED
        if (tracker.getDateStarted() != null && tracker.getStatus() == WatchStatus.PLANNING) {
            throw new UserTvTrackerException("Date started cannot be set unless you're watching the show or have completed it.");
        }

        //If dateCompleted is not null, then status must be COMPLETED
        if (tracker.getDateCompleted() != null && tracker.getStatus() != WatchStatus.COMPLETED) {
            throw new UserTvTrackerException("Date completed cannot be set unless you completed the show.");
        }

    }

    public List<UserTvTracker> getTrackersByUserId(Integer userIdOnRequest) throws ServerException {
        return userTvTrackerDao.getTrackersByUserId(userIdOnRequest);
    }
}
