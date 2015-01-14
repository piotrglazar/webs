package com.piotrglazar.webs;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.piotrglazar.webs.config.IsolationSupportHibernateJpaDialect;
import com.piotrglazar.webs.config.UtilityConfiguration;
import com.piotrglazar.webs.config.WebsJdbcDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;

@Configuration
@Profile("test")
public class DatabaseTestConfiguration {

    public static final ImmutableList<BigDecimal> ACCOUNT_BALANCES = ImmutableList.of(new BigDecimal(1000), new BigDecimal(4000));
    public static final ImmutableList<BigDecimal> ACCOUNT_INTERESTS = ImmutableList.of(new BigDecimal("5.5"), new BigDecimal("4.5"));

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Bean
    public DataSource dataSource() {
        final String currentDirectory = UtilityConfiguration.getCurrentDirectory();
        LOG.info("Creating test database in {}/db/test", currentDirectory);
        return new SimpleDriverDataSource(new WebsJdbcDriver(), "jdbc:hsqldb:file:" + currentDirectory + "/db/test", "sa", "");
    }

    @Bean
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter,
                                                                       IsolationSupportHibernateJpaDialect dialect) {
        final LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource);
        bean.setJpaVendorAdapter(jpaVendorAdapter);
        bean.setPackagesToScan("com.piotrglazar.webs");
        bean.setJpaPropertyMap(ImmutableMap.of("hibernate.hbm2ddl.auto", "create"));
        bean.setJpaDialect(dialect);
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
    public TestDatabasePopulator databaseSetup(final UserProvider userProvider, final WebsNewsProvider newsProvider) {
        return new TestDatabasePopulator(userProvider, newsProvider);
    }

}
