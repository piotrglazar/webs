package com.piotrglazar.webs;

import com.piotrglazar.webs.config.Settings;
import org.hsqldb.jdbc.JDBCDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

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
                userProvider.createUser(Settings.USERNAME, Settings.PASSWORD);
            }
        }
    }
}
