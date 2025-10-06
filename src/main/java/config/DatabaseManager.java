package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/cafe";
    private static final String USER = "postgres";
    private static final String PASSWORD = "02V.L.A.D.O.S.09";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}