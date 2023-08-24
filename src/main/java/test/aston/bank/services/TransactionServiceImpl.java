package test.aston.bank.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.aston.bank.entities.Transaction;
import test.aston.bank.repositories.TransactionRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    @Override
    public Transaction save(Transaction transaction) {
        return repository.save(transaction);
    }

    @Override
    public List<Transaction> findByAccountId(Long id) {
        return repository.findByAccountId(id);
    }

    @Override
    public List<Transaction> findByRecipientId(Long id) {
        return repository.findByRecipientId(id);
    }
}
