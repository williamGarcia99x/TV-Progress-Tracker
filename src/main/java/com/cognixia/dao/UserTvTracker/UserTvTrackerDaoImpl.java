package com.cognixia.dao.UserTvTracker;

import com.cognixia.dto.TrackShowRequest;
import com.cognixia.exception.UserTvTrackerInsertionException;
import com.cognixia.model.UserTvTracker;
import com.cognixia.model.WatchStatus;
import com.cognixia.util.ConnectionFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;


@Repository
public class UserTvTrackerDaoImpl implements  UserTvTrackerDao {

    /**
     * Tracks a TV show for a user by inserting the show, its genres, and tracking information into the database.
     * If the show already exists, it will not be inserted again and the genre information will remain unchanged.
     * @param trackerDto
     * @param showDto
     * @throws UserTvTrackerInsertionException
     */
    // Implement the methods from UserTvTrackerDao interface
    @Override
    public void trackShow(TrackShowRequest.UserTvTrackerDto trackerDto, TrackShowRequest.TvShowDto showDto) throws UserTvTrackerInsertionException {

        final String INSERT_SHOW   = "INSERT IGNORE INTO tv_shows (show_id, original_name) VALUES (?, ?)";
        final String INSERT_GENRE  = "INSERT IGNORE INTO tv_show_genres (show_id, genre_id) VALUES (?, ?)";
        final String INSERT_TRACK  = """
        INSERT INTO user_tv_tracker
        (user_id, show_id, watch_status, episodes_watched, current_season,
         user_rating, notes, date_started, date_completed)
        VALUES (?,?,?,?,?,?,?,?,?);""";

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);

            /* 1️⃣  tv_shows ---------------------------------------------------- */
            try (PreparedStatement psShow = conn.prepareStatement(INSERT_SHOW)) {
                psShow.setInt(1, showDto.getShowId());
                psShow.setString(2, showDto.getOriginalName());
                psShow.executeUpdate();
            }

            /* 2️⃣  tv_show_genres (batch) ------------------------------------- */
            try (PreparedStatement psGenre = conn.prepareStatement(INSERT_GENRE)) {
                for (Integer gid : showDto.getGenreIds()) {
                    psGenre.setInt(1, showDto.getShowId());
                    psGenre.setInt(2, gid);
                    psGenre.addBatch();
                }
                psGenre.executeBatch();
            }

            /* 3️⃣  user_tv_tracker ------------------------------------------- */
            try (PreparedStatement psTrack = conn.prepareStatement(INSERT_TRACK)) {

                psTrack.setInt   (1, trackerDto.getUserId());
                psTrack.setInt   (2, trackerDto.getShowId());
                psTrack.setString(3, trackerDto.getStatus().name().toLowerCase());

                if (trackerDto.getStatus() != WatchStatus.WATCHING) {
                    psTrack.setNull(4, java.sql.Types.INTEGER);
                    psTrack.setNull(5, java.sql.Types.INTEGER);
                } else {
                    psTrack.setInt(4, trackerDto.getEpisodesWatched());
                    psTrack.setInt(5, trackerDto.getCurrentSeason());
                }


                if (trackerDto.getUserRating() == null)
                    psTrack.setNull(6, Types.DOUBLE);
                else psTrack.setDouble(6, trackerDto.getUserRating());


                if (trackerDto.getNotes() == null)
                    psTrack.setNull(7, Types.VARCHAR);
                else psTrack.setString(7, trackerDto.getNotes());

                if (trackerDto.getDateStarted() == null)
                    psTrack.setNull(8, Types.DATE);
                else psTrack.setDate(8, trackerDto.getDateStarted());

                if (trackerDto.getDateCompleted() == null)
                    psTrack.setNull(9, Types.DATE);
                else psTrack.setDate(9, trackerDto.getDateCompleted());

                if (psTrack.executeUpdate() == 0)
                    throw new SQLException("Failed to track show: no rows inserted.");
            }

            conn.commit();                  // ✅ all good
        }
        //Possible exception causes, user is already tracking show so user_id, show_id is a duplicate key
        catch (SQLException ex) {
            // Connection may already be closed in rare cases, so wrap rollback in try‑catch
            try (Connection conn = ConnectionFactory.getConnection()) {
                // If the connection is not in auto-commit mode, rollback the transaction
                if (!conn.getAutoCommit()) conn.rollback();

                if(ex.getErrorCode() == 1062) {
                    // Duplicate entry error code for MySQL
                    throw new UserTvTrackerInsertionException("You are already tracking this show.");
                }


            } catch (SQLException ignored) { }

            //Generic exception for any other SQL errors
           throw new UserTvTrackerInsertionException(ex.getMessage());
        }


    }



    public void insertToTracker(UserTvTracker tracker) {
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
