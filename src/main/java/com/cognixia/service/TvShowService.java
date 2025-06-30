package com.cognixia.service;

import com.cognixia.dao.TvShow.TvShowDao;
import com.cognixia.model.TvShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class TvShowService {

    private TvShowDao tvShowDao;

    @Autowired
    public TvShowService(TvShowDao tvShowDao) {
        this.tvShowDao = tvShowDao;
    }


    public Optional<TvShow> findTvShowById(int id){
        return tvShowDao.getTvShowById(id);
    }

    // This class will contain methods to interact with TvShowDao
    // and perform business logic related to TV shows.



//    public List<TvShow> filterTvShows(String title, String genre, String status, Integer startYear, Integer endYear) {
//
//        //Validate input data
//        //Title, genre, status cannot be null, but they can be empty strings
//        if (title == null || genre == null || status == null) {
//            throw new IllegalArgumentException("Title, genre, and status cannot be null.");
//        }
//        //If status is not empty, we must make sure that it matches one of the valid TvShowStatus values
//        if (!status.isEmpty()) {
//            try{
//                TvShowStatus.valueOf(status.toUpperCase());
//            } catch (IllegalArgumentException e) {
//                throw new IllegalArgumentException("Invalid status provided. Valid statuses are: ongoing, completed, cancelled.");
//            }
//        }
//
//        //Three possibilities for startYear and endYear.
//        //1. Both are null, meaning no filtering by year
//        //2. startYear is non-null, endYear is null, meaning filtering by startYear only
//        //3. Both startYear and endYear are non-null, meaning filtering by a range of years
//        if (startYear != null && endYear != null && startYear > endYear) {
//            throw new IllegalArgumentException("Start year cannot be greater than end year.");
//        }
//        //If startYear is non-null and endYear is null, we can set endYear to the current year
//        if (startYear != null && endYear == null) {
//            endYear = Year.now().getValue();
//        }
//
//        return tvShowDao.filterTvShows(title, genre, status, startYear, endYear);
//
//    }

}
