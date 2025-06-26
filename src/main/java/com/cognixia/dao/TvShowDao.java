package com.cognixia.dao;

import com.cognixia.model.TvShow;

import java.util.List;
import java.util.Optional;

public interface TvShowDao {
    Optional<TvShow> getTvShowById(int id);
    List<TvShow> getTvShowsByTitle(String title);
    List<TvShow> getTvShowsByGenre(String genre);
    List<TvShow> getTvShowsByStatus(String status);
    List<TvShow> getTvShowsByReleaseYearRange(int startYear, int endYear);
    List<TvShow> getAllTvShows();
    boolean insertTvShow(TvShow tvShow);


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
