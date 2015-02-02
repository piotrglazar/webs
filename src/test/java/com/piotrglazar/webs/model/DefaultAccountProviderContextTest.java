package com.piotrglazar.webs.model;

import com.google.common.collect.Sets;
import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.AccountProvider;
import com.piotrglazar.webs.business.utils.AccountType;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.config.Settings;
import com.piotrglazar.webs.dto.AccountDto;
import com.piotrglazar.webs.dto.SavingsAccountDto;
import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.model.entities.SavingsAccount;
import com.piotrglazar.webs.model.entities.SavingsAccountBuilder;
import com.piotrglazar.webs.model.entities.Subaccount;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.model.repositories.AccountRepository;
import com.piotrglazar.webs.model.repositories.WebsUserRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.piotrglazar.webs.model.entities.WebsUser.builder;
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
        final WebsUser websUser = websUserRepository.save(createWebsUserWithUsernameAndAccount("userxyz", account));

        // when
        final List<AccountDto> accountDtos = defaultAccountProvider.getUserAccounts("userxyz");

        // then
        assertThat(accountDtos).extracting("number").containsOnly("abc");
        assertThat(accountDtos).extracting("balance").containsOnly(BigDecimal.TEN.setScale(2));

        // cleanup
        websUserRepository.delete(websUser);
    }

    @Test
    public void shouldFindSavingsAccountByUsernameAndAccountId() {
        // given
        final Account account = SavingsAccount.builder().number("abc").balance(BigDecimal.TEN).interest(BigDecimal.ONE).build();
        final WebsUser websUser = websUserRepository.save(createWebsUserWithUsernameAndAccount("userabc", account));

        // when
        final Optional<SavingsAccountDto> accountDto = defaultAccountProvider.getUserSavingsAccount("userabc", account.getId());

        // then
        assertThat(accountDto.isPresent());
        assertThat(accountDto.get().getBalance()).isEqualByComparingTo("10.00");
        assertThat(accountDto.get().getInterest()).isEqualByComparingTo("1.00");

        // cleanup
        websUserRepository.delete(websUser);
    }

    @Test
    public void shouldCreateNewAccount() {
        // when
        final String accountNumber = defaultAccountProvider.newAccount(Settings.USERNAME, AccountType.SAVINGS, Currency.GBP);

        // then
        final Account account = accountRepository.findByNumber(accountNumber);
        assertThat(account.getCurrency()).isEqualTo(Currency.GBP);
        assertThat(account.getBalance()).isEqualByComparingTo("0");
        assertThat(defaultAccountProvider.getUserSavingsAccount(Settings.USERNAME, account.getId()).isPresent()).isTrue();

        // cleanup
        accountRepository.delete(account);
    }

    @Test
    public void shouldCreateNewSubaccount() {
        // given
        final Account account = savingsAccount(BigDecimal.valueOf(1000));
        final WebsUser websUser = websUserRepository.save(createWebsUserWithUsernameAndAccount("userdef", account));

        // when
        defaultAccountProvider.newSubaccount("userdef", account.getId(), BigDecimal.valueOf(100), "subaccount");

        // then
        final Account userAccount = accountRepository.findOne(account.getId());
        assertThat(userAccount.getBalance()).isEqualByComparingTo("900");
        assertThat(userAccount.getSubaccounts()).hasSize(1);
        final Subaccount userSubaccount = userAccount.getSubaccounts().get(0);
        assertThat(userSubaccount.getName()).isEqualTo("subaccount");
        assertThat(userSubaccount.getBalance()).isEqualByComparingTo("100");

        // cleanup
        // we must delete separatedly because otherwise we get
        // integrity constraint violation: foreign key no action; table: ACCOUNT_SUBACCOUNT
        accountRepository.delete(userAccount);
        websUserRepository.delete(websUser);
    }

    @Test
    public void shouldDeleteSubaccount() {
        // given
        final Account account = savingsAccount(BigDecimal.ZERO, new Subaccount("subaccount", BigDecimal.TEN));
        final WebsUser websUser = websUserRepository.save(createWebsUserWithUsernameAndAccount("userghi", account));

        // when
        defaultAccountProvider.deleteSubaccount("userghi", account.getId(), "subaccount");

        // then
        final Account userAccount = accountRepository.findOne(account.getId());
        assertThat(userAccount.getBalance()).isEqualByComparingTo("10");
        assertThat(userAccount.getSubaccounts()).hasSize(0);

        // cleanup
        // we must delete separatedly because otherwise we get
        // integrity constraint violation: foreign key no action; table: ACCOUNT_SUBACCOUNT
        accountRepository.delete(userAccount);
        websUserRepository.delete(websUser);
    }

    private SavingsAccount savingsAccount(BigDecimal balance, Subaccount... subaccounts) {
        final SavingsAccountBuilder savingsAccountBuilder = SavingsAccount.builder().number("abc").balance(balance);
        Arrays.stream(subaccounts).forEach(savingsAccountBuilder::subaccount);
        return savingsAccountBuilder.build();
    }

    private WebsUser createWebsUserWithUsernameAndAccount(final String username, final Account account) {
        return builder()
                .username(username)
                .email(String.format("%s@%s.pl", username, username))
                .password("pass")
                .accounts(Sets.newHashSet(account))
                .build();
    }
}
