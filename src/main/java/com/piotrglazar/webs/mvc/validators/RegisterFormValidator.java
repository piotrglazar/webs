package com.piotrglazar.webs.mvc.validators;

import com.piotrglazar.webs.ValidatorChain;
import com.piotrglazar.webs.mvc.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegisterFormValidator implements Validator {

    private final ValidatorChain<RegisterForm, Errors> validatorChain;

    @Autowired
    public RegisterFormValidator(final ValidatorChain<RegisterForm, Errors> validatorChain) {
        this.validatorChain = validatorChain;
    }

    @Override
    public boolean supports(final Class<?> clazz) {
        return RegisterForm.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        validatorChain.validateAll((RegisterForm) target, errors);
    }
}
