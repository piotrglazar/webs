package com.piotrglazar.webs.mvc.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.stream.Stream;

@Component
public class PasswordFieldsValidator {

    public void validatePasswords(String password, String repeatedPassword, Errors errors, String formName, String... fieldNames)  {
        if (!password.equals(repeatedPassword)) {
            Stream.of(fieldNames).forEach(fieldName -> {
                errors.rejectValue(fieldName, String.format("%s.%s", formName, fieldName), "passwords must match");
            });
        }
    }
}
