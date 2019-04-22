package com.bank.online.service;

import com.bank.online.controller.TransactionController;
import com.bank.online.exception.ResourceNotFoundException;
import com.bank.online.model.Account;
import com.bank.online.model.ModelConverter;
import com.bank.online.model.Transaction;
import com.bank.online.model.TransactionModel;
import com.bank.online.model.TransactionType;
import com.bank.online.repository.AccountRepository;
import com.bank.online.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
@Slf4j
public class AccountTransactionService {

  @Autowired AccountRepository accountRepository;

  @Autowired TransactionRepository transactionRepository;

  @Autowired ModelConverter modelConverter;


  public List<Account> getAllAccounts(Pageable pageable) {

    return accountRepository
        .findAll(pageable)
        .stream()
        .map(
            account -> {
              BigDecimal balance =
                  transactionRepository
                      .findTransactionsByAccount_AccountNo(account.getAccountNo())
                      .stream()
                      .map(
                          t -> {
                            if (t.getTransactionType().equals(TransactionType.DEBIT)) {
                              t.setTransactionAmount(t.getTransactionAmount().negate());
                            }
                            return t.getTransactionAmount();
                          })
                      .reduce(BigDecimal.ZERO, BigDecimal::add);

              account.setAccountBalance(balance);
              account.add(getSelfLink(pageable, account));
              return account;
            })
        .collect(Collectors.toList());
  }

  public Account getAccountByAccountNo(String accountNo) throws ResourceNotFoundException {

    Account account = accountRepository.findByAccountNo(accountNo);
    if (account == null) {
      throw new ResourceNotFoundException("Account does not exist");
    }
    return account;
  }

  public Account saveAccount(Account account) {
    return accountRepository.save(account);
  }

  public Account updateAccount(Long accountId, Account newAccount)
      throws ResourceNotFoundException {
    return accountRepository
        .findByAccountId(accountId)
        .map(
            account -> {
              account = modelConverter.copyAccount(newAccount);
              return accountRepository.save(account);
            })
        .orElseThrow(() -> new ResourceNotFoundException("Account " + accountId + " Not Found"));
  }

  public Account deleteAccount(Long accountId) throws ResourceNotFoundException {
    return accountRepository
        .findByAccountId(accountId)
        .map(
            account -> {
              accountRepository.delete(account);
              return account;
            })
        .orElseThrow(() -> new ResourceNotFoundException("Account Not Found"));
  }

  private Link getSelfLink(Pageable pageable, Account account) {
    return linkTo(
            methodOn(TransactionController.class)
                .getTransactionByAccountNo(account.getAccountNo(), pageable))
        .withSelfRel();
  }

  public Page<TransactionModel> getAllTransactions(Pageable pageable) {
    return transactionRepository
        .findAll(pageable)
        .map(t -> modelConverter.convertToTransactionModel(t));
  }

  public Page<TransactionModel> getTransactionByAccountNo(String accountNo, Pageable pageable) {
    return transactionRepository
        .findTransactionsByAccount_AccountNo(accountNo, pageable)
        .map(p -> p.map(transaction -> modelConverter.convertToTransactionModel(transaction)))
        .orElseThrow(() -> new ResourceNotFoundException("Unable to find the Trxn"));
  }

  public Transaction saveTransaction(@Valid TransactionModel transactionModel) {
    Account account = accountRepository.findByAccountNo(transactionModel.getAccount_no());
    if (account != null) {
      Transaction transaction = modelConverter.convertToTransaction(transactionModel, account);
      return transactionRepository.save(transaction);
    }
    throw new ResourceNotFoundException("Account Not Found");
  }

  public Transaction updateTransaction(Long transactionId, @Valid TransactionModel newTransaction) {
    return transactionRepository
        .findByTransactionId(transactionId)
        .map(
            transaction -> {
              Account account = accountRepository.findByAccountNo(newTransaction.getAccount_no());
              transaction = modelConverter.convertToTransaction(newTransaction, account);
              return transactionRepository.save(transaction);
            })
        .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
  }

  public Transaction deleteTransaction(Long transactionId) {
    return transactionRepository
        .findByTransactionId(transactionId)
        .map(
            transaction -> {
              transactionRepository.delete(transaction);
              return transaction;
            })
        .orElseThrow(() -> new ResourceNotFoundException("No Transaction found"));
  }
}
