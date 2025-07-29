package de.icongmbh.org.hibernate.connection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.HibernateException;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.jdbc.connections.internal.ConnectionProviderInitiator;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.jndi.spi.JndiService;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;

/**
 * #723270
 * Obtém uma {@link Connection} de um {@link ConnectionProvider} delegado
 * e ajusta client info (application e client name) via JDBC ou chamadas Oracle.
 */
public class NamedConnectionProvider
        implements ConnectionProvider, Configurable, ServiceRegistryAwareService {

    private static final long serialVersionUID = 5462939359707638322L;
    private static final String ORACLE_DATABASE_NAME = "Oracle";

    static final String CONNECTION_PROVIDER_DELEGATE = Environment.CONNECTION_PROVIDER + ".delegate_class";
    static final String APPLICATION_NAME_KEY = "ApplicationName";
    static final String CLIENT_NAME_KEY      = "ClientUser";

    private ConnectionProvider delegate;
    private Boolean clientInfoSupported = null;
    private final Properties clientInfoProperties = new Properties();
    private ServiceRegistryImplementor serviceRegistry;

    @Override
    public void configure(Map<String,Object> configurationValues) throws HibernateException {
        // Remove provider original e permite delegate via propriedade delegate_class
        configurationValues.remove(AvailableSettings.CONNECTION_PROVIDER);
        String delegateClass = ConfigurationHelper.getString(CONNECTION_PROVIDER_DELEGATE, configurationValues);
        if (StringUtils.isNotBlank(delegateClass)) {
            configurationValues.put(AvailableSettings.CONNECTION_PROVIDER, delegateClass);
        }

        // Lê application/client name
        clientInfoProperties.setProperty(
                APPLICATION_NAME_KEY,
                ConfigurationHelper.getString(
                        Environment.CONNECTION_PREFIX + '.' + APPLICATION_NAME_KEY,
                        configurationValues,
                        ""
                )
        );
        clientInfoProperties.setProperty(
                CLIENT_NAME_KEY,
                ConfigurationHelper.getString(
                        Environment.CONNECTION_PREFIX + '.' + CLIENT_NAME_KEY,
                        configurationValues,
                        ""
                )
        );

        // Inicializa o delegate
        this.delegate = initializeConnectionProvider(configurationValues);

        // Injeta services no delegate, se suportado
        if (delegate instanceof ServiceRegistryAwareService) {
            ((ServiceRegistryAwareService) delegate).injectServices(serviceRegistry);
        }
        // Configura delegate, incluindo JNDI se for DatasourceProvider
        if (delegate instanceof Configurable) {
            if (delegate instanceof DatasourceConnectionProviderImpl) {
                JndiService jndi = serviceRegistry.getService(JndiService.class);
                ((DatasourceConnectionProviderImpl) delegate).setJndiService(jndi);
            }
            ((Configurable) delegate).configure(configurationValues);
        }
    }

    private ConnectionProvider initializeConnectionProvider(Map<String,Object> cfg) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(cfg);
        ServiceRegistryImplementor registry = (ServiceRegistryImplementor) builder.build();
        this.serviceRegistry = registry;
        return ConnectionProviderInitiator.INSTANCE.initiateService(cfg, registry);
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = delegate.getConnection();
        Validate.notNull(conn, "Connection must not be null.");
        return isClientInfoSupported()
                ? setClientInfo(conn)
                : conn;
    }

    @Override
    public void closeConnection(Connection conn) throws SQLException {
        delegate.closeConnection(conn);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return delegate.supportsAggressiveRelease();
    }

    private boolean isOracle(Connection conn) {
        try {
            return ORACLE_DATABASE_NAME
                    .equals(conn.getMetaData().getDatabaseProductName());
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean supportsClientInfoProperties(Connection conn) {
        boolean supported = false;
        try {
            supported = conn.getMetaData().getClientInfoProperties().next();
        } catch (SQLException e) {
            // ignora
        }
        this.clientInfoSupported = supported;
        return supported;
    }

    private Connection setClientInfo(Connection conn) {
        if (isOracle(conn)) {
            setClientInfoOnOracle(conn);
        }
        else if (supportsClientInfoProperties(conn)) {
            try {
                conn.setClientInfo(clientInfoProperties);
            } catch (SQLClientInfoException e) {
                // ignora
            }
        }
        return conn;
    }

    private void setClientInfoOnOracle(Connection conn) {
        try (
            CallableStatement mod  = conn.prepareCall("{ call DBMS_APPLICATION_INFO.SET_MODULE(?,NULL) }");
            CallableStatement ident = conn.prepareCall("{ call DBMS_SESSION.SET_IDENTIFIER(?) }")
        ) {
            mod.setString(1, clientInfoProperties.getProperty(APPLICATION_NAME_KEY, ""));
            ident.setString(1, clientInfoProperties.getProperty(CLIENT_NAME_KEY, ""));
            mod.execute();
            ident.execute();
            clientInfoSupported = true;
        } catch (SQLException e) {
            clientInfoSupported = false;
        }
    }

    private boolean isClientInfoSupported() {
        return clientInfoSupported == null || clientInfoSupported;
    }

    @Override
    public void injectServices(ServiceRegistryImplementor serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        // permite unwrapping tanto do próprio provider quanto do delegate
        if (unwrapType.isAssignableFrom(getClass())) {
            return true;
        }
        return delegate.isUnwrappableAs(unwrapType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> unwrapType) {
        if (unwrapType.isAssignableFrom(getClass())) {
            return (T) this;
        }
        if (delegate.isUnwrappableAs(unwrapType)) {
            return delegate.unwrap(unwrapType);
        }
        throw new UnknownUnwrapTypeException(unwrapType);
    }

    // getters opcionais
    public ConnectionProvider getDelegate() {
        return delegate;
    }

    public Properties getClientInfoProperties() {
        return clientInfoProperties;
    }
}
