package com.piotrglazar.webs.mvc.controllers;

import com.piotrglazar.webs.business.passwordreset.PasswordResetService;
import com.piotrglazar.webs.mvc.forms.PasswordResetForm;
import com.piotrglazar.webs.mvc.validators.PasswordResetFormValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PasswordResetControllerTest {

    @Mock
    private BindingResult bindingResult;

    @Mock
    private PasswordResetService passwordResetService;

    @Mock
    private PasswordResetFormValidator passwordResetFormValidator;

    @InjectMocks
    private PasswordResetController passwordResetController;

    @Test
    public void shouldUsePasswordResetForm() {
        // given
        final PasswordResetForm form = new PasswordResetForm();

        // when
        final ModelAndView modelAndView = passwordResetController.resetPassword(form);

        // then
        assertThat(modelAndView.getModel()).containsEntry("passwordResetForm", form);
    }

    @Test
    public void shouldUsePasswordResetServiceAndRedirectToLoginPageWhenValidEmailProvided() {
        // given
        final PasswordResetForm form = passwordResetFormWithEmail("email");

        // when
        final String redirect = passwordResetController.resetPassword(form, bindingResult);

        // then
        assertThat(redirect).isEqualTo("redirect:login");
        verify(passwordResetService).sendUserPasswordResetMessage("email");
    }

    @Test
    public void shouldStayOnPasswordResetPageWhenEmailIsInvalid() {
        // given
        final PasswordResetForm form = new PasswordResetForm();
        given(bindingResult.hasErrors()).willReturn(true);

        // when
        final String redirect = passwordResetController.resetPassword(form, bindingResult);

        // then
        assertThat(redirect).isEqualTo("passwordReset");
        verify(passwordResetService, never()).sendUserPasswordResetMessage(anyString());
    }

    private PasswordResetForm passwordResetFormWithEmail(String email) {
        final PasswordResetForm form = new PasswordResetForm();
        form.setEmail(email);
        return form;
    }
}
