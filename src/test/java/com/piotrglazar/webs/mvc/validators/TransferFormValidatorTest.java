package com.piotrglazar.webs.mvc.validators;

import com.piotrglazar.webs.AccountProvider;
import com.piotrglazar.webs.dto.AccountDto;
import com.piotrglazar.webs.mvc.forms.TransferForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
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
        // given
        given(accountProvider.getAccount(anyString())).willReturn(Optional.<AccountDto>empty());

        // when
        validator.validate(form, errors);

        // then
        verify(errors).rejectValue(anyString(), anyString(), eq("Please provide valid account number"));
    }

    @Test
    public void shouldRejectWhenNoMoneyIsTransferred() {
        // given
        given(accountProvider.getAccount(anyString())).willReturn(Optional.<AccountDto>empty());

        // when
        validator.validate(form, errors);

        // then
        verify(errors, times(2)).rejectValue(anyString(), anyString(), eq("Please provide amount of money to be transferred"));
    }
}
