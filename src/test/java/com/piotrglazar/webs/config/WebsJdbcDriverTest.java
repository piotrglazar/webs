package com.piotrglazar.webs.config;

import com.piotrglazar.webs.AbstractContextTest;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class WebsJdbcDriverTest extends AbstractContextTest {

    @Autowired
    private SimpleDriverDataSource dataSource;

    private Connection connection;

    @After
    public void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    public void shouldWrapConnectionIntoWebsConnection() throws SQLException {
        // given
        assertThat(dataSource.getDriver()).isInstanceOf(WebsJdbcDriver.class);

        // when
        connection = dataSource.getConnection();

        // then
        assertThat(connection).isInstanceOf(WebsJdbcConnection.class);
    }

    @Test
    public void shouldIncreaseConnectionCounterWhenConnectionIsCreated() throws SQLException {
        // given
        final WebsJdbcDriver websJdbcDriver = (WebsJdbcDriver) dataSource.getDriver();

        // when
        connection = dataSource.getConnection();

        // then
        assertThat(websJdbcDriver.numberOfActiveConnections()).isEqualTo(1);
    }

    @Test
    public void shouldDecreaseConnectionCounterWhenConnectionIsClosed() throws SQLException {
        // given
        final WebsJdbcDriver websJdbcDriver = (WebsJdbcDriver) dataSource.getDriver();

        // when
        connection = dataSource.getConnection();
        connection.close();

        // then
        assertThat(websJdbcDriver.numberOfActiveConnections()).isEqualTo(0);
    }
}
