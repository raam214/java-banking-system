package com.banking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    public enum TransactionType { DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT }

    private Long id;
    private Long accountId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String referenceNumber;
    private String description;
    private Long relatedAccountId;
    private LocalDateTime createdAt;

    public Transaction() {}

    public Transaction(Long accountId, TransactionType type,
                       BigDecimal amount, BigDecimal balanceAfter,
                       String referenceNumber, String description) {
        this.accountId       = accountId;
        this.transactionType = type;
        this.amount          = amount;
        this.balanceAfter    = balanceAfter;
        this.referenceNumber = referenceNumber;
        this.description     = description;
    }

    public Long getId()                                { return id; }
    public void setId(Long id)                         { this.id = id; }

    public Long getAccountId()                         { return accountId; }
    public void setAccountId(Long accountId)           { this.accountId = accountId; }

    public TransactionType getTransactionType()        { return transactionType; }
    public void setTransactionType(TransactionType t)  { this.transactionType = t; }

    public BigDecimal getAmount()                      { return amount; }
    public void setAmount(BigDecimal amount)           { this.amount = amount; }

    public BigDecimal getBalanceAfter()                { return balanceAfter; }
    public void setBalanceAfter(BigDecimal b)          { this.balanceAfter = b; }

    public String getReferenceNumber()                 { return referenceNumber; }
    public void setReferenceNumber(String ref)         { this.referenceNumber = ref; }

    public String getDescription()                     { return description; }
    public void setDescription(String description)     { this.description = description; }

    public Long getRelatedAccountId()                  { return relatedAccountId; }
    public void setRelatedAccountId(Long id)           { this.relatedAccountId = id; }

    public LocalDateTime getCreatedAt()                { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt)  { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format("Transaction{ref='%s', type=%s, amount=%.2f, balanceAfter=%.2f}",
                referenceNumber, transactionType, amount, balanceAfter);
    }
}