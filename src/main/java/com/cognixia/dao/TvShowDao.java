package com.cognixia.dao;

import com.cognixia.model.TvShow;

import java.util.List;
import java.util.Optional;

public interface TvShowDao {


    public Optional<TvShow> getTvShowById(int showId);

    /**
     * Filters TV shows based on the provided criteria.
     *
     * @param title The title of the TV show (can be partial).
     * @param genre The genre of the TV show.
     * @param status The status of the TV show (e.g., "ongoing", "completed").
     * @param startYear The start year of the release date range.
     * @param endYear The end year of the release date range.
     * @return A list of TV shows that match the given criteria.
     */
    List<TvShow> filterTvShows(String title, String genre, String status, Integer startYear, Integer endYear);
}
