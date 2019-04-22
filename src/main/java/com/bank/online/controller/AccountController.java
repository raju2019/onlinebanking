package com.bank.online.controller;

import com.bank.online.model.Account;
import com.bank.online.service.AccountTransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = {"Account"})
@RestController
public class AccountController {

  @Autowired AccountTransactionService accountService;

  private static final String HAL_MEDIA_TYPE = "application/hal+json";

  @GetMapping(value = "/accounts", produces = HAL_MEDIA_TYPE)
  @ApiOperation(value = "Get all Accounts and their transactions in Paged format with HATEOS")
  public Page<Account> getAllAccounts(Pageable pageable) {
    List<Account> accounts = accountService.getAllAccounts(pageable);
    return new PageImpl<>(accounts);
  }

  @ApiOperation("Get Accountdetails based on AccountNo")
  @GetMapping("/account/{accountNo}")
  public Account getAccountByAccountNo(@PathVariable @NotNull @Valid String accountNo) {
    return accountService.getAccountByAccountNo(accountNo);
  }

  @ApiOperation("Create Account")
  @PostMapping("/account")
  public Account createAccount(@Valid @RequestBody Account account) {
    return accountService.saveAccount(account);
  }

  @ApiOperation("Update Account")
  @PutMapping("/account/{accountId}")
  public Account updateAccount(
      @PathVariable @NotNull Long accountId, @Valid @RequestBody Account newAccount) {
    return accountService.updateAccount(accountId, newAccount);
  }

  @ApiOperation("Delete Account")
  @DeleteMapping("/account/{accountId}")
  public ResponseEntity<?> deleteAccount(@PathVariable @NotNull Long accountId) {
    accountService.deleteAccount(accountId);
    return ResponseEntity.ok().build();
  }
}
