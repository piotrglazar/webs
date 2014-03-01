package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.model.WebsUser;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RegisterFormValidator implements Validator {
    private final UserProvider userProvider;

    public RegisterFormValidator(final UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    @Override
    public boolean supports(final Class<?> clazz) {
        return RegisterForm.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final RegisterForm form = (RegisterForm) target;

        final WebsUser user = userProvider.findUser(form.getUsername());
        if (user != null) {
            errors.rejectValue("username", "registerForm.username", "username is already in use");
        }
        if (!form.getPassword().equals(form.getRepeatPassword())) {
            errors.rejectValue("password", "registerForm.password", "passwords must match");
            errors.rejectValue("repeatPassword", "registerForm.repeatPassword", "passwords must match");
        }
    }
}
