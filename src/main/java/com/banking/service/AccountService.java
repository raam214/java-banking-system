package com.banking.service;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.exception.AccountNotFoundException;
import com.banking.exception.InsufficientFundsException;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.util.AccountNumberGenerator;
import com.banking.util.InputValidator;
import com.banking.exception.ValidationException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AccountService {

    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public AccountService() {
        this.accountDAO      = new AccountDAO();
        this.transactionDAO  = new TransactionDAO();
    }

    public Account createAccount(Long userId, Account.AccountType type) throws SQLException {
        Account account = new Account(userId, type);
        account.setAccountNumber(AccountNumberGenerator.generate());
        return accountDAO.save(account);
    }

    public Account getAccount(String accountNumber)
            throws SQLException, AccountNotFoundException {

        Optional<Account> opt = accountDAO.findByAccountNumber(accountNumber);
        if (opt.isEmpty())
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        return opt.get();
    }

    public List<Account> getUserAccounts(Long userId) throws SQLException {
        return accountDAO.findByUserId(userId);
    }

    public Transaction deposit(String accountNumber, BigDecimal amount)
            throws SQLException, AccountNotFoundException {

        if (!InputValidator.isValidAmount(amount))
            throw new ValidationException("Amount must be greater than zero");

        Account account = getAccount(accountNumber);
        BigDecimal newBalance = account.getBalance().add(amount);
        accountDAO.updateBalance(account.getId(), newBalance);

        Transaction tx = new Transaction(
                account.getId(),
                Transaction.TransactionType.DEPOSIT,
                amount, newBalance,
                generateRef(),
                "Deposit to " + accountNumber
        );
        return transactionDAO.save(tx);
    }

    public Transaction withdraw(String accountNumber, BigDecimal amount)
            throws SQLException, AccountNotFoundException, InsufficientFundsException {

        if (!InputValidator.isValidAmount(amount))
            throw new ValidationException("Amount must be greater than zero");

        Account account = getAccount(accountNumber);

        if (account.getBalance().compareTo(amount) < 0)
            throw new InsufficientFundsException(
                    "Insufficient funds. Available: " + account.getBalance());

        BigDecimal newBalance = account.getBalance().subtract(amount);
        accountDAO.updateBalance(account.getId(), newBalance);

        Transaction tx = new Transaction(
                account.getId(),
                Transaction.TransactionType.WITHDRAWAL,
                amount, newBalance,
                generateRef(),
                "Withdrawal from " + accountNumber
        );
        return transactionDAO.save(tx);
    }

    public void transfer(String fromNumber, String toNumber, BigDecimal amount)
            throws SQLException, AccountNotFoundException, InsufficientFundsException {

        if (!InputValidator.isValidAmount(amount))
            throw new ValidationException("Amount must be greater than zero");

        Account from = getAccount(fromNumber);
        Account to   = getAccount(toNumber);

        if (from.getBalance().compareTo(amount) < 0)
            throw new InsufficientFundsException(
                    "Insufficient funds. Available: " + from.getBalance());

        BigDecimal fromBalance = from.getBalance().subtract(amount);
        BigDecimal toBalance   = to.getBalance().add(amount);

        accountDAO.updateBalance(from.getId(), fromBalance);
        accountDAO.updateBalance(to.getId(), toBalance);

        String ref = generateRef();

        Transaction debit = new Transaction(
                from.getId(), Transaction.TransactionType.TRANSFER_OUT,
                amount, fromBalance, ref, "Transfer to " + toNumber);
        debit.setRelatedAccountId(to.getId());

        Transaction credit = new Transaction(
                to.getId(), Transaction.TransactionType.TRANSFER_IN,
                amount, toBalance, generateRef(), "Transfer from " + fromNumber);
        credit.setRelatedAccountId(from.getId());

        transactionDAO.save(debit);
        transactionDAO.save(credit);
    }

    public List<Transaction> getStatement(String accountNumber)
            throws SQLException, AccountNotFoundException {
        Account account = getAccount(accountNumber);
        return transactionDAO.findByAccountId(account.getId());
    }

    private String generateRef() {
        return "REF" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}