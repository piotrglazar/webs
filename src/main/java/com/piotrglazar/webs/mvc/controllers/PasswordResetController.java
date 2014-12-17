package com.piotrglazar.webs.mvc.controllers;

import com.piotrglazar.webs.business.passwordreset.PasswordResetService;
import com.piotrglazar.webs.mvc.forms.PasswordResetForm;
import com.piotrglazar.webs.mvc.validators.PasswordResetFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.piotrglazar.webs.config.UtilityConfiguration.PASSWORD_RESET_ROOT_URL;

@Controller
public class PasswordResetController {

    private final PasswordResetFormValidator validator;
    private final PasswordResetService passwordResetService;

    @Autowired
    public PasswordResetController(PasswordResetFormValidator validator, PasswordResetService passwordResetService) {
        this.validator = validator;
        this.passwordResetService = passwordResetService;
    }

    @InitBinder
    public void initBinder(final DataBinder dataBinder) {
        dataBinder.addValidators(validator);
    }

    @RequestMapping(value = "/" + PASSWORD_RESET_ROOT_URL, method = RequestMethod.GET)
    public ModelAndView resetPassword(final PasswordResetForm passwordResetForm) {
        final ModelAndView modelAndView = new ModelAndView("passwordReset");
        modelAndView.addObject("passwordResetForm", passwordResetForm);
        return modelAndView;
    }

    @RequestMapping(value = "/" + PASSWORD_RESET_ROOT_URL, method = RequestMethod.POST)
    public String resetPassword(@NotNull @Valid final PasswordResetForm passwordResetForm, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "passwordReset";
        }

        passwordResetService.sendUserPasswordResetMessage(passwordResetForm.getEmail());

        return "redirect:login";
    }
}
