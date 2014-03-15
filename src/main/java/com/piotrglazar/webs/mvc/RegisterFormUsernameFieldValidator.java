package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.model.WebsUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
class RegisterFormUsernameFieldValidator implements RegisterFormFieldValidator {

    private final UserProvider userProvider;

    @Autowired
    public RegisterFormUsernameFieldValidator(final UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    @Override
    public void validate(final RegisterForm form, final Errors errors) {
        final WebsUser user = userProvider.findUser(form.getUsername());
        if (user != null) {
            errors.rejectValue("username", "registerForm.username", "username is already in use");
        }
    }
}
