package com.banking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account {

    public enum AccountType { SAVINGS, CURRENT }

    private Long id;
    private String accountNumber;
    private Long userId;
    private AccountType accountType;
    private BigDecimal balance;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String ownerFullName;

    public Account() {}

    public Account(Long userId, AccountType accountType) {
        this.userId      = userId;
        this.accountType = accountType;
        this.balance     = BigDecimal.ZERO;
        this.active      = true;
    }

    public Long getId()                          { return id; }
    public void setId(Long id)                   { this.id = id; }

    public String getAccountNumber()             { return accountNumber; }
    public void setAccountNumber(String n)       { this.accountNumber = n; }

    public Long getUserId()                      { return userId; }
    public void setUserId(Long userId)           { this.userId = userId; }

    public AccountType getAccountType()          { return accountType; }
    public void setAccountType(AccountType type) { this.accountType = type; }

    public BigDecimal getBalance()               { return balance; }
    public void setBalance(BigDecimal balance)   { this.balance = balance; }

    public boolean isActive()                    { return active; }
    public void setActive(boolean active)        { this.active = active; }

    public LocalDateTime getCreatedAt()                { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt)  { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt()                { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt)  { this.updatedAt = updatedAt; }

    public String getOwnerFullName()             { return ownerFullName; }
    public void setOwnerFullName(String name)    { this.ownerFullName = name; }

    @Override
    public String toString() {
        return String.format("Account{number='%s', type=%s, balance=%.2f}",
                accountNumber, accountType, balance);
    }
}