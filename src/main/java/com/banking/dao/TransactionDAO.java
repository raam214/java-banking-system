package com.banking.dao;

import com.banking.model.Transaction;
import com.banking.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public Transaction save(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (account_id, transaction_type, amount, balance_after, reference_number, description, related_account_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, transaction.getAccountId());
            ps.setString(2, transaction.getTransactionType().name());
            ps.setBigDecimal(3, transaction.getAmount());
            ps.setBigDecimal(4, transaction.getBalanceAfter());
            ps.setString(5, transaction.getReferenceNumber());
            ps.setString(6, transaction.getDescription());
            if (transaction.getRelatedAccountId() != null)
                ps.setLong(7, transaction.getRelatedAccountId());
            else
                ps.setNull(7, Types.BIGINT);

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) transaction.setId(rs.getLong(1));
            }
            return transaction;
        }
    }

    public List<Transaction> findByAccountId(Long accountId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY created_at DESC LIMIT 20";
        List<Transaction> list = new ArrayList<>();

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setId(rs.getLong("id"));
        t.setAccountId(rs.getLong("account_id"));
        t.setTransactionType(Transaction.TransactionType.valueOf(rs.getString("transaction_type")));
        t.setAmount(rs.getBigDecimal("amount"));
        t.setBalanceAfter(rs.getBigDecimal("balance_after"));
        t.setReferenceNumber(rs.getString("reference_number"));
        t.setDescription(rs.getString("description"));
        t.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return t;
    }
}