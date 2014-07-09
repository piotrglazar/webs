package com.piotrglazar.webs.config;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.TransactionDefinition;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.sql.Connection;
import java.sql.SQLException;

import static com.piotrglazar.webs.config.IsolationSupportHibernateJpaDialect.IsolationSupportSessionTransactionData;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class IsolationSupportHibernateJpaDialectTest {

    @Mock
    private EntityTransaction entityTransaction;

    @Mock
    private Connection connection;

    @Mock
    private Transaction transaction;

    @Mock
    private SessionImplForTest session;

    @Mock
    private DataSourceUtilsWrapper dataSourceUtilsWrapper;

    @Mock
    private EntityManager entityManager;

    @Mock
    private TransactionDefinition definition;

    @InjectMocks
    private IsolationSupportHibernateJpaDialect dialect;

    @Test
    @Ignore("Due to tricky implementation of HibernateJpaDialect.HibernateConnectionHandle this test can be run only manually")
    public void shouldSetIsolationLevelForNewTransaction() throws SQLException {
        // given
        given(definition.getTimeout()).willReturn(0);
        given(entityManager.unwrap(Session.class)).willReturn(session);
        given(entityManager.getTransaction()).willReturn(entityTransaction);
        given(session.connection()).willReturn(connection);
        given(session.getTransaction()).willReturn(transaction);
        given(session.getFlushMode()).willReturn(FlushMode.AUTO);
        given(dataSourceUtilsWrapper.prepareConnectionForTransaction(eq(definition), eq(connection)))
                .willReturn(0);

        // when
        dialect.beginTransaction(entityManager, definition);

        // then
        // this forwards the call to DataSourceUtils which actually sets connection isolation level
        verify(dataSourceUtilsWrapper).prepareConnectionForTransaction(eq(definition), eq(connection));
    }

    @Test
    public void shouldResetIsolationLevelAfterTransaction() {
        // given
        final int previousIsolationLevel = TransactionDefinition.ISOLATION_SERIALIZABLE;
        IsolationSupportSessionTransactionData data = new IsolationSupportSessionTransactionData(new Object(), previousIsolationLevel,
                connection, dataSourceUtilsWrapper);

        // when
        data.resetIsolationLevel();

        // then
        verify(dataSourceUtilsWrapper).resetConnectionAfterTransaction(connection, previousIsolationLevel);
    }

    private static interface SessionImplForTest extends Session {

        Connection connection();
    }
}
