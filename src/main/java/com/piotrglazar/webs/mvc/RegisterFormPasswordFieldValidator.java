package com.piotrglazar.webs.mvc;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
class RegisterFormPasswordFieldValidator implements RegisterFormFieldValidator {
    @Override
    public void validate(final RegisterForm form, final Errors errors) {
        if (!form.getPassword().equals(form.getRepeatPassword())) {
            errors.rejectValue("password", "registerForm.password", "passwords must match");
            errors.rejectValue("repeatPassword", "registerForm.repeatPassword", "passwords must match");
        }
    }
}
