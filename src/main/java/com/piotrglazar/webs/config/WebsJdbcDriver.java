package com.piotrglazar.webs.config;

import com.google.common.collect.Sets;
import org.hsqldb.jdbc.JDBCDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;

public class WebsJdbcDriver extends JDBCDriver {

    private static final Logger LOG = LoggerFactory.getLogger("JDBC");

    private final Set<Connection> activeConnections = Sets.newConcurrentHashSet();

    @Override
    public Connection connect(final String url, final Properties info) throws SQLException {
        LOG.info("obtaining connection");
        final WebsJdbcConnection websJdbcConnection = new WebsJdbcConnection(super.connect(url, info), activeConnections::remove);

        activeConnections.add(websJdbcConnection);

        return websJdbcConnection;
    }

    public int numberOfActiveConnections() {
        return activeConnections.size();
    }
}
