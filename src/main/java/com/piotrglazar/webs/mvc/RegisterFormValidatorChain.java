package com.piotrglazar.webs.mvc;

import com.google.common.collect.ImmutableList;
import com.piotrglazar.webs.Validator;
import com.piotrglazar.webs.ValidatorChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.List;

@Component
class RegisterFormValidatorChain implements ValidatorChain<RegisterForm, Errors>{

    private final List<RegisterFormFieldValidator> validators;

    @Autowired
    RegisterFormValidatorChain(final List<RegisterFormFieldValidator> validators) {
        this.validators = ImmutableList.copyOf(validators);
    }


    @Override
    public void validateAll(final RegisterForm form, final Errors errors) {
        for (final RegisterFormFieldValidator validator : validators) {
            validator.validate(form, errors);
        }
    }

    @Override
    public List<? extends Validator<RegisterForm, Errors>> validators() {
        return validators;
    }
}
