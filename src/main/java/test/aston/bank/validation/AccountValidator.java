package test.aston.bank.validation;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import test.aston.bank.dto.AccountDto;

@Component
public class AccountValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountDto.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pin", "field.required");

        AccountDto accountDto = (AccountDto) obj;

        if (accountDto.getName().length() > 255) {
            errors.rejectValue("name", "length must be < 255");
        }

        if (accountDto.getPin().length() != 4) {
            errors.rejectValue("pin", "length must be equal 4");
        } else {
            if (!NumberUtils.isDigits(accountDto.getPin())) {
                errors.rejectValue("pin", "must be digits");
            }
        }

    }
}
