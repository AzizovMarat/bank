package test.aston.bank.services;

import org.springframework.stereotype.Component;
import test.aston.bank.dto.AccountDto;
import test.aston.bank.dto.PersonDto;
import test.aston.bank.entities.Account;

@Component
public class DtoToEntity {
    public Account dtoAccountToEntityAccount(AccountDto accountDto) {
        Account account = new Account();
        account.setName(accountDto.getName());
        account.setPin(accountDto.getPin());
        account.setScore(0.0);
        return account;
    }

    public PersonDto entityAccountToPersonDto(Account account) {
        PersonDto personDto = new PersonDto();
        personDto.setName(account.getName());
        personDto.setPin(account.getPin());
        personDto.setScore(account.getScore());
        return personDto;
    }

    public AccountDto entityAccountToAccountDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setName(account.getName());
        accountDto.setPin(account.getPin());
        return accountDto;
    }
}
