package test.aston.bank.services;

import test.aston.bank.entities.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction save(Transaction transaction);

    List<Transaction> findByAccountId(Long id);

    List<Transaction> findByRecipientId(Long id);
}
