package com.piotrglazar.webs.config;

import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.WebsNewsProvider;
import com.piotrglazar.webs.model.Account;
import com.piotrglazar.webs.model.Address;
import com.piotrglazar.webs.model.Currency;
import com.piotrglazar.webs.model.InternalWebsNews;
import com.piotrglazar.webs.model.SavingsAccount;
import com.piotrglazar.webs.model.WebsNews;
import com.piotrglazar.webs.model.WebsUser;
import com.piotrglazar.webs.model.WebsUserDetails;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.piotrglazar.webs.config.Settings.ADMIN_ROLE_DB;

class DatabasePopulator {

    private static final String[] IMG_CONTENT = {"#777:#7a7a7a/text:First slide", "#666:#6a6a6a/text:Second slide",
            "#555:#5a5a5a/text:Third slide", "#444:#4a4a4a/text:Fourth slide", "#333:#3a3a3a/text:Fifth slide"};

    private final UserProvider userProvider;

    private final WebsNewsProvider websNewsProvider;

    public DatabasePopulator(final UserProvider userProvider, final WebsNewsProvider websNewsProvider) {
        this.userProvider = userProvider;
        this.websNewsProvider = websNewsProvider;
    }

    @PostConstruct
    public void setupInitialData() {
        if (userProvider.findUserByUsername(Settings.USERNAME) == null) {
            addUserWithAccounts(Settings.USERNAME, "piotr.glazar@gmail.com", "abc123", "def456", true);
        }

        if (userProvider.findUserByUsername(Settings.USERNAME2) == null) {
            addUserWithAccounts(Settings.USERNAME2, "pglazar@wp.pl", "ghi123", "jkl456", false);
        }

        if (websNewsProvider.getNews().isEmpty()) {
            addNews();
        }
    }

    private void addNews() {
        websNewsProvider.addNews(accountsFeatureNews());
        websNewsProvider.addNews(newAccountFeatureNews());
        websNewsProvider.addNews(upcomingFeatureWorldwideFinancialNews());
        websNewsProvider.addNews(accountInterestFeatureNews());
        websNewsProvider.addNews(upcomingFeatureLanguagePacks());
    }

    private WebsNews upcomingFeatureLanguagePacks() {
        return InternalWebsNews.builder()
                .headline("Upcoming feature - I18N in Webs!")
                .body("We are aware that our customers are the best businessman around the world. We believe that using online bank "
                        + "in their mother tongue will enhance user experience")
                .url("#")
                .urlText("Upcoming feature")
                .imgContent(IMG_CONTENT[4])
                .build();
    }

    private WebsNews accountInterestFeatureNews() {
        return InternalWebsNews.builder()
                .headline("Account interest rate!")
                .body("Webs is the most trustworthy bank online. We are aiming at best interest rate around the world. It will be "
                        + "calculated daily so that you can benefit most from your accounts")
                .url("#")
                .urlText("Watch your account balance!")
                .imgContent(IMG_CONTENT[3])
                .build();
    }

    private WebsNews upcomingFeatureWorldwideFinancialNews() {
        return InternalWebsNews.builder()
                .headline("Upcoming feature - Worldwide financial news!")
                .body("We would like you to keep your finger of financial news. That's why you will see here most recent and "
                        + "reliable news about finance and economy")
                .url("#")
                .urlText("Upcoming feature")
                .imgContent(IMG_CONTENT[2])
                .build();
    }

    private WebsNews newAccountFeatureNews() {
        return InternalWebsNews.builder()
                .headline("Create new account!")
                .body("Do you need new account to organise your money in a better way? You can do that with just one click - "
                        + "just choose currency and it's done")
                .url("newAccount")
                .urlText("Create new account")
                .imgContent(IMG_CONTENT[1])
                .build();
    }

    private WebsNews accountsFeatureNews() {
        return InternalWebsNews.builder()
                .headline("Browse your accounts!")
                .body("Webs is a modern bank. As a result you can explore all of its features online - you can do everything with just "
                        + "one click. It has never been easier to keep an eye of your money")
                .url("accounts")
                .urlText("Start browsing your accounts")
                .imgContent(IMG_CONTENT[0])
                .build();
    }

    private void addUserWithAccounts(String username, String email, String firstAccountNo, String secondAccountNo, boolean admin) {
        final WebsUserDetails details = commonUserDetails();

        WebsUser websUser = userProvider.createUser(username, Settings.PASSWORD);
        websUser.setEmail(email);
        websUser.setDetails(details);

        final Account firstAccount = SavingsAccount.builder().number(firstAccountNo).balance(new BigDecimal(2500))
                .currency(Currency.PLN).interest(BigDecimal.valueOf(5.5)).build();
        websUser.getAccounts().add(firstAccount);
        websUser = userProvider.update(websUser);

        final Account secondAccount = SavingsAccount.builder().number(secondAccountNo).balance(new BigDecimal(8000))
                .currency(Currency.PLN).interest(BigDecimal.valueOf(4.5)).build();
        websUser.getAccounts().add(secondAccount);
        websUser = userProvider.update(websUser);

        if (admin) {
            websUser.getRoles().add(ADMIN_ROLE_DB);
            userProvider.update(websUser);
        }
    }

    private WebsUserDetails commonUserDetails() {
        final Address address = new Address("Warsaw", "Poland");
        return  new WebsUserDetails(LocalDateTime.now(), address);
    }
}
