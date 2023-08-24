package test.aston.bank.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import test.aston.bank.dto.AccountDto;
import test.aston.bank.dto.PersonDto;
import test.aston.bank.dto.Violation;
import test.aston.bank.entities.Account;
import test.aston.bank.exceptions.ViolationException;

import java.util.Objects;


@Service
public class ValidationServiceImpl implements ValidationService {

    @Autowired
    @Qualifier("accountValidator")
    private Validator accountValidator;
    @Autowired
    @Qualifier("personValidator")
    private Validator personValidator;

//    public boolean isValidEmployee(PersonDto accountDto) throws ValidationException {
//        Set<ConstraintViolation<PersonDto>> constraintViolations = validator.validate(accountDto);
//
//        if (CollectionUtils.isNotEmpty(constraintViolations)) {
//            throw new ValidationException(buildViolationsList(constraintViolations));
//        }
//        return true;
//    }
//
//    private <T> List<Violation> buildViolationsList(Set<Constraintviolation<T>> constraintViolations) {
//        return constraintViolations.stream()
//                .map(violation -> new Violation(
//                                violation.getPropertyPath().toString(),
//                                violation.getMessage()
//                        )
//                )
//                .toList();
//    }

    @Override
    public boolean isValid(AccountDto accountDto) throws ViolationException {
        final DataBinder dataBinder = new DataBinder(accountDto);
        dataBinder.addValidators(accountValidator);
        dataBinder.validate();

        if (dataBinder.getBindingResult().hasErrors()) {
            FieldError error = (FieldError) dataBinder.getBindingResult().getAllErrors().get(0);
            Violation violation = new Violation(error.getField(), Objects.requireNonNull(error.getCodes())[error.getCodes().length - 1]);
            throw new ViolationException(violation);
        }

        return true;
    }

    @Override
    public boolean isValid(PersonDto personDto) throws ViolationException {
        final DataBinder dataBinder = new DataBinder(personDto);
        dataBinder.addValidators(personValidator);
        dataBinder.validate();

        if (dataBinder.getBindingResult().hasErrors()) {
            FieldError error = (FieldError) dataBinder.getBindingResult().getAllErrors().get(0);
            Violation violation = new Violation(error.getField(), Objects.requireNonNull(error.getCodes())[error.getCodes().length - 1]);
            throw new ViolationException(violation);
        }

        return true;
    }

    @Override
    public boolean isValid(Double amount) throws ViolationException {
        if (amount == null) throw new ViolationException(new Violation("amount", "field.required"));
        if (amount <= 0) throw new ViolationException(new Violation("amount", "amount must be > 0"));

        return true;
    }

    @Override
    public boolean isMoneyEnough(Double amount, Account account) throws ViolationException {
        if (account.getScore() - amount < 0) throw new ViolationException(new Violation("amount", "amount must be > score"));

        return true;
    }
}
