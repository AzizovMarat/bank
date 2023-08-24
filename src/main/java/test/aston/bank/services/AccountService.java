package test.aston.bank.services;

import test.aston.bank.entities.Account;

import java.util.List;
import java.util.Set;

public interface AccountService {
    Account update(Account account);

    Account findByNameAndPin(String name, String pin);

    List<Account> findByName(String name);

    Set<Account> findAll();
}
