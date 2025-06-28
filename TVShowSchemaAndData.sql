DROP DATABASE IF EXISTS tv_progress_tracker;
CREATE DATABASE tv_progress_tracker;
USE tv_progress_tracker;

-- Users table for authentication and user management
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Genres table for normalized genre storage
CREATE TABLE genres (
    genre_id INT PRIMARY KEY,  -- Use TMDb genre IDs
    name VARCHAR(100) NOT NULL UNIQUE
);

-- TV Shows table containing the master list of available shows
CREATE TABLE tv_shows (
    show_id INT PRIMARY KEY,  -- Use TMDb show ID directly
    title VARCHAR(200) NOT NULL,
    original_name VARCHAR(200),  -- Original title in original language
    original_language CHAR(2),  -- ISO 639-1 language code
    overview TEXT,  -- Description/plot summary
    first_air_date DATE,  -- More precise than release_year
    adult BOOLEAN DEFAULT FALSE,
    backdrop_path VARCHAR(255),  -- TMDb backdrop image path
    poster_path VARCHAR(255),    -- TMDb poster image path
    vote_average DECIMAL(3,1) DEFAULT 0,  -- TMDb rating (0.0-10.0) User rating
    vote_count INT DEFAULT 0, -- Number of votes received
    total_episodes INT DEFAULT 1,
    total_seasons INT DEFAULT 1,
    status ENUM('ongoing', 'completed', 'cancelled') DEFAULT 'ongoing',
    origin_country JSON,  -- Store array of country codes as JSON
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Indexes for better query performance
    INDEX idx_title (title),
    INDEX idx_first_air_date (first_air_date),
    INDEX idx_vote_average (vote_average)
);

-- Junction table for TV shows and their genres (many-to-many relationship)
CREATE TABLE tv_show_genres (
    show_id INT,
    genre_id INT,
    PRIMARY KEY (show_id, genre_id),
    FOREIGN KEY (show_id) REFERENCES tv_shows(show_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id) ON DELETE CASCADE
);

