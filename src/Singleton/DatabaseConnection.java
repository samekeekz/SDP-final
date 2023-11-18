package Singleton;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import Models.*;
import Observer.*;
import Factory.*;

public class DatabaseConnection implements HoopifySubject {
    private final List<HoopifyObserver> observers = new ArrayList<>();
    @Override
    public void addObserver(HoopifyObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (HoopifyObserver observer : observers) {
            observer.update();
        }
    }
    private static DatabaseConnection instance;
    private Connection conn;

    private void createTablesIfNotExist() {
        try (Statement stmt = conn.createStatement()) {
            String createTeamsTableSQL =
                    "CREATE TABLE IF NOT EXISTS teams (" +
                            "id SERIAL PRIMARY KEY," +
                            "team_name VARCHAR(255) UNIQUE NOT NULL," +
                            "coache_name VARCHAR(255) " +
                            ")";
            stmt.executeUpdate(createTeamsTableSQL);

            String createPlayersTableSQL =
                    "CREATE TABLE IF NOT EXISTS players (" +
                            "id SERIAL PRIMARY KEY," +
                            "name VARCHAR(255) NOT NULL," +
                            "age INT NOT NULL," +
                            "position VARCHAR(255) NOT NULL," +
                            "points INT NOT NULL," +
                            "assists INT NOT NULL," +
                            "rebounds INT NOT NULL," +
                            "steals INT NOT NULL," +
                            "blocks INT NOT NULL," +
                            "team_name VARCHAR(255) NOT NULL" +
                            ")";

            stmt.executeUpdate(createPlayersTableSQL);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private DatabaseConnection() {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "823252";

        try {
            conn = DriverManager.getConnection(url, username, password);
            createTablesIfNotExist();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }


}