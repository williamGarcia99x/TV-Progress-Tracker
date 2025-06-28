package com.cognixia.dao;

import com.cognixia.model.UserTvTracker;
import com.cognixia.model.WatchStatus;
import com.cognixia.util.ConnectionFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@Repository
public class UserTvTrackerDaoImpl implements  UserTvTrackerDao {

    // Implement the methods from UserTvTrackerDao interface
    @Override
    public void trackShow(UserTvTracker tracker) {
        // Example implementation of tracking a show
        try  {
            Connection connection = ConnectionFactory.getConnection();
            // Logic to insert the user and tvShow into the database
            String sql = "INSERT INTO user_tv_tracker (user_id, show_id, watch_status, episodes_watched" +
                    ", current_season, user_rating, notes, date_started, date_completed) VALUES (?,?,?,?,?,?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, tracker.getUserId());
            preparedStatement.setInt(2, tracker.getShowId());
            preparedStatement.setString(3, tracker.getStatus().toString());
//            // if tracker.getStatus is not WATCHING, then episodesWatched and currentSeason can be null
            if (tracker.getStatus() != WatchStatus.WATCHING) {
                preparedStatement.setNull(4, java.sql.Types.INTEGER); // Set to NULL if not WATCHING
                preparedStatement.setNull(5, java.sql.Types.INTEGER); // Set to NULL if not WATCHING
            } else {
                preparedStatement.setInt(4, tracker.getEpisodesWatched());
                preparedStatement.setInt(5, tracker.getCurrentSeason());
            }

            //Check if userRating is null or 0, and set it to NULL in the database
            if(tracker.getUserRating() == null){
                preparedStatement.setNull(6, java.sql.Types.DOUBLE); // Set to NULL if userRating is 0
            } else {
                preparedStatement.setDouble(6, tracker.getUserRating());
            }

            preparedStatement.setString(7, tracker.getNotes());
            preparedStatement.setDate(8, tracker.getDateStarted());
            preparedStatement.setDate(9, tracker.getDateCompleted());
            // Assuming dateAdded is automatically set by the database, so not included in the insert statement
            // Execute the insert statement
            if(preparedStatement.executeUpdate() == 0) {
                // Handle the case where no rows were affected, indicating failure to insert
                throw new SQLException("Failed to track show: No rows affected.");
            }
        } catch (SQLException e) {
            // Handle SQL exceptions, such as connection issues or SQL syntax errors
            throw new RuntimeException(e);
        }
    }
}
