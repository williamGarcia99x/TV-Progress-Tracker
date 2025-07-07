package com.cognixia.dao.UserTvTracker;

import com.cognixia.dto.TrackShowRequest;
import com.cognixia.exception.ServerException;
import com.cognixia.exception.UserTvTrackerException;
import com.cognixia.model.TvShow;
import com.cognixia.model.UserTvTracker;
import com.cognixia.model.WatchStatus;

import java.util.List;
import java.util.Optional;

public interface UserTvTrackerDao {

    //User wants to track show
    public void trackShow(TrackShowRequest.TvShowDto showDto, UserTvTracker tvTracker) throws UserTvTrackerException;

    //Users to update their tracking information for a show
    public void updateTracking(UserTvTracker tracker) throws UserTvTrackerException, ServerException;

    // User wants to delete their tracking information for a show
    public void deleteTracking(int trackerId) throws UserTvTrackerException, ServerException;

    public Optional<UserTvTracker> getTrackerById(int trackerId) throws ServerException;

    public List<UserTvTracker> getTrackersByUserId(int userId) throws ServerException;

    public Optional<UserTvTracker> getTrackingByUserIdAndShowID(int userId, int showId) throws ServerException;
    public List<TvShow> getTrackedShowsByUserId(int userId, WatchStatus status) throws ServerException;
}