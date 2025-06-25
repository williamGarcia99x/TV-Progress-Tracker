package com.cognixia.model;

import lombok.Getter;

import java.util.Date;

@Getter
public class TvShow {


// This class represents a TV Show entity in the application.

    /*
    * -- TV Shows table containing the master list of available shows
CREATE TABLE tv_shows (
    show_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    genre VARCHAR(100),
    total_episodes INT NOT NULL DEFAULT 1,
    total_seasons INT NOT NULL DEFAULT 1,
    release_year YEAR,
    description TEXT,
    status ENUM('ongoing', 'completed', 'cancelled') DEFAULT 'ongoing',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);*/

    private int showId;
    private String title;
    private String genre;
    private int totalEpisodes;
    private int totalSeasons;
    private int releaseYear;
    private String description;
    private TvShowStatus status; // ongoing, completed, cancelled
    private Date createdAt;



}
