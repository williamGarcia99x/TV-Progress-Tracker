package com.cognixia.dao.TvShow;

import com.cognixia.model.TvShow;
import com.cognixia.util.ConnectionFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TvShowDaoImpl implements TvShowDao{


    @Override
    public Optional<TvShow> getTvShowById(int showId) {
       //implemen method
        try{
            Connection connection = ConnectionFactory.getConnection();
            String sql = "SELECT * FROM tv_shows WHERE show_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, showId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                TvShow tvShow = mapRowToTvShow(rs);
                return Optional.of(tvShow);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();

        }
    }


    @Override
    public List<TvShow> filterTvShows(String title, String genre, String status, Integer startYear, Integer endYear) {
        List<TvShow> shows = new ArrayList<>();

        //implement method


        return shows;
    }

    public TvShow mapRowToTvShow(ResultSet rs) throws SQLException {
        return new TvShow(rs.getInt("show_id"),
                rs.getString("original_name"),
                rs.getDate("created_at"));
    }

}