-- User TV Show Tracker - Junction table tracking user's progress on shows
CREATE TABLE user_tv_tracker (
    tracker_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    show_id INT NOT NULL,
    watch_status ENUM('planning', 'watching', 'completed') NOT NULL DEFAULT 'planning',
    episodes_watched INT DEFAULT 0,
    current_season INT DEFAULT 1,
    user_rating DECIMAL(2,1),  -- User's personal rating (1.0-10.0). Does not affect TV-show rating.
    notes TEXT,  -- User's personal notes about the show
    date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_started TIMESTAMP NULL,  -- When user started watching
    date_completed TIMESTAMP NULL,  -- When user completed the show

    -- Foreign key constraints
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (show_id) REFERENCES tv_shows(show_id) ON DELETE CASCADE,

    -- Ensure one entry per user/show
    UNIQUE KEY unique_user_show (user_id, show_id),

    -- Indexes for common queries
    INDEX idx_user_status (user_id, watch_status),
    INDEX idx_user_rating (user_id, user_rating)
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


-- Insert Breaking Bad TV show
INSERT INTO tv_shows (
    show_id,
    title,
    original_name,
    original_language,
    overview,
    first_air_date,
    adult,
    backdrop_path,
    poster_path,
    vote_average,
    vote_count,
    origin_country
) VALUES (
    1396,
    'Breaking Bad',
    'Breaking Bad',
    'en',
    'Walter White, a New Mexico chemistry teacher, is diagnosed with Stage III cancer and given a prognosis of only two years left to live. He becomes filled with a sense of fearlessness and an unrelenting desire to secure his family\'s financial future at any cost as he enters the dangerous world of drugs and crime.',
    '2008-01-20',
    FALSE,
    '/gc8PfyTqzqltKPW3X0cIVUGmagz.jpg',
    '/ztkUQFLlC19CCMYHW9o1zWhJRNq.jpg',
    8.9,
    15710,
    JSON_ARRAY('US')
);

-- Insert genre associations for Breaking Bad
-- Genre ID 18 = Drama, Genre ID 80 = Crime
INSERT INTO tv_show_genres (show_id, genre_id) VALUES
(1396, 18),  -- Drama
(1396, 80);  -- Crime

-- Insert The Office (US) TV show
INSERT INTO tv_shows (
    show_id,
    title,
    original_name,
    original_language,
    overview,
    first_air_date,
    adult,
    backdrop_path,
    poster_path,
    vote_average,
    vote_count,
    origin_country
) VALUES (
    2316,
    'The Office',
    'The Office',
    'en',
    'The everyday lives of office employees in the Scranton, Pennsylvania branch of the fictional Dunder Mifflin Paper Company.',
    '2005-03-24',
    FALSE,
    '/mLyW3UTgi2lsMdtueYODcfAB9Ku.jpg',
    '/7DJKHzAi83BmQrWLrYYOqcoKfhR.jpg',
    8.6,
    4552,
    JSON_ARRAY('US')
);

-- Insert genre association for The Office (US)
-- Genre ID 35 = Comedy
INSERT INTO tv_show_genres (show_id, genre_id) VALUES
(2316, 35);  -- Comedy

-- Insert Stranger Things TV show
INSERT INTO tv_shows (
    show_id,
    title,
    original_name,
    original_language,
    overview,
    first_air_date,
    adult,
    backdrop_path,
    poster_path,
    vote_average,
    vote_count,
    origin_country
) VALUES (
    66732,
    'Stranger Things',
    'Stranger Things',
    'en',
    'When a young boy vanishes, a small town uncovers a mystery involving secret experiments, terrifying supernatural forces, and one strange little girl.',
    '2016-07-15',
    FALSE,
    '/56v2KjBlU4XaOv9rVYEQypROD7P.jpg',
    '/uOOtwVbSr4QDjAGIifLDwpb2Pdl.jpg',
    8.6,
    18437,
    JSON_ARRAY('US')
);

-- Insert genre associations for Stranger Things
-- Genre ID 18 = Drama, Genre ID 10765 = Sci-Fi & Fantasy, Genre ID 9648 = Mystery
INSERT INTO tv_show_genres (show_id, genre_id) VALUES
(66732, 18),    -- Drama
(66732, 10765), -- Sci-Fi & Fantasy
(66732, 9648);  -- Mystery

-- Insert Game of Thrones TV show
INSERT INTO tv_shows (
    show_id,
    title,
    original_name,
    original_language,
    overview,
    first_air_date,
    adult,
    backdrop_path,
    poster_path,
    vote_average,
    vote_count,
    origin_country
) VALUES (
    1399,
    'Game of Thrones',
    'Game of Thrones',
    'en',
    'Seven noble families fight for control of the mythical land of Westeros. Friction between the houses leads to full-scale war. All while a very ancient evil awakens in the farthest north. Amidst the war, a neglected military order of misfits, the Night\'s Watch, is all that stands between the realms of men and icy horrors beyond.',
    '2011-04-17',
    FALSE,
    '/zZqpAXxVSBtxV9qPBcscfXBcL2w.jpg',
    '/1XS1oqL89opfnbLl8WnZY1O1uJx.jpg',
    8.5,
    25170,
    JSON_ARRAY('US')
);

-- Insert genre associations for Game of Thrones
-- Genre ID 10765 = Sci-Fi & Fantasy, Genre ID 18 = Drama, Genre ID 10759 = Action & Adventure
INSERT INTO tv_show_genres (show_id, genre_id) VALUES
(1399, 10765), -- Sci-Fi & Fantasy
(1399, 18),    -- Drama
(1399, 10759); -- Action & Adventure

-- Insert Friends TV show
INSERT INTO tv_shows (
    show_id,
    title,
    original_name,
    original_language,
    overview,
    first_air_date,
    adult,
    backdrop_path,
    poster_path,
    vote_average,
    vote_count,
    origin_country
) VALUES (
    1668,
    'Friends',
    'Friends',
    'en',
    'Six young people from New York City, on their own and struggling to survive in the real world, find the companionship, comfort and support they get from each other to be the perfect antidote to the pressures of life.',
    '1994-09-22',
    FALSE,
    '/l0qVZIpXtIo7km9u5Yqh0nKPOr5.jpg',
    '/2koX1xLkpTQM4IZebYvKysFW1Nh.jpg',
    8.4,
    8421,
    JSON_ARRAY('US')
);

-- Insert genre association for Friends
-- Genre ID 35 = Comedy
INSERT INTO tv_show_genres (show_id, genre_id) VALUES
(1668, 35);  -- Comedy

-- Insert Money Heist (La casa de papel) TV show
INSERT INTO tv_shows (
    show_id,
    title,
    original_name,
    original_language,
    overview,
    first_air_date,
    adult,
    backdrop_path,
    poster_path,
    vote_average,
    vote_count,
    origin_country
) VALUES (
    71446,
    'Money Heist',
    'La casa de papel',
    'es',
    'To carry out the biggest heist in history, a mysterious man called The Professor recruits a band of eight robbers who have a single characteristic: none of them has anything to lose. Five months of seclusion - memorizing every step, every detail, every probability - culminate in eleven days locked up in the National Coinage and Stamp Factory of Spain, surrounded by police forces and with dozens of hostages in their power, to find out whether their suicide wager will lead to everything or nothing.',
    '2017-05-02',
    FALSE,
    '/gFZriCkpJYsApPZEF3jhxL4yLzG.jpg',
    '/reEMJA1uzscCbkpeRJeTT2bjqUp.jpg',
    8.2,
    18950,
    JSON_ARRAY('ES')
);

-- Insert genre associations for Money Heist
-- Genre ID 80 = Crime, Genre ID 18 = Drama
INSERT INTO tv_show_genres (show_id, genre_id) VALUES
(71446, 80),  -- Crime
(71446, 18);  -- Drama