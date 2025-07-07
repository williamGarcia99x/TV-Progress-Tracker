package com.cognixia.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

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
    private static final HikariDataSource dataSource;

    /* ---------- Static initializer ---------- */
    static {
        HikariConfig cfg = new HikariConfig();

        // Core JDBC properties
        cfg.setJdbcUrl("jdbc:mysql://localhost:3306/tv_progress_tracker");
        cfg.setUsername("root");
        cfg.setPassword("password");

        dataSource = new HikariDataSource(cfg);
    }

    /** Hide constructor */
    private ConnectionFactory() { }

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
