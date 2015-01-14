package com.piotrglazar.webs.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.inOrder;

@RunWith(MockitoJUnitRunner.class)
public class WebsJdbcConnectionTest {

    @Mock
    private WebsJdbcConnectionCallback closeCallback;

    @Mock
    private Connection connection;

    @InjectMocks
    private WebsJdbcConnection websJdbcConnection;

    @Test
    public void shouldCallCloseCallbackBeforeClosingConnection() throws SQLException {
        // when
        websJdbcConnection.close();

        // then
        final InOrder inOrder = inOrder(closeCallback, connection);
        inOrder.verify(closeCallback).action(websJdbcConnection);
        inOrder.verify(connection).close();
    }
}
