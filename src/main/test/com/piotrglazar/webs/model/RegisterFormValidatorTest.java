package com.piotrglazar.webs.model;

import com.google.common.collect.Sets;
import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.model.WebsUser;
import com.piotrglazar.webs.mvc.RegisterForm;
import com.piotrglazar.webs.mvc.RegisterFormValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RegisterFormValidatorTest {

    @Mock
    private UserProvider userProvider;

    @Mock
    private Errors errors;

    @InjectMocks
    private RegisterFormValidator validator;

    @Test
    public void shouldAcceptRegisterForm() {
        // expect
        validator.supports(RegisterForm.class);
    }

    @Test
    public void shouldRejectAlreadyTakenUsername() {
        // given
        final RegisterForm registerForm = new RegisterForm();
        registerForm.setUsername("user");
        registerForm.setPassword("password");
        registerForm.setRepeatPassword("password");
        given(userProvider.findUser("user")).willReturn(new WebsUser("user", "password", Sets.<String>newHashSet()));

        // when
        validator.validate(registerForm, errors);

        // then
        verify(errors).rejectValue("username", "registerForm.username", "username is already in use");
    }

    @Test
    public void shouldRejectWhenPasswordAndRepeatedPasswordDoNotMatch() {
        // given
        final RegisterForm registerForm = new RegisterForm();
        registerForm.setPassword("password");
        registerForm.setRepeatPassword("another password");
        given(userProvider.findUser("user")).willReturn(new WebsUser("user", "password", Sets.<String>newHashSet()));

        // when
        validator.validate(registerForm, errors);

        // then
        verify(errors).rejectValue("password", "registerForm.password", "passwords must match");
        verify(errors).rejectValue("repeatPassword", "registerForm.repeatPassword", "passwords must match");
    }
}
