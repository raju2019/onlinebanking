package com.bank.online.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 *
 * Domain Model of Transaction
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionModel {

  private Long id;

  private String transaction_description;

  private Long account_id;

  private String account_no;

  private String user_name;

  private BigDecimal transactionAmount;

  private TransactionType transactionType;

  private CurrencyType currencyType;
}
