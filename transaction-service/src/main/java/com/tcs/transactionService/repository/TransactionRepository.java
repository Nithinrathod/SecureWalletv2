package com.tcs.transactionService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcs.transactionService.bean.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySenderUserId(String senderUserId);

    List<Transaction> findByReceiverUserId(String receiverUserId);

    List<Transaction> findByStatus(String status);
}
