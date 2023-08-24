package test.aston.bank.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import test.aston.bank.entities.Account;

import java.util.List;
import java.util.Set;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    Account findAccountByNameAndPin(String name, String pin);

    List<Account> findAllByName(String name);

    Set<Account> findAll();
}
