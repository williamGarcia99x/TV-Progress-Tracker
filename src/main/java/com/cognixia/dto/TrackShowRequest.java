package com.cognixia.dto;

import com.cognixia.model.UserTvTracker;
import com.cognixia.model.WatchStatus;
import lombok.Data;

import java.time.LocalDate;
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
        private LocalDate dateStarted;
        private LocalDate dateCompleted;
    }

    //DTO used when storing show information on our database
    @Data
    public static class TvShowDto extends ShowSummaryDTO {
        private List<Integer> genreIds;    // REQUIRED for tv_show_genres
    }

    // This method converts the UserTvTrackerDto to a UserTvTracker model object
    public static UserTvTracker toUserTvTracker(UserTvTrackerDto trackerDto) {
        UserTvTracker userTvTracker = new UserTvTracker();
        userTvTracker.setUserId(trackerDto.getUserId());
        userTvTracker.setShowId(trackerDto.getShowId());
        userTvTracker.setStatus(trackerDto.getStatus());
        userTvTracker.setEpisodesWatched(trackerDto.getEpisodesWatched());
        userTvTracker.setCurrentSeason(trackerDto.getCurrentSeason());
        userTvTracker.setUserRating(trackerDto.getUserRating());
        userTvTracker.setNotes(trackerDto.getNotes());
        userTvTracker.setDateStarted(trackerDto.getDateStarted());
        userTvTracker.setDateCompleted(trackerDto.getDateCompleted());


        return userTvTracker;
    }

}
