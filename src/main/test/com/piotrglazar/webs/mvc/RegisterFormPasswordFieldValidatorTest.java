package com.piotrglazar.webs.mvc;

import org.junit.Test;
import org.springframework.validation.Errors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RegisterFormPasswordFieldValidatorTest {

    private RegisterFormPasswordFieldValidator validator = new RegisterFormPasswordFieldValidator();

    private Errors errors = mock(Errors.class);

    @Test
    public void shouldRejectWhenPasswordAndRepeatedPasswordDoNotMatch() {
        // given
        final RegisterForm registerForm = new RegisterForm();
        registerForm.setPassword("password");
        registerForm.setRepeatPassword("another password");

        // when
        validator.validate(registerForm, errors);

        // then
        verify(errors).rejectValue("password", "registerForm.password", "passwords must match");
        verify(errors).rejectValue("repeatPassword", "registerForm.repeatPassword", "passwords must match");
    }
}
