package com.piotrglazar.webs.mvc.validators;

import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.model.WebsUser;
import com.piotrglazar.webs.mvc.RegisterForm;
import com.piotrglazar.webs.mvc.RegisterFormFieldValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
class RegisterFormEmailFieldValidator implements RegisterFormFieldValidator {

    private final UserProvider userProvider;

    @Autowired
    public RegisterFormEmailFieldValidator(final UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    @Override
    public void validate(final RegisterForm object, final Errors errorGatherer) {
        final WebsUser websUser = userProvider.findUserByEmail(object.getEmail());
        if (websUser != null) {
            errorGatherer.rejectValue("email", "registerForm.email", "email is already in use");
        }
    }
}
