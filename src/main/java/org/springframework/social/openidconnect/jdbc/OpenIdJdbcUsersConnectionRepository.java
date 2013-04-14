package org.springframework.social.openidconnect.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;

import javax.sql.DataSource;

public class OpenIdJdbcUsersConnectionRepository extends JdbcUsersConnectionRepository {

    private final JdbcTemplate jdbcTemplate;

    private final ConnectionFactoryLocator connectionFactoryLocator;

    private final TextEncryptor textEncryptor;

    private String tablePrefix = "";


    public OpenIdJdbcUsersConnectionRepository(DataSource dataSource, ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor) {
        super(dataSource, connectionFactoryLocator, textEncryptor);

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.textEncryptor = textEncryptor;
    }

    /**
     * Sets a table name prefix. This will be prefixed to all the table names before queries are executed. Defaults to "".
     * This is can be used to qualify the table name with a schema or to distinguish Spring Social tables from other application tables.
     *
     * @param tablePrefix the tablePrefix to set
     */
    @Override
    public void setTablePrefix(String tablePrefix) {
        super.setTablePrefix(tablePrefix);
        this.tablePrefix = tablePrefix;
    }

    /**
     * The command to execute to create a new local user profile in the event no user id could be mapped to a connection.
     * Allows for implicitly creating a user profile from connection data during a provider sign-in attempt.
     * Defaults to null, indicating explicit sign-up will be required to complete the provider sign-in attempt.
     *
     * @see #findUserIdsWithConnection(org.springframework.social.connect.Connection)
     */
    @Override
    public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
        super.setConnectionSignUp(connectionSignUp);
    }

    /**
     * Creates a OpenId Specific JDBC connection repository.  Need to store id_token which is not part of OAuth2 specification.
     * @param userId - User ID
     * @return  OpenIdConnectJdbcConnectionRepository
     */
    @Override
    public ConnectionRepository createConnectionRepository(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }
        return new OpenIdConnectJdbcConnectionRepository(userId, jdbcTemplate, connectionFactoryLocator, textEncryptor, tablePrefix);
    }
}
