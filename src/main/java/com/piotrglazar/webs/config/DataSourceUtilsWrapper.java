package com.piotrglazar.webs.config;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DataSourceUtilsWrapper implements Serializable {

    public void resetConnectionAfterTransaction(Connection connection, Integer previousIsolationLevel) {
        DataSourceUtils.resetConnectionAfterTransaction(connection, previousIsolationLevel);
    }

    public Integer prepareConnectionForTransaction(TransactionDefinition definition, Connection connection) throws SQLException {
        return DataSourceUtils.prepareConnectionForTransaction(connection, definition);
    }
}
