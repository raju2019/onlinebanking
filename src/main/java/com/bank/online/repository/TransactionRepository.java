package com.bank.online.repository;

import com.bank.online.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  Optional<Page<Transaction>> findByAccountId(Long accountId, Pageable pageable);

  Optional<Page<Transaction>> findTransactionsByAccount_AccountNo(
      String accountNo, Pageable pageable);

  List<Transaction> findTransactionsByAccount_AccountNo(String accountNo);

  Optional<Transaction> findByTransactionId(Long transactionId);
}
