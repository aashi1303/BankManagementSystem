package main;

import java.sql.*;
import java.util.Scanner;

public class BankService {
    public void createAccount(int userId, Scanner sc) {
        System.out.print("Account name: ");
        String name = sc.nextLine();
        System.out.print("Account number: ");
        String number = sc.nextLine();

        String sql = "INSERT INTO accounts (name, account_number, user_id) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, number);
            ps.setInt(3, userId);
            ps.executeUpdate();
            System.out.println("Account created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deposit(int userId, Scanner sc) {
        System.out.print("Account number: ");
        String number = sc.nextLine();
        System.out.print("Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();

        String sql = "UPDATE accounts SET balance = balance + ? WHERE account_number = ? AND user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setString(2, number);
            ps.setInt(3, userId);
            if (ps.executeUpdate() > 0) {
                TransactionService.recordTransaction(number, "DEPOSIT", amount);
                System.out.println("Deposit successful.");
            } else {
                System.out.println("Account not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void withdraw(int userId, Scanner sc) {
        System.out.print("Account number: ");
        String number = sc.nextLine();
        System.out.print("Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();

        String checkSql = "SELECT balance FROM accounts WHERE account_number = ? AND user_id = ?";
        String updateSql = "UPDATE accounts SET balance = balance - ? WHERE account_number = ? AND user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, number);
            checkStmt.setInt(2, userId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getDouble("balance") >= amount) {
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setDouble(1, amount);
                    updateStmt.setString(2, number);
                    updateStmt.setInt(3, userId);
                    updateStmt.executeUpdate();
                    TransactionService.recordTransaction(number, "WITHDRAW", amount);
                    System.out.println("Withdrawal successful.");
                }
            } else {
                System.out.println("Insufficient balance or account not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewAccount(int userId, Scanner sc) {
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.printf("Account: %s | Balance: %.2f\n",
                        rs.getString("account_number"), rs.getDouble("balance"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}