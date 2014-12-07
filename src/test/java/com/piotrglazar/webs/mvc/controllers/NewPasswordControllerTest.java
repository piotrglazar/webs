package com.piotrglazar.webs.mvc.controllers;

import com.piotrglazar.webs.business.PasswordResetService;
import com.piotrglazar.webs.mvc.forms.NewPasswordForm;
import com.piotrglazar.webs.mvc.validators.NewPasswordFormFieldValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NewPasswordControllerTest {

    @Mock
    private BindingResult bindingResult;

    @Mock
    private NewPasswordFormFieldValidator newPasswordFormFieldValidator;

    @Mock
    private PasswordResetService passwordResetService;

    @InjectMocks
    private NewPasswordController newPasswordController;

    @Test
    public void shouldUseNewPasswordForm() {
        // given
        final NewPasswordForm newPasswordForm = new NewPasswordForm();
        final String tokenId = "tokenId";

        // when
        final ModelAndView modelAndView = newPasswordController.newPassword(tokenId, newPasswordForm);

        // then
        assertThat(modelAndView.getModel())
                .containsEntry("tokenId", tokenId)
                .containsEntry("newPasswordForm", newPasswordForm);
    }

    @Test
    public void shouldStayOnNewPasswordPageWhenThereArePasswordErrors() {
        // given
        given(bindingResult.hasErrors()).willReturn(true);

        // when
        final String view = newPasswordController.updatePassword("tokenId", new NewPasswordForm(), bindingResult);

        // then
        assertThat(view).isEqualTo("newPassword");
    }

    @Test
    public void shouldResetUserPasswordAndRedirectToLoginPage() {
        // given
        final NewPasswordForm newPasswordForm = new NewPasswordForm();
        newPasswordForm.setNewPassword("password");

        // when
        final String view = newPasswordController.updatePassword("tokenId", newPasswordForm, bindingResult);

        // then
        verify(passwordResetService).resetUserPassword("tokenId", "password");
        assertThat(view).contains("redirect", "login");
    }
}
