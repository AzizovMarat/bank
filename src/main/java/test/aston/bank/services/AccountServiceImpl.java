package test.aston.bank.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.aston.bank.entities.Account;
import test.aston.bank.repositories.AccountRepository;
import test.aston.bank.validation.ValidationService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ValidationService validationService;

//    @Override
//    public Account update(Account account) {
//        return accountRepository.save(account);
//    }

    @Override
    public Account update(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account findByNameAndPin(String name, String pin) {
        return accountRepository.findAccountByNameAndPin(name, pin);
    }

    @Override
    public List<Account> findByName(String name) {
        return accountRepository.findAllByName(name);
    }

    @Override
    public Set<Account> findAll() {
        return accountRepository.findAll();
    }
}