package com.piotrglazar.webs.mvc.validators;

import com.piotrglazar.webs.mvc.forms.NewPasswordForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class NewPasswordFormFieldValidator implements Validator {

    private final PasswordFieldsValidator passwordFieldsValidator;

    @Autowired
    public NewPasswordFormFieldValidator(PasswordFieldsValidator passwordFieldsValidator) {
        this.passwordFieldsValidator = passwordFieldsValidator;
    }

    @Override
    public boolean supports(final Class<?> clazz) {
        return NewPasswordForm.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final NewPasswordForm newPasswordForm = (NewPasswordForm) target;

        passwordFieldsValidator.validatePasswords(newPasswordForm.getNewPassword(), newPasswordForm.getRepeatedNewPassword(), errors,
                "newPasswordForm", "newPassword", "repeatedNewPassword");
    }
}
