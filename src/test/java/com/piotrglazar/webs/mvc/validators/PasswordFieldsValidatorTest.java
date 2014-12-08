package com.piotrglazar.webs.mvc.validators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class PasswordFieldsValidatorTest {

    @Mock
    private Errors errors;

    private PasswordFieldsValidator validator = new PasswordFieldsValidator();

    @Test
    public void shouldNotReportAnyErrorsWhenPasswordsMatch() {
        // given
        final String password = "password";
        final String repeatedPassword = "password";

        // when
        validator.validatePasswords(password, repeatedPassword, errors, "form", "field");

        // then
        verifyNoMoreInteractions(errors);
    }

    @Test
    public void shouldReportErrorWhenPasswordsDoNotMatch() {
        // given
        final String password = "password";
        final String repeatedPassword = "repeatedPassword";

        // when
        validator.validatePasswords(password, repeatedPassword, errors, "form", "field");

        // then
        verify(errors).rejectValue("field", "form.field", "passwords must match");
    }
}
