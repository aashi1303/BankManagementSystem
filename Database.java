package main;

import java.sql.*;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/bank_system?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "your_mysql_password"; 

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}