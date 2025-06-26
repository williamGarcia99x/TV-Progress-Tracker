package com.cognixia.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@AllArgsConstructor
@ToString
public class TvShow {



    private int showId;
    private String title;
    private String genre;
    private int totalEpisodes;
    private int totalSeasons;
    private Date releaseYear;
    private String description;
    private TvShowStatus status; // ongoing, completed, cancelled
    private Date createdAt;


}
