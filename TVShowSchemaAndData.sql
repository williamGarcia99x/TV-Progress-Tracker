-- TV Show Progress Tracker Database Schema
-- Drop database if exists and create fresh
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

-- TV Shows table containing the master list of available shows
CREATE TABLE tv_shows (
    show_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    genre VARCHAR(100),
    total_episodes INT NOT NULL DEFAULT 1,
    total_seasons INT NOT NULL DEFAULT 1,
    release_year YEAR,
    description TEXT,
    status ENUM('ongoing', 'completed', 'cancelled') DEFAULT 'ongoing',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User TV Show Tracker - Junction table tracking user's progress on shows
CREATE TABLE user_tv_tracker (
    tracker_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    show_id INT NOT NULL,
    watch_status ENUM('planning', 'in-progress', 'completed') NOT NULL DEFAULT 'planning',
    episodes_watched INT DEFAULT 0,
    current_season INT DEFAULT 1,
    date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_started TIMESTAMP NULL, -- timestamp when tuple obtains "in-progress" status
    date_completed TIMESTAMP NULL, 
    
    -- Foreign key constraints
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (show_id) REFERENCES tv_shows(show_id) ON DELETE CASCADE,
    
    -- Ensure one entry per user/show
    UNIQUE KEY unique_user_show (user_id, show_id),
    
    -- Ensure episodes watched doesn't exceed total episodes. Validation 
    -- is performed at the application level
);

-- Populate TV Shows table with at least 10 shows (as required)
INSERT INTO tv_shows (title, genre, total_episodes, total_seasons, release_year, description, status) VALUES
('Breaking Bad', 'Drama/Crime', 62, 5, 2008, 'A high school chemistry teacher turned methamphetamine manufacturer', 'completed'),
('The Office', 'Comedy', 188, 9, 2005, 'Mockumentary sitcom about office employees', 'completed'),
('Stranger Things', 'Sci-Fi/Horror', 42, 4, 2016, 'Kids in a small town encounter supernatural forces', 'completed'),
('Game of Thrones', 'Fantasy/Drama', 73, 8, 2011, 'Epic fantasy series based on George R.R. Martin novels', 'completed'),
('The Crown', 'Historical Drama', 60, 6, 2016, 'The reign of Queen Elizabeth II', 'completed'),
('Friends', 'Comedy', 236, 10, 1994, 'Six friends navigating life in New York City', 'completed'),
('The Mandalorian', 'Sci-Fi/Adventure', 24, 3, 2019, 'A bounty hunter in the Star Wars universe', 'completed'),
('Squid Game', 'Thriller/Drama', 9, 1, 2021, 'Desperate people compete in deadly childhood games', 'ongoing'),
('Wednesday', 'Comedy/Horror', 8, 1, 2022, 'Wednesday Addams at Nevermore Academy', 'ongoing'),
('House of the Dragon', 'Fantasy/Drama', 18, 2, 2022, 'Game of Thrones prequel about House Targaryen', 'ongoing'),
('The Bear', 'Comedy/Drama', 28, 3, 2022, 'A chef returns to run his deceased brother\'s restaurant', 'ongoing'),
('Avatar: The Last Airbender', 'Animation/Adventure', 61, 3, 2005, 'A young airbender must master the elements to save the world', 'completed'),
('The Witcher', 'Fantasy/Adventure', 24, 3, 2019, 'A mutated monster hunter struggles to find his place in a world', 'ongoing'),
('Ozark', 'Crime/Drama', 44, 4, 2017, 'A financial advisor launders money for a Mexican cartel', 'completed'),
('Euphoria', 'Drama/Teen', 18, 2, 2019, 'A group of high school students navigate love, drugs, and trauma', 'ongoing');

-- Populate Users table with sample users
-- Note: In a real application, passwords should be properly hashed using bcrypt or similar
-- These are example hashes for demonstration (password123 hashed with bcrypt)
INSERT INTO users (username, password_hash) VALUES
('john_doe', '$2a$10$N9qo8uLOickgx2ZMRZoMye1K2SJPtQJ8H8G7L9Z3QQwQR5Z8N9qo8'),
('jane_smith', '$2a$10$N9qo8uLOickgx2ZMRZoMye1K2SJPtQJ8H8G7L9Z3QQwQR5Z8N9qo8'),
('mike_wilson', '$2a$10$N9qo8uLOickgx2ZMRZoMye1K2SJPtQJ8H8G7L9Z3QQwQR5Z8N9qo8'),
('sarah_jones', '$2a$10$N9qo8uLOickgx2ZMRZoMye1K2SJPtQJ8H8G7L9Z3QQwQR5Z8N9qo8'),
('david_brown', '$2a$10$N9qo8uLOickgx2ZMRZoMye1K2SJPtQJ8H8G7L9Z3QQwQR5Z8N9qo8');

-- Populate User TV Tracker table with sample tracking data
-- This demonstrates the three different watch statuses and realistic progress
INSERT INTO user_tv_tracker (user_id, show_id, watch_status, episodes_watched, current_season, date_added, date_started, date_completed) VALUES
-- John's tracking data (user_id = 1)
(1, 1, 'completed', 62, 5, '2023-01-15 10:00:00', '2023-01-15 10:00:00', '2023-02-28 20:30:00'), -- Breaking Bad
(1, 2, 'in-progress', 45, 3, '2023-03-01 19:00:00', '2023-03-01 19:00:00', NULL), -- The Office
(1, 3, 'planning', 0, 1, '2023-06-15 21:00:00', NULL, NULL), -- Stranger Things
(1, 8, 'completed', 9, 1, '2023-06-01 21:00:00', '2023-06-01 21:00:00', '2023-06-05 23:15:00'), -- Squid Game
(1, 12, 'planning', 0, 1, '2023-07-10 18:30:00', NULL, NULL), -- Avatar

-- Jane's tracking data (user_id = 2)
(2, 1, 'in-progress', 25, 2, '2023-04-10 20:00:00', '2023-04-10 20:00:00', NULL), -- Breaking Bad
(2, 6, 'completed', 236, 10, '2022-12-01 18:00:00', '2022-12-01 18:00:00', '2023-01-30 22:00:00'), -- Friends
(2, 3, 'in-progress', 18, 2, '2023-05-15 21:30:00', '2023-05-15 21:30:00', NULL), -- Stranger Things
(2, 9, 'planning', 0, 1, '2023-07-01 14:00:00', NULL, NULL), -- Wednesday
(2, 13, 'in-progress', 8, 1, '2023-06-20 19:45:00', '2023-06-20 19:45:00', NULL), -- The Witcher

-- Mike's tracking data (user_id = 3)
(3, 4, 'completed', 73, 8, '2023-02-01 19:30:00', '2023-02-01 19:30:00', '2023-04-15 23:45:00'), -- Game of Thrones
(3, 7, 'in-progress', 16, 2, '2023-06-01 20:00:00', '2023-06-01 20:00:00', NULL), -- The Mandalorian
(3, 12, 'planning', 0, 1, '2023-07-05 16:20:00', NULL, NULL), -- Avatar
(3, 14, 'completed', 44, 4, '2023-03-10 21:15:00', '2023-03-10 21:15:00', '2023-05-20 22:30:00'), -- Ozark

-- Sarah's tracking data (user_id = 4)
(4, 5, 'in-progress', 25, 3, '2023-05-01 20:30:00', '2023-05-01 20:30:00', NULL), -- The Crown
(4, 10, 'planning', 0, 1, '2023-07-12 17:00:00', NULL, NULL), -- House of the Dragon
(4, 11, 'in-progress', 12, 2, '2023-06-10 19:00:00', '2023-06-10 19:00:00', NULL), -- The Bear
(4, 15, 'planning', 0, 1, '2023-07-08 20:45:00', NULL, NULL), -- Euphoria

-- David's tracking data (user_id = 5)
(5, 2, 'completed', 188, 9, '2023-01-05 18:00:00', '2023-01-05 18:00:00', '2023-04-20 21:45:00'), -- The Office
(5, 8, 'planning', 0, 1, '2023-07-15 16:30:00', NULL, NULL), -- Squid Game
(5, 9, 'completed', 8, 1, '2023-06-25 22:00:00', '2023-06-25 22:00:00', '2023-06-28 23:30:00'), -- Wednesday
(5, 13, 'planning', 0, 1, '2023-07-20 19:15:00', NULL, NULL); -- The Witcher

-- Create indexes for better query performance
CREATE INDEX idx_user_tv_tracker_user_id ON user_tv_tracker(user_id);
CREATE INDEX idx_user_tv_tracker_show_id ON user_tv_tracker(show_id);
CREATE INDEX idx_user_tv_tracker_status ON user_tv_tracker(watch_status);
CREATE INDEX idx_tv_shows_genre ON tv_shows(genre);
CREATE INDEX idx_tv_shows_status ON tv_shows(status);