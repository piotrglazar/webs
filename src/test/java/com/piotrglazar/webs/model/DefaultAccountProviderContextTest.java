package com.piotrglazar.webs.model;

import com.google.common.collect.Sets;
import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.AccountProvider;
import com.piotrglazar.webs.config.Settings;
import com.piotrglazar.webs.dto.AccountDto;
import com.piotrglazar.webs.dto.SavingsAccountDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultAccountProviderContextTest extends AbstractContextTest {

    // due to @OperationLogging(operation = "newAccount") we have to autowire by interface
    @Autowired
    private AccountProvider defaultAccountProvider;

    @Autowired
    private WebsUserRepository websUserRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void shouldFindUserAccountAndConvertItToDto() {
        // given
        final Account account = SavingsAccount.builder().number("abc").balance(BigDecimal.TEN).interest(BigDecimal.ONE).build();
        final WebsUser websUser = WebsUser.builder().username("userxyz").password("pass").accounts(Sets.newHashSet(account)).build();
        websUserRepository.save(websUser);

        // when
        final List<AccountDto> accountDtos = defaultAccountProvider.getUserAccounts("userxyz");

        // then
        assertThat(accountDtos).extracting("number").containsOnly("abc");
        assertThat(accountDtos).extracting("balance").containsOnly(BigDecimal.TEN.setScale(2));
    }

    @Test
    public void shouldFindSavingsAccountByUsernameAndAccountId() {
        // given
        final Account account = SavingsAccount.builder().number("abc").balance(BigDecimal.TEN).interest(BigDecimal.ONE).build();
        final WebsUser websUser = WebsUser.builder().username("userabc").password("pass").accounts(Sets.newHashSet(account)).build();
        websUserRepository.save(websUser);

        // when
        final Optional<SavingsAccountDto> accountDto = defaultAccountProvider.getUserSavingsAccount("userabc", account.getId());

        // then
        assertThat(accountDto.isPresent());
        assertThat(accountDto.get().getBalance()).isEqualByComparingTo("10.00");
        assertThat(accountDto.get().getInterest()).isEqualByComparingTo("1.00");
    }

    @Test
    public void shouldCreateNewAccount() {
        // given

        // when
        String accountNumber = defaultAccountProvider.newAccount(Settings.USERNAME, AccountType.SAVINGS, Currency.GBP);

        // then
        final Account account = accountRepository.findByNumber(accountNumber);
        assertThat(account.getCurrency()).isEqualTo(Currency.GBP);
        assertThat(account.getBalance()).isEqualByComparingTo("0");
        assertThat(defaultAccountProvider.getUserSavingsAccount(Settings.USERNAME, account.getId()).isPresent()).isTrue();
    }
}
