package com.bank.online.repository;

import com.bank.online.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

  Account findByAccountNo(String accountNo);

  Optional<Account> findByAccountId(Long accountId);
}
