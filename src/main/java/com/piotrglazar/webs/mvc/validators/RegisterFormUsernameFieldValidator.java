package com.piotrglazar.webs.mvc.validators;

import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.model.WebsUser;
import com.piotrglazar.webs.mvc.RegisterForm;
import com.piotrglazar.webs.mvc.RegisterFormFieldValidator;
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
        final WebsUser user = userProvider.findUserByUsername(form.getUsername());
        if (user != null) {
            errors.rejectValue("username", "registerForm.username", "username is already in use");
        }
    }
}
