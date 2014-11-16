package com.piotrglazar.webs.business;

import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.model.entities.WebsUserBuilder;
import com.piotrglazar.webs.model.repositories.AccountRepository;
import com.piotrglazar.webs.mvc.forms.TransferForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class MoneyTransferParamsFactoryTest {

    @Mock
    private UserProvider userProvider;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private MoneyAmountBuilder moneyAmountBuilder;

    @InjectMocks
    private MoneyTransferParamsFactory factory;

    @Test
    public void shouldConstructMoneyTransferParams() {
        // given
        final TransferForm transferForm = transferFormWith("abc", 123, 45);
        final Account receivingAccount = mock(Account.class);
        final WebsUser sender = new WebsUserBuilder().email("email").build();
        final WebsUser receiver = new WebsUserBuilder().id(1000).build();
        given(receivingAccount.getId()).willReturn(2L);
        given(receivingAccount.getCurrency()).willReturn(Currency.GBP);
        given(accountRepository.findByNumber("abc")).willReturn(receivingAccount);
        given(userProvider.getUserByUsername("user")).willReturn(sender);
        given(moneyAmountBuilder.fromIntegralAndFractionalParts(123, 45)).willReturn(new BigDecimal("123.45"));
        given(userProvider.findUserByAccountId(2L)).willReturn(receiver);

        // when
        final MoneyTransferParams params = factory.create("user", transferForm);

        // then
        MoneyTransferParamsAssert.assertThat(params)
                .hasFromAccount(1)
                .hasToAccount(2)
                .hasUsername("user")
                .hasAmount("123.45")
                .hasReceivingUserId(1000)
                .hasCurrency(Currency.GBP);
    }

    private TransferForm transferFormWith(final String accountNumber, final long integralPart, final long fractionalPart) {
        final TransferForm transferForm = new TransferForm();
        transferForm.setAccountId(1);
        transferForm.setAccountNumber(accountNumber);
        transferForm.setIntegralPart(integralPart);
        transferForm.setFractionalPart(fractionalPart);
        return transferForm;
    }
}
