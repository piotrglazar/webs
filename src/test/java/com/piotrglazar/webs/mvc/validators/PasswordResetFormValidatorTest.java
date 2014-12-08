package com.piotrglazar.webs.mvc.validators;

import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.model.entities.WebsUserBuilder;
import com.piotrglazar.webs.mvc.forms.PasswordResetForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class PasswordResetFormValidatorTest {

    @Mock
    private Errors errors;

    @Mock
    private UserProvider userProvider;

    @InjectMocks
    private PasswordResetFormValidator validator;

    @Test
    public void shouldNotReportAnyErrorsWhenEmailIsValid() {
        // given
        given(userProvider.findUserByEmail("email")).willReturn(new WebsUserBuilder().build());

        // when
        validator.validate(newPasswordResetFormWithEmail("email"), errors);

        // then
        verifyZeroInteractions(errors);
    }

    @Test
    public void shouldReportErrorsWhenEmailIsInvalid() {
        // given

        // when
        validator.validate(newPasswordResetFormWithEmail("email"), errors);

        // then
        verify(errors).rejectValue(eq("email"), anyString(), anyString());
    }

    private PasswordResetForm newPasswordResetFormWithEmail(final String email) {
        final PasswordResetForm form = new PasswordResetForm();
        form.setEmail(email);
        return form;
    }
}
