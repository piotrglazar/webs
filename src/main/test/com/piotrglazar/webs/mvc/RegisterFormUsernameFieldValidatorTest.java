package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.model.WebsUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RegisterFormUsernameFieldValidatorTest {

    @Mock
    private UserProvider userProvider;

    @Mock
    private Errors errors;

    @InjectMocks
    private RegisterFormUsernameFieldValidator validator;

    @Test
    public void shouldRejectAlreadyTakenUsername() {
        // given
        final RegisterForm registerForm = new RegisterForm();
        registerForm.setUsername("user");
        given(userProvider.findUser("user")).willReturn(WebsUser.builder().username("user").build());

        // when
        validator.validate(registerForm, errors);

        // then
        verify(errors).rejectValue("username", "registerForm.username", "username is already in use");
    }
}
