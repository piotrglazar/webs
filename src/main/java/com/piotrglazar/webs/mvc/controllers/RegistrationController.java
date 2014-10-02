package com.piotrglazar.webs.mvc.controllers;

import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.mvc.forms.RegisterForm;
import com.piotrglazar.webs.mvc.validators.RegisterFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("/newUser")
class RegistrationController {

    private final UserProvider userProvider;

    private final RegisterFormValidator validator;

    @Autowired
    public RegistrationController(final UserProvider userProvider, final RegisterFormValidator validator) {
        this.userProvider = userProvider;
        this.validator = validator;
    }

    @InitBinder
    public void initBinder(final DataBinder dataBinder) {
        dataBinder.addValidators(validator);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showRegisterPage(final RegisterForm registerForm) {
        return "newUser";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String registerNewUser(@Valid final RegisterForm registerForm, final BindingResult result) {
        if (result.hasErrors()) {
            return "newUser";
        }
        userProvider.createUser(registerForm.getUsername(), registerForm.getPassword());
        return "redirect:login";
    }
}
