package com.bank.online.model;

import org.springframework.stereotype.Component;

/**
 *
 * Component Utility for Model Conversion
 */
@Component
public class ModelConverter {
  public ModelConverter() {}

  public TransactionModel convertToTransactionModel(Transaction transaction) {

    return TransactionModel.builder()
        .id(transaction.getTransactionId())
        .account_id(transaction.getAccount().getAccountId())
        .account_no(transaction.getAccount().getAccountNo())
        .transaction_description(transaction.getTransaction_description())
        .transactionAmount(transaction.getTransactionAmount())
        .transactionType(transaction.getTransactionType())
        .currencyType(transaction.getCurrencyType())
        .user_name(transaction.getAccount().getUserName())
        .build();
  }

  public Transaction convertToTransaction(TransactionModel transactionModel, Account account) {

    return Transaction.builder()
        .transaction_description(transactionModel.getTransaction_description())
        .transactionAmount(transactionModel.getTransactionAmount())
        .transactionType(transactionModel.getTransactionType())
        .currencyType(transactionModel.getCurrencyType())
        .account(account)
        .build();
  }

  public Account copyAccount(Account newAccount) {

    return Account.builder()
        .userName(newAccount.getUserName())
        .accountNo(newAccount.getAccountNo())
        .accountType(newAccount.getAccountType())
        .accountBalance(newAccount.getAccountBalance())
        .accountName(newAccount.getAccountName())
        .build();
  }
}
