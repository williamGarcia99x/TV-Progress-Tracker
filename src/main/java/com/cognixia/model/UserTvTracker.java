package com.cognixia.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


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
    private LocalDate dateAdded;
    private LocalDate dateStarted;
    private LocalDate dateCompleted;


}
