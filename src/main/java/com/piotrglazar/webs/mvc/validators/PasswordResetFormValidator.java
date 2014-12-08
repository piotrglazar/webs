package com.piotrglazar.webs.mvc.validators;

import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.mvc.forms.PasswordResetForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PasswordResetFormValidator implements Validator {

    private final UserProvider userProvider;

    @Autowired
    public PasswordResetFormValidator(final UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    @Override
    public boolean supports(final Class<?> clazz) {
        return PasswordResetForm.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final PasswordResetForm passwordResetForm = (PasswordResetForm) target;
        if (noUserWithEmail(passwordResetForm)) {
            errors.rejectValue("email", "passwordResetForm.email", "there is no user with such an email");
        }
    }

    private boolean noUserWithEmail(final PasswordResetForm passwordResetForm) {
        return userProvider.findUserByEmail(passwordResetForm.getEmail()) == null;
    }
}
