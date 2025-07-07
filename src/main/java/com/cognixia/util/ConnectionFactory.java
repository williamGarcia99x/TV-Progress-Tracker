package com.cognixia.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Thread‑safe, pooled connection factory backed by HikariCP.
 *
 * Call {@link #getConnection()} in a try‑with‑resources block:
 *
 * try (Connection conn = ConnectionFactory.getConnection()) {
 *     …
 * }
 *
 * Each call borrows a connection from the pool and returns it automatically
 * when closed.
 */
public final class ConnectionFactory {

    /** Singleton pool instance */
    private static  HikariDataSource dataSource;

    private static String datasourceUrl;

    private static String username;

    private static String password;

    /* ---------- Static initializer ---------- */
    static {
        try {
            makeConnection();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** Hide constructor */
    private ConnectionFactory() { }

    private static void makeConnection() throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
        Properties props = new Properties();

        props.load(new FileInputStream("src/main/resources/application.properties"));

        String url = props.getProperty("spring.datasource.url");
        String username = props.getProperty("spring.datasource.username");
        String password = props.getProperty("spring.datasource.password");

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(url);
        cfg.setUsername(username);
        cfg.setPassword(password);
        dataSource = new HikariDataSource(cfg);
    }


    /* ---------- Public API ---------- */

    /** Borrow a connection from the pool. */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /** Gracefully close the pool on application shutdown (optional). */
    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
