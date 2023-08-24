package test.aston.bank.validation;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import test.aston.bank.dto.PersonDto;

@Component
public class PersonValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PersonDto.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pin", "field.required");

        PersonDto personDto = (PersonDto) obj;

        if (personDto.getName().length() > 255) {
            errors.rejectValue("name", "length must be < 255");
        }

        if (personDto.getPin().length() != 4) {
            errors.rejectValue("pin", "length must be equal 4");
        } else {
            if (!NumberUtils.isDigits(personDto.getPin())) {
                errors.rejectValue("pin", "must be digits");
            }
        }

        if (personDto.getScore() == null) errors.rejectValue("score", "field.required");
        if (personDto.getScore() <= 0) errors.rejectValue("score", "amount must be > 0");

    }
}
