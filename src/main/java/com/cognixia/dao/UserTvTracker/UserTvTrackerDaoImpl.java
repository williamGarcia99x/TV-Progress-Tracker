package com.cognixia.dao.UserTvTracker;

import com.cognixia.dto.TrackShowRequest;
import com.cognixia.exception.ServerException;
import com.cognixia.exception.UserTvTrackerException;
import com.cognixia.model.TvShow;
import com.cognixia.model.UserTvTracker;
import com.cognixia.model.WatchStatus;
import com.cognixia.util.ConnectionFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class UserTvTrackerDaoImpl implements  UserTvTrackerDao {

    /**
     * Tracks a TV show for a user by inserting the show, its genres, and tracking information into the database.
     * If the show already exists, it will not be inserted again and the genre information will remain unchanged.
     *
     * @param showDto
     * @param tvTracker
     * @throws UserTvTrackerException
     */
    // Implement the methods from UserTvTrackerDao interface
    @Override
    public void trackShow(TrackShowRequest.TvShowDto showDto, UserTvTracker tvTracker) throws UserTvTrackerException, ServerException {

        final String INSERT_SHOW   = "INSERT IGNORE INTO tv_shows (show_id, name, original_name, poster_path) VALUES (?, ?, ?, ?)";
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
                psShow.setInt(1, showDto.getId());
                psShow.setString(2, showDto.getName());
                psShow.setString(3, showDto.getOriginal_name());
                psShow.setString(4, showDto.getPoster_path());
                psShow.executeUpdate();
            }

            /* 2️⃣  tv_show_genres (batch) ------------------------------------- */
            try (PreparedStatement psGenre = conn.prepareStatement(INSERT_GENRE)) {
                for (Integer gid : showDto.getGenreIds()) {
                    psGenre.setInt(1, showDto.getId());
                    psGenre.setInt(2, gid);
                    psGenre.addBatch();
                }
                psGenre.executeBatch();
            }

            /* 3️⃣  user_tv_tracker ------------------------------------------- */
            try (PreparedStatement psTrack = conn.prepareStatement(INSERT_TRACK)) {

                psTrack.setInt   (1, tvTracker.getUserId());
                psTrack.setInt   (2, tvTracker.getShowId());
                psTrack.setString(3, tvTracker.getStatus().name().toLowerCase());

                if (tvTracker.getStatus() != WatchStatus.WATCHING) {
                    psTrack.setNull(4, Types.INTEGER);
                    psTrack.setNull(5, Types.INTEGER);
                } else {
                    psTrack.setInt(4, tvTracker.getEpisodesWatched());
                    psTrack.setInt(5, tvTracker.getCurrentSeason());
                }


                if (tvTracker.getUserRating() == null)
                    psTrack.setNull(6, Types.DOUBLE);
                else psTrack.setDouble(6, tvTracker.getUserRating());

                if (tvTracker.getNotes() == null)
                    psTrack.setNull(7, Types.VARCHAR);
                else psTrack.setString(7, tvTracker.getNotes());

                if (tvTracker.getDateStarted() == null)
                    psTrack.setNull(8, Types.DATE);
                else psTrack.setDate(8, Date.valueOf(tvTracker.getDateStarted()));

                if (tvTracker.getDateCompleted() == null)
                    psTrack.setNull(9, Types.DATE);
                else psTrack.setDate(9, Date.valueOf(tvTracker.getDateCompleted()));

                if (psTrack.executeUpdate() == 0)
                    throw new ServerException("Failed to track show: no rows inserted.");
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
                    throw new UserTvTrackerException("You are already tracking this show.");
                }


            } catch (SQLException ignored) { }

            //Generic exception for any other SQL errors
           throw new ServerException(ex.getMessage());
        }


    }

    public void updateTracking(UserTvTracker tracker) throws UserTvTrackerException, ServerException {
        String updateSql = """
                UPDATE user_tv_tracker 
                SET watch_status = ?,
                    episodes_watched = ?,
                    current_season = ?,
                    user_rating = ?,
                    notes = ?,
                    date_started = ?,
                    date_completed = ?
                WHERE tracker_id = ?;""";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(updateSql)){
            pstmt.setString(1, tracker.getStatus().name().toLowerCase());

            //if status is NOT watching, then nullify episodes_watched and current_season
            if (tracker.getStatus() != WatchStatus.WATCHING) {
                pstmt.setNull(2, Types.INTEGER);
                pstmt.setNull(3, Types.INTEGER);
            } else {
                pstmt.setInt(2, tracker.getEpisodesWatched());
                pstmt.setInt(3, tracker.getCurrentSeason());
            }

            if (tracker.getUserRating() == null)
                pstmt.setNull(4, Types.DOUBLE);
             else pstmt.setDouble(4, tracker.getUserRating());

            if (tracker.getNotes() == null)
                pstmt.setNull(5, Types.VARCHAR);
             else pstmt.setString(5, tracker.getNotes());

            if (tracker.getDateStarted() == null)
                pstmt.setNull(6, Types.DATE);
             else pstmt.setDate(6, Date.valueOf(tracker.getDateStarted()));

            if (tracker.getDateCompleted() == null)
                pstmt.setNull(7, Types.DATE);
            else pstmt.setDate(7, Date.valueOf(tracker.getDateCompleted()));

            pstmt.setInt(8, tracker.getTrackerId());

            if(pstmt.executeUpdate() != 1) {
                throw new UserTvTrackerException("Failed to update tracking information. Resource not found");
            }

        }catch (SQLException e) {
            throw new ServerException("Failed to update tracking information: " + e.getMessage());
        }

    }

    public void deleteTracking(int trackerId) throws UserTvTrackerException, ServerException {
        String deleteSql = "DELETE FROM user_tv_tracker WHERE tracker_id = ?;";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {

            pstmt.setInt(1, trackerId);
            if(pstmt.executeUpdate() != 1) {
                throw new UserTvTrackerException("Failed to delete tracking information: Does the tracker_id exist?");
            }

        } catch (SQLException e) {
            throw new ServerException("Failed to delete tracking information: " + e.getMessage());
        }
    }

    public Optional<UserTvTracker> getTrackerById(int trackerId){
        String sql = "SELECT * FROM user_tv_tracker WHERE tracker_id = ?;";
        UserTvTracker tracker = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, trackerId);

            try(ResultSet rs = pstmt.executeQuery();){
                if (rs.next()) {
                    tracker = mapRowToUserTvTracker(rs);
                }
            }

        } catch (SQLException e) {
            throw new ServerException("Failed to retrieve tracker by ID: " + e.getMessage());
        }

        return Optional.ofNullable(tracker);
    }

    public Optional<UserTvTracker> getTrackingByUserIdAndShowID(int userId, int showId) throws ServerException{
        String sql = "SELECT * FROM user_tv_tracker WHERE user_id = ? AND show_id = ?;";
        UserTvTracker tracker = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, showId);


            try(ResultSet rs = pstmt.executeQuery();){
                if (rs.next()) {
                    tracker = mapRowToUserTvTracker(rs);
                }
            }

        } catch (SQLException e) {
            throw new ServerException("Failed to retrieve tracker by user ID and show ID: " + e.getMessage());
        }

        //If the tracker is not found, an empty optional is returned.
        return Optional.ofNullable(tracker);

    }

    public List<UserTvTracker> getTrackersByUserId(int userId) throws ServerException{
        String sql = "SELECT * FROM user_tv_tracker WHERE user_id = ? ORDER BY date_added;";
        List<UserTvTracker> trackers = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try(ResultSet rs = pstmt.executeQuery();){
                while (rs.next()) {
                    trackers.add(mapRowToUserTvTracker(rs));
                }
            }


        } catch (SQLException e) {
            throw new ServerException("Failed to retrieve trackers by user ID: " + e.getMessage());
        }

        return trackers;
    }

    /**
     * Returns the list of TV shows a user is tracking.
     *
     * <pre>
     * SELECT ts.show_id, ts.original_name, ts.created_at
     * FROM   user_tv_tracker utt
     * JOIN   tv_shows ts ON utt.show_id = ts.show_id
     * WHERE  utt.user_id = ?;
     * </pre>
     *
     * @param userId the authenticated user’s ID
     * @return list of {@code TvShow} records (may be empty)
     * @throws ServerException if a SQL or connection error occurs
     */
    public List<TvShow> getTrackedShowsByUserId(int userId, WatchStatus status) throws ServerException {
        final String sql =
                "SELECT ts.show_id, ts.name, ts.original_name, ts.poster_path, ts.created_at " +
                        "FROM user_tv_tracker utt " +
                        "JOIN tv_shows ts ON utt.show_id = ts.show_id " +
                        "WHERE utt.user_id = ? AND utt.watch_status = ? " +
                        "ORDER BY ts.created_at;";

        List<TvShow> shows = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, status.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    shows.add(mapRowToTvShow(rs));
                }
            }

        } catch (SQLException e) {
            throw new ServerException(
                    "Failed to retrieve tracked shows for user " + userId + ": " + e.getMessage()
            );
        }

        return shows;
    }



    private UserTvTracker mapRowToUserTvTracker(ResultSet rs) throws SQLException {
        return new UserTvTracker(
                rs.getInt("tracker_id"),
                rs.getInt("user_id"),
                rs.getInt("show_id"),
                WatchStatus.valueOf(rs.getString("watch_status").toUpperCase()),
                rs.getInt("episodes_watched"),
                rs.getInt("current_season"),
                rs.getDouble("user_rating"),
                rs.getString("notes"),
                rs.getDate("date_added").toLocalDate(),
                rs.getDate("date_started") != null ? rs.getDate("date_started").toLocalDate() : null,
                rs.getDate("date_completed") != null ? rs.getDate("date_completed").toLocalDate() : null
        );
    }
    /**
     * Helper that converts a {@link ResultSet} row into a {@link TvShow}.
     */
    private TvShow mapRowToTvShow(ResultSet rs) throws SQLException {
        return new TvShow(rs.getInt("show_id"), rs.getString("name"), rs.getString("original_name"),
                rs.getString("poster_path"), rs.getDate("created_at").toLocalDate());
    }




}
