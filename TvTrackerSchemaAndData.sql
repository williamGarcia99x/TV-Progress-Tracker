DROP DATABASE IF EXISTS tv_progress_tracker;
CREATE DATABASE tv_progress_tracker;
USE tv_progress_tracker;

-- Users table for authentication and user management
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at DATE DEFAULT (CURRENT_DATE)
);

CREATE TABLE sessions (
    token VARCHAR(255) PRIMARY KEY,
    user_id INT NOT NULL,
    expires_at DATE NOT NULL,
    FOREIGN KEY foreign_key_sessions (user_id) REFERENCES users(user_id)
);


-- Minimal TV shows table - only store what's needed for tracking
-- All other show details come from TMDb API calls
CREATE TABLE tv_shows (
    show_id INT PRIMARY KEY,  -- TMDb show ID
    name VARCHAR(200) NOT NULL,
    original_name VARCHAR(200) NOT NULL,  -- Original title in original language
    poster_path VARCHAR(200), -- Poster path for the tv_show. This way it doesn't need to be fetched from TMDB.
    created_at DATE DEFAULT (CURRENT_DATE)  -- When first added to our system
);

-- Genres table for normalized genre storage
CREATE TABLE genres (
    genre_id INT PRIMARY KEY,  -- Use TMDb genre IDs
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Junction table for TV shows and their genres (many-to-many relationship)
CREATE TABLE tv_show_genres (
    show_id INT,
    genre_id INT,
    PRIMARY KEY (show_id, genre_id),
    FOREIGN KEY (show_id) REFERENCES tv_shows(show_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id) ON DELETE CASCADE
);

-- User TV Show Tracker - The core tracking table
CREATE TABLE user_tv_tracker (
    tracker_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    show_id INT NOT NULL,
    watch_status ENUM('planning', 'watching', 'completed') NOT NULL,
    episodes_watched INT,
    current_season INT,
    user_rating DECIMAL(3,1) CHECK (user_rating BETWEEN 1.0 AND 10.0),  -- User's personal rating (1.0-10.0)
    notes TEXT,  -- User's personal notes about the show
    date_added DATE DEFAULT (CURRENT_DATE),
    date_started DATE NULL,  -- When user started watching
    date_completed DATE NULL,  -- When user completed the show

    -- Foreign key constraints
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (show_id) REFERENCES tv_shows(show_id) ON DELETE CASCADE,

    -- Ensure one entry per user/show
    UNIQUE KEY unique_user_show (user_id, show_id),

    -- Indexes for analytics queries,
    INDEX idx_user_status (user_id, watch_status),
    INDEX idx_show_status (show_id, watch_status)
);

-- Insert common genres (TMDb genre IDs)
INSERT INTO genres (genre_id, name) VALUES
(10759, 'Action & Adventure'),
(16, 'Animation'),
(35, 'Comedy'),
(80, 'Crime'),
(99, 'Documentary'),
(18, 'Drama'),
(10751, 'Family'),
(10762, 'Kids'),
(9648, 'Mystery'),
(10763, 'News'),
(10764, 'Reality'),
(10765, 'Sci-Fi & Fantasy'),
(10766, 'Soap'),
(10767, 'Talk'),
(10768, 'War & Politics'),
(37, 'Western');

-- Insert some popular TV shows with their TMDb IDs and original names
-- Breaking Bad
INSERT INTO tv_shows (show_id, name, original_name, poster_path) VALUES
(1396, 'Breaking Bad', 'Breaking Bad', 'ztkUQFLlC19CCMYHW9o1zWhJRNq.jpg');

-- The Office (US)
INSERT INTO tv_shows (show_id, name, original_name, poster_path) VALUES
(2316, 'The Office', 'The Office', '7DJKHzAi83BmQrWLrYYOqcoKfhR.jpg');

-- Stranger Things
INSERT INTO tv_shows (show_id, name, original_name, poster_path) VALUES
(66732, 'Stranger Things', 'Stranger Things', 'uOOtwVbSr4QDjAGIifLDwpb2Pdl.jpg');

-- Game of Thrones
INSERT INTO tv_shows (show_id, name, original_name, poster_path) VALUES
(1399, 'Game of Thrones', 'Game of Thrones' ,'1XS1oqL89opfnbLl8WnZY1O1uJx.jpg');

-- Friends
INSERT INTO tv_shows (show_id, name, original_name, poster_path) VALUES
(1668, 'Friends','Friends', '2koX1xLkpTQM4IZebYvKysFW1Nh.jpg');

-- Money Heist (La casa de papel)
INSERT INTO tv_shows (show_id, name, original_name, poster_path) VALUES
(71446, 'Money Heist','La casa de papel', 'l7MngINTyv0O6mNlwNsUlhQ9iwZ.png');

-- Insert genres for each TV show using TMDb genre IDs
-- Breaking Bad
INSERT INTO tv_show_genres (show_id, genre_id) VALUES
(1396, 18),   -- Drama
(1396, 80);   -- Crime

-- The Office (US)
INSERT INTO tv_show_genres (show_id, genre_id) VALUES
(2316, 35);   -- Comedy

-- Stranger Things
INSERT INTO tv_show_genres (show_id, genre_id) VALUES
(66732, 18),     -- Drama
(66732, 10765),  -- Sci-Fi & Fantasy
(66732, 9648);   -- Mystery

-- Game of Thrones
INSERT INTO tv_show_genres (show_id, genre_id) VALUES
(1399, 10765),   -- Sci-Fi & Fantasy
(1399, 18),      -- Drama
(1399, 10759);   -- Action & Adventure

-- Friends
INSERT INTO tv_show_genres (show_id, genre_id) VALUES
(1668, 35);      -- Comedy

-- Money Heist (La casa de papel)
INSERT INTO tv_show_genres (show_id, genre_id) VALUES
(71446, 80),     -- Crime
(71446, 18);     -- Drama

-- The Sopranos
INSERT INTO tv_shows (show_id, name, original_name, poster_path) VALUES
(1398, 'The Sopranos', 'The Sopranos','rTc7ZXdroqjkKivFPvCPX0Ru7uw.jpg');

-- Genre IDs from JSON: 80 = Crime, 18 = Drama
INSERT INTO tv_show_genres (show_id, genre_id) VALUES
(1398, 80),   -- Crime
(1398, 18);   -- Drama

-- tv_shows
INSERT INTO tv_shows (show_id, name, original_name, poster_path) VALUES
(1621, 'Boardwalk Empire', 'Boardwalk Empire','kL6SqlVPpfAof2nQbh1VxkUuXBQ.jpg');

-- tv_show_genres
INSERT INTO tv_show_genres (show_id, genre_id) VALUES
(1621, 80),  -- Crime
(1621, 18);  -- Drama

-- tv_shows
INSERT INTO tv_shows (show_id, name, original_name, poster_path) VALUES
(93405, 'Squid Game','오징어 게임', '1QdXdRYfktUSONkl1oD5gc6Be0s.jpg');

-- tv_show_genres
INSERT INTO tv_show_genres (show_id, genre_id) VALUES
(93405, 10759),  -- Action & Adventure
(93405, 9648),   -- Mystery
(93405, 18);     -- Drama

-- tv_shows
INSERT INTO tv_shows (show_id, name, original_name, poster_path) VALUES
(1996, 'The Flintstones', 'The Flintstones', '30P6ifagQ3fguTsI33KMmDLTAx6.jpg');

-- tv_show_genres
INSERT INTO tv_show_genres (show_id, genre_id) VALUES
(1996, 10751),  -- Family
(1996, 16),     -- Animation
(1996, 35),     -- Comedy
(1996, 10762);  -- Kids


