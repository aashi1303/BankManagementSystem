package main;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AuthService authService = new AuthService();
        BankService bankService = new BankService();
        int userId = -1;

        System.out.println("=== Welcome to Bank Management System ===");

        while (userId == -1) {
            System.out.println("\n1. Register\n2. Login");
            System.out.print("Choose: ");
            int opt = sc.nextInt();
            sc.nextLine();
            if (opt == 1) userId = authService.register(sc);
            else if (opt == 2) userId = authService.login(sc);
        }

        int choice;
        do {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Account");
            System.out.println("5. Transaction History");
            System.out.println("6. Logout");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> bankService.createAccount(userId, sc);
                case 2 -> bankService.deposit(userId, sc);
                case 3 -> bankService.withdraw(userId, sc);
                case 4 -> bankService.viewAccount(userId, sc);
                case 5 -> TransactionService.viewTransactions(sc);
                case 6 -> System.out.println("Logged out.");
                default -> System.out.println("Invalid choice.");
            }

        } while (choice != 6);
        sc.close();
    }
}