package com.bank.online.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ModelConverterTest {

  @InjectMocks ModelConverter modelConverter;

  @Test
  public void convertToTransactionModel() {

    ModelConverter modelConverter = new ModelConverter();

    Account account =
        new Account()
            .builder()
            .accountBalance(new BigDecimal(100))
            .accountName("ACCONE")
            .accountNo("1")
            .accountType(AccountType.CREDIT)
            .accountId(123l)
            .userName("USER1")
            .build();

    Transaction transaction =
        new Transaction()
            .builder()
            .transactionAmount(new BigDecimal(100))
            .transaction_description("TEST")
            .transactionId(1l)
            .transactionType(TransactionType.CREDIT)
            .account(account)
            .build();

    TransactionModel transactionModel = modelConverter.convertToTransactionModel(transaction);
    assertThat(transactionModel.getTransactionAmount(), is(new BigDecimal(100)));
    assertThat(transactionModel.getTransaction_description(), is("TEST"));
    assertThat(transactionModel.getId(), is(1l));
    assertThat(transactionModel.getTransactionType(), is(TransactionType.CREDIT));
    assertThat(transactionModel.getAccount_id(), is(123l));
  }

  @Test
  public void convertToTransaction() {

    Account account =
        new Account()
            .builder()
            .accountBalance(new BigDecimal(100))
            .accountName("ACCONE")
            .accountNo("1234")
            .accountType(AccountType.CREDIT)
            .accountId(123l)
            .userName("USER1")
            .build();

    TransactionModel transactionModel =
        new TransactionModel()
            .builder()
            .transaction_description("TXN1")
            .transactionAmount(new BigDecimal(123))
            .build();

    ModelConverter modelConverter = new ModelConverter();

    Transaction t = modelConverter.convertToTransaction(transactionModel, account);

    assertThat(t.getAccount().getAccountNo(), is("1234"));
    assertThat(t.getTransactionAmount(), is(new BigDecimal("123")));
    assertThat(t.getTransaction_description(), is("TXN1"));
  }

  @Test
  public void copyAccount() {

    Account account = new Account().builder().userName("USER1").accountNo("123").build();

    ModelConverter modelConverter = new ModelConverter();
    Account copiedAccount = modelConverter.copyAccount(account);

    assertThat(copiedAccount.getAccountNo(), is("123"));
  }
}
