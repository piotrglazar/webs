package com.piotrglazar.webs;

import com.piotrglazar.webs.config.Settings;
import com.piotrglazar.webs.model.Account;
import com.piotrglazar.webs.model.Currency;
import com.piotrglazar.webs.model.SavingsAccount;
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
    public DatabasePopulator databaseSetup(final UserProvider userProvider) {
        return new DatabasePopulator(userProvider);
    }

    static class DatabasePopulator {

        private final UserProvider userProvider;

        public DatabasePopulator(final UserProvider userProvider) {
            this.userProvider = userProvider;
        }

        @PostConstruct
        public void setupInitialData() {
            if (userProvider.findUser(Settings.USERNAME) == null) {
                addUserWithAccounts(Settings.USERNAME, "piotr.glazar@gmail.com", "abc123", "def456");
            }

            if (userProvider.findUser(Settings.USERNAME2) == null) {
                addUserWithAccounts(Settings.USERNAME2, "pglazar@wp.pl", "ghi123", "jkl456");
            }
        }

        private void addUserWithAccounts(String username, String email, String firstAccountNo, String secondAccountNo) {
            WebsUser websUser = userProvider.createUser(username, Settings.PASSWORD);
            websUser.setEmail(email);

            final Account firstAccount = SavingsAccount.builder().number(firstAccountNo).balance(BigDecimal.TEN)
                    .currency(Currency.PLN).interest(BigDecimal.valueOf(5.5)).build();
            websUser.getAccounts().add(firstAccount);
            websUser = userProvider.update(websUser);

            final Account secondAccount = SavingsAccount.builder().number(secondAccountNo).balance(new BigDecimal("22.22"))
                    .currency(Currency.PLN).interest(BigDecimal.valueOf(4.5)).build();
            websUser.getAccounts().add(secondAccount);
            userProvider.update(websUser);
        }
    }
}
