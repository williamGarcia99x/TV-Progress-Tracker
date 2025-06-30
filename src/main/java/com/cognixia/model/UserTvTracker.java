package com.cognixia.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTvTracker {

    private int trackerId;
    private int userId;
    private int showId;
    private WatchStatus status;
    private Integer episodesWatched;
    private Integer currentSeason;
    private Double userRating;
    private String notes;
    private Date dateAdded;
    private Date dateStarted;
    private Date dateCompleted;


}
