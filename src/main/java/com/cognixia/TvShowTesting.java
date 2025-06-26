package com.cognixia;

import com.cognixia.dao.TvShowDaoImpl;
import com.cognixia.model.TvShow;
import com.cognixia.service.TvShowService;

import java.util.List;

public class TvShowTesting {
    public static void main(String[] args) {

        TvShowService tvShowService = new TvShowService(new TvShowDaoImpl());
        List<TvShow> showList = tvShowService.filterTvShows("", "drama", "completed", null, null);
        for(TvShow show : showList) {
            System.out.println(show.toString());
        }


    }
}
