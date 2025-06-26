package com.cognixia.dao;

import com.cognixia.model.TvShow;
import com.cognixia.model.TvShowStatus;
import com.cognixia.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TvShowDaoImpl implements TvShowDao{
    @Override
    public Optional<TvShow> getTvShowById(int id) {
        return Optional.empty();
    }

    @Override
    public List<TvShow> getTvShowsByTitle(String title) {
        return null;
    }

    @Override
    public List<TvShow> getTvShowsByGenre(String genre) {
        return null;
    }

    @Override
    public List<TvShow> getTvShowsByStatus(String status) {
        return null;
    }

    @Override
    public List<TvShow> getTvShowsByReleaseYearRange(int startYear, int endYear) {
        return null;
    }

    @Override
    public List<TvShow> getAllTvShows() {
        return null;
    }

    @Override
    public boolean insertTvShow(TvShow tvShow) {
        return false;
    }

    /**
     * Filters TV shows based on the provided criteria.
     *
     * @param title     The title of the TV show (can be partial).
     * @param genre     The genre of the TV show.
     * @param status    The status of the TV show (e.g., "ongoing", "completed").
     * @param startYear The start year of the release date range.
     * @param endYear   The end year of the release date range.
     * @return A list of TV shows that match the given criteria.
     */
    @Override
    public List<TvShow> filterTvShows(String title, String genre, String status, Integer startYear, Integer endYear) {
        List<TvShow> shows = new ArrayList<>();

        // Build the SQL query based on the provided criteria
        StringBuilder query = new StringBuilder("SELECT * FROM tv_shows WHERE 1=1");

        //We can assume values will not be null.

        // Case-insensitive search for title
        if(!title.isEmpty()) {
            query.append(" AND LOWER(title) LIKE LOWER(?)");
        }
        // Filter by genre
        if(!genre.isEmpty()){
            query.append(" AND LOWER(genre) LIKE LOWER(?)");
        }

        // Filter by status
        if(!status.isEmpty()){
            query.append(" AND LOWER(status) = LOWER(?)");
        }
        // Filter by release year range
        if(startYear != null && endYear != null) {
            query.append(" AND release_year BETWEEN ? AND ?");
        }

        query.append(";"); // End the query with a semicolon
        // Prepare the SQL statement
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query.toString());

            //Parameters are only set after the query is built
            int index = 1;
            // Set parameters for title
            if (!title.isEmpty()) {
                pstmt.setString(index++, "%" + title + "%");
            }
            // Set parameters for genre
            if (!genre.isEmpty()) {
                pstmt.setString(index++, "%" + genre + "%");
            }
            // Set parameters for status
            if (!status.isEmpty()) {
                pstmt.setString(index++, status);
            }
            // Set parameters for release year range
            if (startYear != null && endYear != null) {
                pstmt.setInt(index++, startYear);
                pstmt.setInt(index++, endYear);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                TvShow show = mapRowToTvShow(rs);
                shows.add(show);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



        return shows;
    }

    public TvShow mapRowToTvShow(ResultSet rs) throws SQLException {
        return new TvShow(
                rs.getInt("show_id"),
                rs.getString("title"),
                rs.getString("genre"),
                rs.getInt("total_episodes"),
                rs.getInt("total_seasons"),
                rs.getDate("release_year"),
                rs.getString("description"),
                TvShowStatus.valueOf(rs.getString("status").toUpperCase()),
                rs.getDate("created_at")
        );
    }

}
