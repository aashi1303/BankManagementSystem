package main;

import java.sql.*;
import java.util.Scanner;
import utils.HashUtil;

public class AuthService {
    public int register(Scanner sc) {
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        String hashed = HashUtil.hash(password);

        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, hashed);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("Registration successful!");
                return rs.getInt(1);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Username already taken.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int login(Scanner sc) {
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        String hashed = HashUtil.hash(password);

        String sql = "SELECT id FROM users WHERE username = ? AND password = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hashed);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Login successful.");
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Invalid credentials.");
        return -1;
    }
}