package com.cognixia.dao.UserTvTracker;

import com.cognixia.dto.TrackShowRequest;
import com.cognixia.exception.UserTvTrackerInsertionException;
import com.cognixia.model.UserTvTracker;

public interface UserTvTrackerDao {

    //User wants to track show
    public void trackShow(TrackShowRequest.UserTvTrackerDto trackerDto, TrackShowRequest.TvShowDto showDto) throws UserTvTrackerInsertionException;

}