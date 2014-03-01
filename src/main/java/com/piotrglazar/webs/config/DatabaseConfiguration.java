package com.piotrglazar.webs.config;

import com.piotrglazar.webs.UserProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Driver;

@Configuration
@EnableJpaRepositories
@Profile("default")
public class DatabaseConfiguration {

    @Bean
    public DataSource dataSource() throws ClassNotFoundException {
        final Class<? extends Driver> driver = (Class<? extends Driver>)
                ClassUtils.forName("org.hsqldb.jdbcDriver", DatabaseConfiguration.class.getClassLoader());
        return new SimpleDriverDataSource(BeanUtils.instantiateClass(driver), "jdbc:hsqldb:file:/home/webs/production", "sa", "");
    }

    @Bean
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(final DataSource dataSource,
                                                                       final JpaVendorAdapter jpaVendorAdapter) {
        final LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource);
        bean.setJpaVendorAdapter(jpaVendorAdapter);
        bean.setPackagesToScan("com.piotrglazar.webs");
        return bean;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        final HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setDatabase(Database.HSQL);
        return jpaVendorAdapter;
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
