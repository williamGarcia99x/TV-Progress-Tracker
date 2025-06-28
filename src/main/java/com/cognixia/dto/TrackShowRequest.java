package com.cognixia.dto;

import java.util.Date;

import com.cognixia.model.WatchStatus;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class TrackShowRequest {

    private UserTvTrackerDto userTvTracker;
    private TvShowDto tvShow;

    @Data
    public static class UserTvTrackerDto {
        private Integer userId;
        private Integer showId;
        private WatchStatus status;            // "planning" | "watching" | "completed"
        private Integer episodesWatched;
        private Integer currentSeason;
        private Double  userRating;
        private String  notes;
        private java.sql.Date dateStarted;
        private java.sql.Date dateCompleted;
    }

    @Data
    public static class TvShowDto {
        private Integer showId;
        private String  originalName;
        private List<Integer> genreIds;    // REQUIRED for tv_show_genres
    }
}
