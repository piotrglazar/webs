package com.piotrglazar.webs.mvc.validators;

import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.model.WebsUser;
import com.piotrglazar.webs.mvc.RegisterForm;
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
public class RegisterFormUsernameFieldValidatorTest {

    @Mock
    private UserProvider userProvider;

    @Mock
    private Errors errors;

    @InjectMocks
    private RegisterFormUsernameFieldValidator validator;

    private RegisterForm registerForm = new RegisterForm();

    @Test
    public void shouldRejectAlreadyTakenUsername() {
        // given
        registerForm.setUsername("user");
        given(userProvider.findUserByUsername("user")).willReturn(WebsUser.builder().username("user").build());

        // when
        validator.validate(registerForm, errors);

        // then
        verify(errors).rejectValue("username", "registerForm.username", "username is already in use");
    }

    @Test
    public void shouldAllowFreshUsername() {
        // given
        registerForm.setUsername("user");

        // when
        validator.validate(registerForm, errors);

        // then
        verify(errors, never()).rejectValue(anyString(), anyString(), anyString());
    }
}
