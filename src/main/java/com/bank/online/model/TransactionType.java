package com.bank.online.model;

/**
 *
 * Enum of Transaction Types
 */
public enum TransactionType {
  CREDIT("CREDIT"),
  DEBIT("DEBIT");

  private String transactionType;

  TransactionType(String transactionType) {
    this.transactionType = transactionType;
  }

  public String getName() {
    return transactionType;
  }
}
