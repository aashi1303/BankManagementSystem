package main;

import java.sql.*;
import java.util.Scanner;

public class TransactionService {
    public static void recordTransaction(String acc, String type, double amount) {
        String sql = "INSERT INTO transactions (account_number, type, amount) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, acc);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewTransactions(Scanner sc) {
        System.out.print("Enter account number: ");
        String acc = sc.nextLine();
        String sql = "SELECT * FROM transactions WHERE account_number = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, acc);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.printf("[%s] %s: %.2f\n", rs.getTimestamp("timestamp"), rs.getString("type"), rs.getDouble("amount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}