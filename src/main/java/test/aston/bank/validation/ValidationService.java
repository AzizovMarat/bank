package test.aston.bank.validation;

import test.aston.bank.dto.AccountDto;
import test.aston.bank.dto.PersonDto;
import test.aston.bank.entities.Account;
import test.aston.bank.exceptions.ViolationException;

public interface ValidationService {
    boolean isValid(AccountDto accountDto) throws ViolationException;

    boolean isValid(PersonDto personDto) throws ViolationException;

    boolean isValid(Double amount) throws ViolationException;

    boolean isMoneyEnough(Double amount, Account account) throws ViolationException;
}
