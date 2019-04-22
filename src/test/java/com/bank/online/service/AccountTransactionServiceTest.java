package com.bank.online.service;

import com.bank.online.model.Account;
import com.bank.online.model.AccountType;
import com.bank.online.model.CurrencyType;
import com.bank.online.model.ModelConverter;
import com.bank.online.model.Transaction;
import com.bank.online.model.TransactionType;
import com.bank.online.repository.AccountRepository;
import com.bank.online.repository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.bank.online.model.TransactionType.CREDIT;
import static com.bank.online.model.TransactionType.DEBIT;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class AccountTransactionServiceTest {

  @Mock AccountRepository accountRepository;

  @Mock TransactionRepository transactionRepository;

  @Mock ModelConverter modelConverter;

  @Mock Pageable pageable;

  @InjectMocks AccountTransactionService accountTransactionService;

  @Test
  public void saveAccount() {
    Account account = getAccount();
    accountTransactionService.saveAccount(account);
    verify(accountRepository, times(1)).save(account);
  }

  @Test
  public void checkCreditBalance_Accounts() {
    Mockito.when(accountRepository.findAll(pageable)).thenReturn(getAccounts());
    Mockito.when(transactionRepository.findTransactionsByAccount_AccountNo("123"))
        .thenReturn(
            getTransactions(
                new BigDecimal(100),
                CREDIT,
                new BigDecimal(50),
                CREDIT,
                new BigDecimal(20),
                CREDIT));
    List<Account> accountList = accountTransactionService.getAllAccounts(pageable);
    assertThat(accountList.size(), is(1));
    assertEquals(accountList.get(0).getAccountBalance(), new BigDecimal("170"));
  }

  @Test
  public void checkCreditAndDebitBalance_Accounts() {
    Mockito.when(accountRepository.findAll(pageable)).thenReturn(getAccounts());
    Mockito.when(transactionRepository.findTransactionsByAccount_AccountNo("123"))
        .thenReturn(
            getTransactions(
                new BigDecimal(100),
                CREDIT,
                new BigDecimal(50),
                CREDIT,
                new BigDecimal(80),
                DEBIT));
    List<Account> accountList = accountTransactionService.getAllAccounts(pageable);
    assertThat(accountList.size(), is(1));
    assertEquals(accountList.get(0).getAccountBalance(), new BigDecimal("70"));
  }

  @Test
  public void checkCreditBalance_Accounts_WithDecimal() {
    Mockito.when(accountRepository.findAll(pageable)).thenReturn(getAccounts());
    Mockito.when(transactionRepository.findTransactionsByAccount_AccountNo("123"))
        .thenReturn(
            getTransactions(
                new BigDecimal(10.50),
                CREDIT,
                new BigDecimal(20.10),
                CREDIT,
                new BigDecimal(20.00),
                CREDIT));
    List<Account> accountList = accountTransactionService.getAllAccounts(pageable);
    assertThat(accountList.size(), is(1));
    assertEquals(
        accountList.get(0).getAccountBalance().setScale(2, BigDecimal.ROUND_HALF_EVEN),
        new BigDecimal("50.60"));
  }

  @Test
  public void checkCreditAndDebitBalance_Accounts_WithDecimal() {
    Mockito.when(accountRepository.findAll(pageable)).thenReturn(getAccounts());
    Mockito.when(transactionRepository.findTransactionsByAccount_AccountNo("123"))
        .thenReturn(
            getTransactions(
                new BigDecimal(100.90),
                CREDIT,
                new BigDecimal(20.40),
                CREDIT,
                new BigDecimal(24.56),
                DEBIT));
    List<Account> accountList = accountTransactionService.getAllAccounts(pageable);
    assertThat(accountList.size(), is(1));
    assertEquals(
        accountList.get(0).getAccountBalance().setScale(2, BigDecimal.ROUND_HALF_EVEN),
        new BigDecimal("96.74"));
  }

  private Page<Account> getAccounts() {
    ArrayList<Account> accounts = new ArrayList<>();
    Account account1 =
        Account.builder()
            .accountId(1l)
            .accountNo("123")
            .userName("USER1")
            .accountBalance(new BigDecimal(100.00))
            .accountName("TEST_CREDIT_ACCOUNT")
            .accountType(AccountType.CREDIT)
            .build();
    accounts.add(account1);
    Page<Account> accountPage = new PageImpl<>(accounts);
    return accountPage;
  }

  private ArrayList<Transaction> getTransactions(
      BigDecimal trn1,
      TransactionType trn1Type,
      BigDecimal trn2,
      TransactionType trn2Type,
      BigDecimal trn3,
      TransactionType trn3Type) {
    ArrayList<Transaction> transactions = new ArrayList<>();

    Transaction t1 =
        Transaction.builder()
            .account(getAccount())
            .transaction_description("TXN1")
            .transactionAmount(trn1)
            .transactionType(trn1Type)
            .currencyType(CurrencyType.AUD)
            .transactionId(1l)
            .build();
    Transaction t2 =
        Transaction.builder()
            .account(getAccount())
            .transaction_description("TXN2")
            .transactionAmount(trn2)
            .transactionType(trn2Type)
            .currencyType(CurrencyType.AUD)
            .transactionId(2l)
            .build();
    Transaction t3 =
        Transaction.builder()
            .account(getAccount())
            .transaction_description("TXN3")
            .transactionAmount(trn3)
            .transactionType(trn3Type)
            .currencyType(CurrencyType.AUD)
            .transactionId(3l)
            .build();

    transactions.add(t1);
    transactions.add(t2);
    transactions.add(t3);

    return transactions;
  }

  private Account getAccount() {
    return getAccounts().stream().findFirst().get();
  }
}
