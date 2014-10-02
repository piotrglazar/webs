package com.piotrglazar.webs.mvc.validators;

import com.piotrglazar.webs.AccountProvider;
import com.piotrglazar.webs.mvc.forms.TransferForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TransferFormValidator implements Validator {

    private final AccountProvider accountProvider;

    @Autowired
    public TransferFormValidator(final AccountProvider accountProvider) {
        this.accountProvider = accountProvider;
    }

    @Override
    public boolean supports(final Class<?> clazz) {
        return TransferForm.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final TransferForm transferForm = (TransferForm) target;

        if (accountProvider.getAccount(transferForm.getAccountNumber()) == null) {
            errors.rejectValue("accountNumber", "transferForm.accountNumber", "Please provide valid account number");
        }

        if (transferForm.getIntegralPart() == 0 && transferForm.getFractionalPart() == 0) {
            errors.rejectValue("integralPart", "transferForm.integralPart", "Please provide amount of money to be transferred");
            errors.rejectValue("fractionalPart", "transferForm.fractionalPart", "Please provide amount of money to be transferred");
        }

    }
}
