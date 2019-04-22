package com.bank.online.controller;

import com.bank.online.model.Transaction;
import com.bank.online.model.TransactionModel;
import com.bank.online.service.AccountTransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "Transaction")
@RestController
@CrossOrigin
public class TransactionController {

  @Autowired AccountTransactionService accountTransactionService;

  private static final String HAL_MEDIA_TYPE = "application/hal+json";

  @ApiOperation("Get All Transactions")
  @GetMapping(value = "/transactions", produces = HAL_MEDIA_TYPE)
  public Page<TransactionModel> getAllTransactions(Pageable pageable) {
    return accountTransactionService.getAllTransactions(pageable);
  }

  @ApiOperation("Get Transaction details based on AccountNo")
  @GetMapping("/transaction/{accountNo}/accountNo")
  public Page<TransactionModel> getTransactionByAccountNo(
      @PathVariable String accountNo, Pageable pageable) {

    return accountTransactionService.getTransactionByAccountNo(accountNo, pageable);
  }

  @ApiOperation("Create Transaction")
  @PostMapping("/transaction")
  public Transaction postTransactionModel(@RequestBody @Valid TransactionModel transactionModel) {
    return accountTransactionService.saveTransaction(transactionModel);
  }

  @ApiOperation("Update Transaction")
  @PutMapping("/transaction/{transactionId}")
  public Transaction updateTransaction(
      @PathVariable Long transactionId, @RequestBody @Valid TransactionModel newTransaction) {
    return accountTransactionService.updateTransaction(transactionId, newTransaction);
  }

  @ApiOperation("Delete Transaction")
  @DeleteMapping("/transaction/{transactionId}")
  public ResponseEntity<?> deleteTransaction(@PathVariable Long transactionId) {
    accountTransactionService.deleteTransaction(transactionId);
    return ResponseEntity.ok().build();
  }
}
