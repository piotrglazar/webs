package com.piotrglazar.webs.mvc.validators;

import com.piotrglazar.webs.mvc.RegisterForm;
import org.junit.Test;
import org.springframework.validation.Errors;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class RegisterFormPasswordFieldValidatorTest {

    private RegisterFormPasswordFieldValidator validator = new RegisterFormPasswordFieldValidator();

    private Errors errors = mock(Errors.class);

    private RegisterForm registerForm = new RegisterForm();

    @Test
    public void shouldRejectWhenPasswordAndRepeatedPasswordDoNotMatch() {
        // given
        registerForm.setPassword("password");
        registerForm.setRepeatPassword("another password");

        // when
        validator.validate(registerForm, errors);

        // then
        verify(errors).rejectValue("password", "registerForm.password", "passwords must match");
        verify(errors).rejectValue("repeatPassword", "registerForm.repeatPassword", "passwords must match");
    }

    @Test
    public void shouldAllowMatchingPasswordAndRepeatedPassword() {
        // given
        registerForm.setPassword("password");
        registerForm.setRepeatPassword("password");

        // when
        validator.validate(registerForm, errors);

        // then
        verify(errors, never()).rejectValue(anyString(), anyString(), anyString());
    }
}
