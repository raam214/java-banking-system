package com.banking.cli;

import com.banking.exception.AccountNotFoundException;
import com.banking.exception.AuthenticationException;
import com.banking.exception.InsufficientFundsException;
import com.banking.exception.ValidationException;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.User;
import com.banking.service.AccountService;
import com.banking.service.AuthService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class BankingCLI {

    private final AuthService authService;
    private final AccountService accountService;
    private final Scanner scanner;

    public BankingCLI() {
        this.authService    = new AuthService();
        this.accountService = new AccountService();
        this.scanner        = new Scanner(System.in);
    }

    public void start() {
        printBanner();
        boolean running = true;
        while (running) {
            if (!authService.isLoggedIn()) {
                printMainMenu();
                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1" -> register();
                    case "2" -> login();
                    case "0" -> { running = false; print("Goodbye!"); }
                    default  -> print("Invalid option. Try again.");
                }
            } else {
                printBankingMenu();
                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1" -> createAccount();
                    case "2" -> deposit();
                    case "3" -> withdraw();
                    case "4" -> transfer();
                    case "5" -> viewStatement();
                    case "6" -> viewMyAccounts();
                    case "0" -> { authService.logout(); print("Logged out successfully."); }
                    default  -> print("Invalid option. Try again.");
                }
            }
        }
        scanner.close();
    }

    private void register() {
        print("\n--- REGISTER ---");
        System.out.print("Full Name     : "); String name = scanner.nextLine().trim();
        System.out.print("Username      : "); String username = scanner.nextLine().trim();
        System.out.print("Email         : "); String email = scanner.nextLine().trim();
        System.out.print("Password      : "); String password = scanner.nextLine().trim();
        try {
            User user = authService.register(username, password, name, email);
            print("Registration successful! Welcome, " + user.getFullName());
        } catch (ValidationException e) {
            print("Validation Error: " + e.getMessage());
        } catch (SQLException e) {
            print("Database Error: " + e.getMessage());
        }
    }

    private void login() {
        print("\n--- LOGIN ---");
        System.out.print("Username : "); String username = scanner.nextLine().trim();
        System.out.print("Password : "); String password = scanner.nextLine().trim();
        try {
            User user = authService.login(username, password);
            print("Login successful! Welcome back, " + user.getFullName());
        } catch (AuthenticationException e) {
            print("Login Failed: " + e.getMessage());
        } catch (SQLException e) {
            print("Database Error: " + e.getMessage());
        }
    }

    private void createAccount() {
        print("\n--- CREATE ACCOUNT ---");
        print("Account Type: 1. SAVINGS  2. CURRENT");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();
        Account.AccountType type = choice.equals("1")
                ? Account.AccountType.SAVINGS
                : Account.AccountType.CURRENT;
        try {
            Account account = accountService.createAccount(
                    authService.getLoggedInUser().getId(), type);
            print("Account created successfully!");
            print("Account Number : " + account.getAccountNumber());
            print("Account Type   : " + account.getAccountType());
            print("Balance        : Rs. " + account.getBalance());
        } catch (SQLException e) {
            print("Database Error: " + e.getMessage());
        }
    }

    private void deposit() {
        print("\n--- DEPOSIT ---");
        System.out.print("Account Number : "); String accNum = scanner.nextLine().trim();
        System.out.print("Amount         : "); String amt = scanner.nextLine().trim();
        try {
            Transaction tx = accountService.deposit(accNum, new BigDecimal(amt));
            print("Deposit successful!");
            print("Reference      : " + tx.getReferenceNumber());
            print("Amount         : Rs. " + tx.getAmount());
            print("Balance After  : Rs. " + tx.getBalanceAfter());
        } catch (AccountNotFoundException | ValidationException e) {
            print("Error: " + e.getMessage());
        } catch (SQLException e) {
            print("Database Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            print("Invalid amount entered.");
        }
    }

    private void withdraw() {
        print("\n--- WITHDRAW ---");
        System.out.print("Account Number : "); String accNum = scanner.nextLine().trim();
        System.out.print("Amount         : "); String amt = scanner.nextLine().trim();
        try {
            Transaction tx = accountService.withdraw(accNum, new BigDecimal(amt));
            print("Withdrawal successful!");
            print("Reference      : " + tx.getReferenceNumber());
            print("Amount         : Rs. " + tx.getAmount());
            print("Balance After  : Rs. " + tx.getBalanceAfter());
        } catch (AccountNotFoundException | ValidationException | InsufficientFundsException e) {
            print("Error: " + e.getMessage());
        } catch (SQLException e) {
            print("Database Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            print("Invalid amount entered.");
        }
    }

    private void transfer() {
        print("\n--- TRANSFER ---");
        System.out.print("From Account : "); String from = scanner.nextLine().trim();
        System.out.print("To Account   : "); String to = scanner.nextLine().trim();
        System.out.print("Amount       : "); String amt = scanner.nextLine().trim();
        try {
            accountService.transfer(from, to, new BigDecimal(amt));
            print("Transfer successful!");
        } catch (AccountNotFoundException | ValidationException | InsufficientFundsException e) {
            print("Error: " + e.getMessage());
        } catch (SQLException e) {
            print("Database Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            print("Invalid amount entered.");
        }
    }

    private void viewStatement() {
        print("\n--- ACCOUNT STATEMENT ---");
        System.out.print("Account Number : "); String accNum = scanner.nextLine().trim();
        try {
            List<Transaction> txList = accountService.getStatement(accNum);
            if (txList.isEmpty()) {
                print("No transactions found.");
                return;
            }
            print(String.format("%-12s %-15s %-12s %-12s %s",
                    "Ref", "Type", "Amount", "Balance", "Date"));
            print("-".repeat(75));
            for (Transaction tx : txList) {
                print(String.format("%-12s %-15s %-12s %-12s %s",
                        tx.getReferenceNumber(),
                        tx.getTransactionType(),
                        tx.getAmount(),
                        tx.getBalanceAfter(),
                        tx.getCreatedAt().toLocalDate()));
            }
        } catch (AccountNotFoundException e) {
            print("Error: " + e.getMessage());
        } catch (SQLException e) {
            print("Database Error: " + e.getMessage());
        }
    }

    private void viewMyAccounts() {
        try {
            List<Account> accounts = accountService.getUserAccounts(
                    authService.getLoggedInUser().getId());
            if (accounts.isEmpty()) {
                print("No accounts found. Create one first.");
                return;
            }
            print("\n--- MY ACCOUNTS ---");
            for (Account acc : accounts) {
                print("Account Number : " + acc.getAccountNumber());
                print("Type           : " + acc.getAccountType());
                print("Balance        : Rs. " + acc.getBalance());
                print("-".repeat(40));
            }
        } catch (SQLException e) {
            print("Database Error: " + e.getMessage());
        }
    }

    private void printBanner() {
        print("╔══════════════════════════════════════╗");
        print("║     JAVA CONSOLE BANKING SYSTEM      ║");
        print("║         Built by Ram Dukare          ║");
        print("╚══════════════════════════════════════╝");
    }

    private void printMainMenu() {
        print("\n========== MAIN MENU ==========");
        print("1. Register");
        print("2. Login");
        print("0. Exit");
        System.out.print("Enter choice: ");
    }

    private void printBankingMenu() {
        User u = authService.getLoggedInUser();
        print("\n========== BANKING MENU [ " + u.getUsername() + " ] ==========");
        print("1. Create Account");
        print("2. Deposit");
        print("3. Withdraw");
        print("4. Transfer");
        print("5. View Statement");
        print("6. View My Accounts");
        print("0. Logout");
        System.out.print("Enter choice: ");
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}