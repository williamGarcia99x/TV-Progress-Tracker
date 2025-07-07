# TV Progress Tracker (Backend API) 🎬

This is the backend component of the **Popcorn Watchers** project, a modern web application for tracking TV show progress. The backend provides RESTful APIs for user authentication, TV show tracking, and data management. It is built with **Spring Boot** and integrates with a relational database to store user and tracking data.

---

## Project Overview

The backend API is responsible for:

- Managing user accounts and authentication.
- Storing and retrieving user-tracked TV show data.
- Handling CRUD operations for tracked shows, ratings, and notes.
- Integrating with [The Movie Database (TMDB)](https://www.themoviedb.org/) for show metadata.

---

## Features

- **User Management:** Secure user registration, login, and session handling.
- **TV Show Tracking:** APIs to track episodes/seasons watched, add ratings, and manage notes.
- **Data Validation:** Ensures data integrity and proper input validation.
- **Error Handling:** Custom exceptions for meaningful error messages.
- **Database Integration:** Persistent storage for user and tracking data.
- **Scalable Architecture:** Built with Spring Boot for modularity and scalability.

---

## Tech Stack

- **Framework:** Spring Boot
- **Language:** Java
- **Database:** MySQL
- **Authentication:** Cookie-based sessions
- **Build Tool:** Maven

---

## Getting Started

### 1. **Clone the Repository**

```bash
git clone https://github.com/williamGarcia99x/TV-Progress-Tracker.git
cd TV-Progress-Tracker
```

### 2. **Configure the Database**

- Ensure you have **MySQL** and **MySQL Workbench** installed on your system. If not, follow [this guide](https://dev.mysql.com/doc/workbench/en/wb-installing.html) to install them.
- Open **MySQL Workbench** and connect to your MySQL server.
- Use the `TvTrackerSchemaAndData.sql` file to create and initialize the database:
    1. Open the `TvTrackerSchemaAndData.sql` file in MySQL Workbench.
    2. Execute the script to create the `tv_progress_tracker` database and populate it with the required schema and initial data.

Update the database connection details in the `application.properties` file:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tv_progress_tracker
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
server.error.include-message=always
```

### 3. **Install Dependencies**

Ensure you have Maven installed, then run:

```bash
mvn clean install
```

### 4. **Run the Application**

Start the Spring Boot application:

```bash
mvn spring-boot:run
```

The backend will be available at [http://localhost:8080](http://localhost:8080).

---

## 📝 Additional Notes

- **Frontend Integration:** This backend is designed to work with the [Popcorn Watchers Frontend](https://github.com/williamGarcia99x/TV-Progress-Tracker-UI.git). Ensure the `NEXT_PUBLIC_API_BASE_URL` in the frontend `.env.local` matches the backend server address.
- **Database Schema:** The database schema is defined in `TvTrackerSchemaAndData.sql`. Run this script to initialize your database.

---

## 🤝 Contributing

Pull requests and issues are welcome! Please open an issue to discuss your ideas or report bugs.

---

**Enjoy building and tracking your TV shows!**