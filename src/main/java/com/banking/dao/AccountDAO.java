package com.banking.dao;

import com.banking.model.Account;
import com.banking.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDAO {

    private static final Logger logger = LoggerFactory.getLogger(AccountDAO.class);

    public Account save(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (account_number, user_id, account_type, balance) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, account.getAccountNumber());
            ps.setLong(2, account.getUserId());
            ps.setString(3, account.getAccountType().name());
            ps.setBigDecimal(4, account.getBalance());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) account.setId(rs.getLong(1));
            }
            logger.info("Account created: {}", account.getAccountNumber());
            return account;
        }
    }

    public Optional<Account> findByAccountNumber(String accountNumber) throws SQLException {
        String sql = "SELECT a.*, u.full_name FROM accounts a JOIN users u ON a.user_id = u.id WHERE a.account_number = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    public List<Account> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT a.*, u.full_name FROM accounts a JOIN users u ON a.user_id = u.id WHERE a.user_id = ?";
        List<Account> accounts = new ArrayList<>();

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) accounts.add(mapRow(rs));
            }
        }
        return accounts;
    }

    public void updateBalance(Long accountId, java.math.BigDecimal newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, newBalance);
            ps.setLong(2, accountId);
            ps.executeUpdate();
        }
    }

    private Account mapRow(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setId(rs.getLong("id"));
        account.setAccountNumber(rs.getString("account_number"));
        account.setUserId(rs.getLong("user_id"));
        account.setAccountType(Account.AccountType.valueOf(rs.getString("account_type")));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setActive(rs.getBoolean("is_active"));
        account.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        account.setOwnerFullName(rs.getString("full_name"));
        return account;
    }
}