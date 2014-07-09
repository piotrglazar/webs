package com.piotrglazar.webs.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class IsolationSupportHibernateJpaDialect extends HibernateJpaDialect {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DataSourceUtilsWrapper dataSourceUtilsWrapper;

    @Autowired
    public IsolationSupportHibernateJpaDialect(DataSourceUtilsWrapper dataSourceUtilsWrapper) {
        this.dataSourceUtilsWrapper = dataSourceUtilsWrapper;
    }

    /**
     * This method is overridden to set custom isolation levels on the
     * connection
     */
    @SuppressWarnings("deprecation")
    @Override
    public Object beginTransaction(final EntityManager entityManager, final TransactionDefinition definition)
            throws PersistenceException, TransactionException, SQLException {
        setTransactionTimeout(entityManager, definition);

        final Connection connection = getConnection(entityManager, definition);
        final Integer previousIsolationLevel = getPreviousIsolationLevel(definition, connection);

        beginTransaction(entityManager);

        Object transactionDataFromHibernateJpaTemplate = prepareTransaction(entityManager, definition.isReadOnly(), definition.getName());

        return new IsolationSupportSessionTransactionData(transactionDataFromHibernateJpaTemplate, previousIsolationLevel, connection,
                dataSourceUtilsWrapper);
    }

    private Connection getConnection(final EntityManager entityManager, final TransactionDefinition definition) throws SQLException {
        Connection connection = getJdbcConnection(entityManager, definition.isReadOnly()).getConnection();
        logConnectionAndTransactionDetails(definition, connection);
        return connection;
    }

    private void beginTransaction(final EntityManager entityManager) {
        entityManager.getTransaction().begin();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Transaction started");
        }
    }

    private Integer getPreviousIsolationLevel(final TransactionDefinition definition, final Connection connection) throws SQLException {
        final Integer previousIsolationLevel = dataSourceUtilsWrapper.prepareConnectionForTransaction(definition, connection);
        if (LOG.isInfoEnabled()){
            LOG.info("The previousIsolationLevel {}", previousIsolationLevel);
        }
        return previousIsolationLevel;
    }

    private void logConnectionAndTransactionDetails(TransactionDefinition definition, Connection connection) throws SQLException {
        if (LOG.isInfoEnabled()) {
            LOG.info("Connection Info: isolationlevel={}, instance={}", connection.getTransactionIsolation(), connection);
            LOG.info("Transaction Info: IsolationLevel={}, PropagationBehavior={}, Timeout={}, Name={}",
                    definition.getIsolationLevel(), definition.getPropagationBehavior(), definition.getTimeout(), definition.getName());
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("The isolation level of the connection is {} and the isolation level set on the transaction is {}",
                    connection.getTransactionIsolation(), definition.getIsolationLevel());
        }
    }

    private void setTransactionTimeout(final EntityManager entityManager, final TransactionDefinition definition) {
        if (definition.getTimeout() != TransactionDefinition.TIMEOUT_DEFAULT) {
            getSession(entityManager).getTransaction().setTimeout(definition.getTimeout());
        }
    }


    /* (non-Javadoc)
     * @see org.springframework.orm.jpa.vendor.HibernateJpaDialect#cleanupTransaction(java.lang.Object)
     */
    @Override
    public void cleanupTransaction(Object transactionData) {
        super.cleanupTransaction(((IsolationSupportSessionTransactionData) transactionData)
                .getSessionTransactionDataFromHibernateTemplate());
        ((IsolationSupportSessionTransactionData) transactionData).resetIsolationLevel();
    }

    protected static class IsolationSupportSessionTransactionData {

        private final Object sessionTransactionDataFromHibernateJpaTemplate;
        private final Integer previousIsolationLevel;
        private final Connection connection;
        private final DataSourceUtilsWrapper dataSourceUtilsWrapper;

        public IsolationSupportSessionTransactionData(Object sessionTransactionDataFromHibernateJpaTemplate, Integer previousIsolationLevel,
                                                      Connection connection, DataSourceUtilsWrapper dataSourceUtilsWrapper) {
            this.sessionTransactionDataFromHibernateJpaTemplate = sessionTransactionDataFromHibernateJpaTemplate;
            this.previousIsolationLevel = previousIsolationLevel;
            this.connection = connection;
            this.dataSourceUtilsWrapper = dataSourceUtilsWrapper;
        }

        public void resetIsolationLevel() {
            if (previousIsolationLevel != null) {
                dataSourceUtilsWrapper.resetConnectionAfterTransaction(connection, previousIsolationLevel);
            }
        }

        public Object getSessionTransactionDataFromHibernateTemplate(){
            return sessionTransactionDataFromHibernateJpaTemplate;
        }
    }
}
