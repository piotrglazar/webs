package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.Validator;
import com.piotrglazar.webs.mvc.forms.RegisterForm;
import org.springframework.validation.Errors;

public interface RegisterFormFieldValidator extends Validator<RegisterForm, Errors> {
}
