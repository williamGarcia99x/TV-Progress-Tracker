package com.cognixia.service;

import com.cognixia.dao.TvShowDao;
import com.cognixia.model.TvShow;
import com.cognixia.model.TvShowStatus;

import java.time.Year;
import java.util.Date;
import java.util.List;

public class TvShowService {

    private TvShowDao tvShowDao;
    public TvShowService(TvShowDao tvShowDao) {
        this.tvShowDao = tvShowDao;
    }

    // This class will contain methods to interact with TvShowDao
    // and perform business logic related to TV shows.

    // Example method to get a list of TV shows
    // public List<TvShow> getAllTvShows() {
    //     // Logic to retrieve all TV shows using TvShowDao
    // }

    // Example method to get a TV show by ID
    // public Optional<TvShow> getTvShowById(int id) {
    //     // Logic to retrieve a TV show by ID using TvShowDao
    // }

    public List<TvShow> filterTvShows(String title, String genre, String status, Integer startYear, Integer endYear) {

        //Validate input data
        //Title, genre, status cannot be null, but they can be empty strings
        if (title == null || genre == null || status == null) {
            throw new IllegalArgumentException("Title, genre, and status cannot be null.");
        }
        //If status is not empty, we must make sure that it matches one of the valid TvShowStatus values
        if (!status.isEmpty()) {
            try{
                TvShowStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status provided. Valid statuses are: ongoing, completed, cancelled.");
            }
        }

        //Three possibilities for startYear and endYear.
        //1. Both are null, meaning no filtering by year
        //2. startYear is non-null, endYear is null, meaning filtering by startYear only
        //3. Both startYear and endYear are non-null, meaning filtering by a range of years
        if (startYear != null && endYear != null && startYear > endYear) {
            throw new IllegalArgumentException("Start year cannot be greater than end year.");
        }
        //If startYear is non-null and endYear is null, we can set endYear to the current year
        if (startYear != null && endYear == null) {
            endYear = Year.now().getValue();
        }

        return tvShowDao.filterTvShows(title, genre, status, startYear, endYear);

    }

}
