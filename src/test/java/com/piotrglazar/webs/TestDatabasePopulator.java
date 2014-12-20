package com.piotrglazar.webs;

import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.config.Settings;
import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.model.entities.Address;
import com.piotrglazar.webs.model.entities.InternalWebsNews;
import com.piotrglazar.webs.model.entities.SavingsAccount;
import com.piotrglazar.webs.model.entities.WebsNews;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.model.entities.WebsUserDetails;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

import static com.piotrglazar.webs.DatabaseTestConfiguration.ACCOUNT_BALANCES;

class TestDatabasePopulator {

    private final UserProvider userProvider;

    private final WebsNewsProvider newsProvider;

    public TestDatabasePopulator(final UserProvider userProvider, final WebsNewsProvider newsProvider) {
        this.userProvider = userProvider;
        this.newsProvider = newsProvider;
    }

    @PostConstruct
    public void setupInitialData() {
        if (userProvider.findUserByUsername(Settings.USERNAME) == null) {
            addUserWithAccounts(Settings.USERNAME, "piotr.glazar@gmail.com", "abc123", "def456", true);
        }

        if (userProvider.findUserByUsername(Settings.USERNAME2) == null) {
            addUserWithAccounts(Settings.USERNAME2, "pglazar@wp.pl", "ghi123", "jkl456", false);
        }

        if (newsProvider.getNews().isEmpty()) {
            addNews();
        }
    }

    private void addNews() {
        newsProvider.addNews(accountsFeatureNews());
        newsProvider.addNews(newAccountFeatureNews());
    }

    private WebsNews newAccountFeatureNews() {
        return InternalWebsNews.builder()
                .headline("Create new account!")
                .body("Do you need new account to organise your money in a better way? You can do that with just one click - "
                        + "just choose currency and it's done")
                .url("newAccount")
                .urlText("Create new account")
                .imgContent("img content 1")
                .build();
    }

    private WebsNews accountsFeatureNews() {
        return InternalWebsNews.builder()
                .headline("Browse your accounts!")
                .body("Webs is a modern bank. As a result you can explore all of its features online - you can do everything with just "
                        + "one click. It has never been easier to keep an eye of your money")
                .url("accounts")
                .urlText("Start browsing your accounts")
                .imgContent("img content 2")
                .build();
    }

    private void addUserWithAccounts(String username, String email, String firstAccountNo, String secondAccountNo, boolean isAdmin) {
        final WebsUserDetails commonUserDetails = commonUserDetails();

        WebsUser websUser = userProvider.createUser(username, Settings.PASSWORD);
        websUser.setEmail(email);
        websUser.setDetails(commonUserDetails);

        final Account firstAccount = SavingsAccount.builder().number(firstAccountNo).balance(ACCOUNT_BALANCES.get(0))
                .currency(Currency.PLN).interest(DatabaseTestConfiguration.ACCOUNT_INTERESTS.get(0)).build();
        websUser.getAccounts().add(firstAccount);
        websUser = userProvider.update(websUser);

        final Account secondAccount = SavingsAccount.builder().number(secondAccountNo).balance(ACCOUNT_BALANCES.get(1))
                .currency(Currency.PLN).interest(DatabaseTestConfiguration.ACCOUNT_INTERESTS.get(1)).build();
        websUser.getAccounts().add(secondAccount);
        websUser = userProvider.update(websUser);

        if (isAdmin) {
            websUser.getRoles().add(Settings.ADMIN_ROLE_DB);
            userProvider.update(websUser);
        }
    }

    private WebsUserDetails commonUserDetails() {
        final Address address = new Address("Warsaw", "Poland");
        return  new WebsUserDetails(LocalDateTime.of(2014, 7, 21, 0, 0), address);
    }
}
