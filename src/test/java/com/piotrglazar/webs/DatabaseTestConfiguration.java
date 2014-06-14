package com.piotrglazar.webs;

import com.piotrglazar.webs.config.Settings;
import com.piotrglazar.webs.model.Account;
import com.piotrglazar.webs.model.Currency;
import com.piotrglazar.webs.model.SavingsAccount;
import com.piotrglazar.webs.model.WebsNews;
import com.piotrglazar.webs.model.WebsUser;
import org.hsqldb.jdbc.JDBCDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.math.BigDecimal;

@Configuration
@Profile("test")
public class DatabaseTestConfiguration {

    @Bean
    public DataSource dataSource() {
        return new SimpleDriverDataSource(new JDBCDriver(), "jdbc:hsqldb:file:/home/webs/test", "sa", "");
    }

    @Bean
    @Autowired
    public DatabasePopulator databaseSetup(final UserProvider userProvider, final WebsNewsProvider newsProvider) {
        return new DatabasePopulator(userProvider, newsProvider);
    }

    static class DatabasePopulator {

        private final UserProvider userProvider;

        private final WebsNewsProvider newsProvider;

        public DatabasePopulator(final UserProvider userProvider, final WebsNewsProvider newsProvider) {
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
            return WebsNews.builder()
                    .headline("Create new account!")
                    .body("Do you need new account to organise your money in a better way? You can do that with just one click - "
                            + "just choose currency and it's done")
                    .url("newAccount")
                    .urlText("Create new account")
                    .imgContent("img content 1")
                    .build();
        }

        private WebsNews accountsFeatureNews() {
            return WebsNews.builder()
                    .headline("Browse your accounts!")
                    .body("Webs is a modern bank. As a result you can explore all of its features online - you can do everything with just "
                            + "one click. It has never been easier to keep an eye of your money")
                    .url("accounts")
                    .urlText("Start browsing your accounts")
                    .imgContent("img content 2")
                    .build();
        }

        private void addUserWithAccounts(String username, String email, String firstAccountNo, String secondAccountNo, boolean isAdmin) {
            WebsUser websUser = userProvider.createUser(username, Settings.PASSWORD);
            websUser.setEmail(email);

            final Account firstAccount = SavingsAccount.builder().number(firstAccountNo).balance(BigDecimal.TEN)
                    .currency(Currency.PLN).interest(BigDecimal.valueOf(5.5)).build();
            websUser.getAccounts().add(firstAccount);
            websUser = userProvider.update(websUser);

            final Account secondAccount = SavingsAccount.builder().number(secondAccountNo).balance(new BigDecimal("22.22"))
                    .currency(Currency.PLN).interest(BigDecimal.valueOf(4.5)).build();
            websUser.getAccounts().add(secondAccount);
            websUser = userProvider.update(websUser);

            if (isAdmin) {
                websUser.getRoles().add(Settings.ADMIN_ROLE_DB);
                userProvider.update(websUser);
            }
        }
    }
}
