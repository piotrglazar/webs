package com.piotrglazar.webs.business;

import com.piotrglazar.webs.model.Account;
import com.piotrglazar.webs.model.AccountRepository;
import com.piotrglazar.webs.model.WebsUser;
import com.piotrglazar.webs.model.WebsUserRepository;
import com.piotrglazar.webs.mvc.TransferForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class MoneyTransferParamsFactoryTest {

    @Mock
    private WebsUserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private MoneyTransferParamsFactory factory;

    @Test
    public void shouldProperlyConstructBigDecimalFromIntegralAndFractionalPart() {
        // given
        final TransferForm transferForm = transferFormWith("abc", 123, 45);
        final Account account = mock(Account.class);
        final WebsUser user = new WebsUser();
        user.setEmail("email");
        given(account.getId()).willReturn(2L);
        given(accountRepository.findByNumber("abc")).willReturn(account);
        given(userRepository.findByUsername("user")).willReturn(user);

        // when
        final MoneyTransferParams params = factory.create("user", transferForm);

        // then
        assertThat(params.getFromAccount()).isEqualTo(1L);
        assertThat(params.getToAccount()).isEqualTo(2L);
        assertThat(params.getUsername()).isEqualTo("user");
        assertThat(params.getAmount()).isEqualByComparingTo("123.45");
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
