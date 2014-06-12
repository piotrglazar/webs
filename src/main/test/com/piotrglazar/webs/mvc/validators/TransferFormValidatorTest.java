package com.piotrglazar.webs.mvc.validators;

import com.piotrglazar.webs.AccountProvider;
import com.piotrglazar.webs.mvc.TransferForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TransferFormValidatorTest {

    @Mock
    private TransferForm form;

    @Mock
    private AccountProvider accountProvider;

    @Mock
    private Errors errors;
    
    @InjectMocks
    private TransferFormValidator validator;

    @Test
    public void shouldRejectWhenThereIsNoAccount() {
        // when
        validator.validate(form, errors);

        // then
        verify(errors).rejectValue(anyString(), anyString(), eq("Please provide valid account number"));
    }

    @Test
    public void shouldRejectWhenNoMoneyIsTransferred() {
        // when
        validator.validate(form, errors);

        // then
        verify(errors, times(2)).rejectValue(anyString(), anyString(), eq("Please provide amount of money to be transferred"));
    }
}
