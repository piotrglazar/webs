package com.piotrglazar.webs.mvc.validators;

import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.mvc.forms.RegisterForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RegisterFormEmailFieldValidatorTest {

    @Mock
    private UserProvider userProvider;

    @Mock
    private Errors errors;

    @InjectMocks
    private RegisterFormEmailFieldValidator validator;

    private RegisterForm registerForm = new RegisterForm();

    @Test
    public void shouldRejectAlreadyTakenEmail() {
        // given
        registerForm.setEmail("p@p.pl");
        given(userProvider.findUserByEmail("p@p.pl")).willReturn(WebsUser.builder().email("p@p.pl").build());

        // when
        validator.validate(registerForm, errors);

        // then
        verify(errors).rejectValue("email", "registerForm.email", "email is already in use");
    }

    @Test
    public void shouldAllowFreshEmail() {
        // given
        registerForm.setEmail("p@p.pl");

        // when
        validator.validate(registerForm, errors);

        // then
        verify(errors, never()).rejectValue(anyString(), anyString(), anyString());
    }
}
