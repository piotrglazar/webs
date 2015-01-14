package com.piotrglazar.webs.config;

import org.springframework.boot.autoconfigure.jdbc.metadata.AbstractDataSourcePoolMetadata;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

public class WebsDataSourcePoolMetadata extends AbstractDataSourcePoolMetadata<SimpleDriverDataSource> {

    public static final int MAXIMUM_NUMBER_OF_CONNECTIONS = 1;
    public static final int MINIMUM_NUMBER_OF_CONNECTIONS = 0;
    public static final String VALIDATION_QUERY = null;
    private final WebsJdbcDriver websJdbcDriver;

    public WebsDataSourcePoolMetadata(DataSource dataSource) {
        super((SimpleDriverDataSource) dataSource);
        this.websJdbcDriver = (WebsJdbcDriver) ((SimpleDriverDataSource) dataSource).getDriver();
    }

    @Override
    public Integer getActive() {
        return websJdbcDriver.numberOfActiveConnections();
    }

    @Override
    public Integer getMax() {
        return MAXIMUM_NUMBER_OF_CONNECTIONS;
    }

    @Override
    public Integer getMin() {
        return MINIMUM_NUMBER_OF_CONNECTIONS;
    }

    @Override
    public String getValidationQuery() {
        return VALIDATION_QUERY;
    }
}
