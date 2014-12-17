package com.piotrglazar.webs.mvc.controllers;

import com.piotrglazar.webs.business.passwordreset.PasswordResetService;
import com.piotrglazar.webs.mvc.forms.NewPasswordForm;
import com.piotrglazar.webs.mvc.validators.NewPasswordFormFieldValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.piotrglazar.webs.config.UtilityConfiguration.PASSWORD_RESET_ROOT_URL;

@Controller
public class NewPasswordController {

    private final NewPasswordFormFieldValidator newPasswordFormFieldValidator;
    private final PasswordResetService passwordResetService;

    @Autowired
    public NewPasswordController(NewPasswordFormFieldValidator newPasswordFormFieldValidator, PasswordResetService passwordResetService) {
        this.newPasswordFormFieldValidator = newPasswordFormFieldValidator;
        this.passwordResetService = passwordResetService;
    }

    @InitBinder
    public void initBinder(final DataBinder dataBinder) {
        dataBinder.addValidators(newPasswordFormFieldValidator);
    }

    @RequestMapping(value = "/" + PASSWORD_RESET_ROOT_URL + "/{tokenId}/", method = RequestMethod.GET)
    public ModelAndView newPassword(@PathVariable("tokenId") String tokenId, NewPasswordForm newPasswordForm) {
        final ModelAndView modelAndView = new ModelAndView("newPassword");
        modelAndView.addObject("tokenId", tokenId);
        modelAndView.addObject("newPasswordForm", newPasswordForm);
        return modelAndView;
    }

    @RequestMapping(value = "/" + PASSWORD_RESET_ROOT_URL + "/{tokenId}/", method = RequestMethod.POST)
    public String updatePassword(@PathVariable("tokenId") String tokenId, @NotNull @Valid NewPasswordForm newPasswordForm,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "newPassword";
        }

        passwordResetService.resetUserPassword(tokenId, newPasswordForm.getNewPassword());

        return "redirect:/login";
    }
}
