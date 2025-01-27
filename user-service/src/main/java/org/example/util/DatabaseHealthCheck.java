package org.example.util;

import jakarta.annotation.PostConstruct;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseHealthCheck {

    private final DataSource dataSource;

    public DatabaseHealthCheck(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void checkDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Successfully connected to the database!");
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }
}