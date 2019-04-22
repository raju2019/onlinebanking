package com.bank.online.controller;

import com.bank.online.model.Account;
import com.bank.online.model.AccountType;
import com.bank.online.service.AccountTransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountControllerTest {

  private MockMvc mockMvc;

  @Autowired WebApplicationContext webApplicationContext;

  @MockBean AccountTransactionService accountTransactionService;

  @Autowired private ObjectMapper mapper;

  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void getAllAccounts_statusCheck() throws Exception {

    mockMvc
        .perform(get("/accounts").contentType(MediaType.APPLICATION_JSON).accept(MediaType.ALL))
        .andExpect(status().isOk());
  }

  @Test
  public void getAllAccounts() throws Exception {

    Mockito.when(accountTransactionService.getAllAccounts(Mockito.any())).thenReturn(getAccounts());

    mockMvc
        .perform(get("/accounts").contentType(MediaType.APPLICATION_JSON).accept(MediaType.ALL))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].accountId", is(1)))
        .andExpect(jsonPath("$.content[0].accountNo", is("123")))
        .andExpect(jsonPath("$.content[0].userName", is("USER1")))
        .andExpect(jsonPath("$.content[0].accountName", is("TEST_CREDIT_ACCOUNT")))
        .andExpect(jsonPath("$.content[0].accountType", is(AccountType.CREDIT.name())))
        .andExpect(jsonPath("$.content[0].accountBalance", is(100)));
  }

  @Test
  public void getAccountByAccountNo() throws Exception {

    Mockito.when(accountTransactionService.getAccountByAccountNo("2")).thenReturn(getAccount());

    mockMvc
        .perform(
            get("/account/2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accountId", is(1)))
        .andExpect(jsonPath("$.accountNo", is("123")))
        .andExpect(jsonPath("$.userName", is("USER1")))
        .andExpect(jsonPath("$.accountName", is("TEST_CREDIT_ACCOUNT")))
        .andExpect(jsonPath("$.accountType", is(AccountType.CREDIT.name())))
        .andExpect(jsonPath("$.accountBalance", is(100)));

    verify(accountTransactionService, times(1)).getAccountByAccountNo(eq("2"));
  }

  @Test
  public void createAccount() throws Exception {

    String accountJSON = mapper.writeValueAsString(getAccount());

    mockMvc
        .perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(accountJSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(accountTransactionService, times(1)).saveAccount(getAccount());
  }

  @Test
  public void updateAccount() throws Exception {

    String accountJSON = mapper.writeValueAsString(getAccount());

    mockMvc
        .perform(
            put("/account/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(accountJSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(accountTransactionService, times(1)).updateAccount(1l, getAccount());
  }

  @Test
  public void deleteAccount() throws Exception {

    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/account/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(accountTransactionService, times(1)).deleteAccount(1l);
  }

  private ArrayList<Account> getAccounts() {
    ArrayList<Account> accounts = new ArrayList<>();
    Account account1 =
        Account.builder()
            .accountId(1l)
            .accountNo("123")
            .userName("USER1")
            .accountBalance(new BigDecimal(100))
            .accountName("TEST_CREDIT_ACCOUNT")
            .accountType(AccountType.CREDIT)
            .build();
    accounts.add(account1);

    return accounts;
  }

  private Account getAccount() {
    return getAccounts().stream().findFirst().get();
  }
}
