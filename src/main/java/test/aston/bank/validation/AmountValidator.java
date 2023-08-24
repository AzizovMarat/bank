package test.aston.bank.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import test.aston.bank.dto.DepositPayload;

public class AmountValidator implements Validator {

    @Autowired
    private ValidationService validationService;

    @Override
    public boolean supports(Class<?> clazz) {
        return DepositPayload.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {

        DepositPayload depositPayload = (DepositPayload) obj;


    }
}
