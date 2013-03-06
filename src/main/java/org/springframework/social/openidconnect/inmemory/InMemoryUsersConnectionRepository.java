package org.springframework.social.openidconnect.inmemory;

/*
 * Copyright 2011 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

import org.springframework.social.connect.*;

import java.util.*;

/**
 * {@link org.springframework.social.connect.UsersConnectionRepository} that stores Connection data in a simple in-memory map structure
 *
 * @author Michael Lavelle
 */
public class InMemoryUsersConnectionRepository implements UsersConnectionRepository {

    /**
     * User Connection factory locator
     */
    protected final ConnectionFactoryLocator connectionFactoryLocator;

    /**
     * Signup information
     */
    private ConnectionSignUp connectionSignUp;

    /**
     * Repositories map
     */
    protected SortedMap<String, InMemoryConnectionRepository> connectionRepositoriesByUserId;

    /**
     * connection factory locator is needed to find existing connections.
     *
     * @param connectionFactoryLocator - {@link org.springframework.social.connect.ConnectionFactoryLocator}
     */
    public InMemoryUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.connectionRepositoriesByUserId = new TreeMap<String, InMemoryConnectionRepository>();
    }

    /**
     * Adds connection data into repository if exists or creates new one.
     *
     * @param userId - User id
     * @param connectionData - Given connection data
     * @param rank - value which aids on sorting.
     */
    public void addConnectionData(String userId, ConnectionData connectionData, int rank) {
        createInMemoryConnectionRepository(userId).addConnectionData(connectionData, rank);
    }

    /**
     * The command to execute to create a new local user profile in the event no user id could be mapped to a
     * connection. Allows for implicitly creating a user profile from connection data during a provider sign-in attempt.
     * Defaults to null, indicating explicit sign-up will be required to complete the provider sign-in attempt.
     *
     * @see #findUserIdsWithConnection(org.springframework.social.connect.Connection)
     */
    public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
        this.connectionSignUp = connectionSignUp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.social.connect.UsersConnectionRepository#findUserIdsWithConnection(org.springframework.social
     * .connect.Connection)
     */
    @Override
    public List<String> findUserIdsWithConnection(Connection<?> connection) {
        ConnectionKey key = connection.getKey();
        List<String> localUserIds = new ArrayList<String>();
        for (Map.Entry<String, InMemoryConnectionRepository> entry : connectionRepositoriesByUserId.entrySet()) {
            if (entry.getValue().hasConnection(key)) {
                localUserIds.add(entry.getKey());
            }
        }
        if (localUserIds.size() == 0 && connectionSignUp != null) {
            String newUserId = connectionSignUp.execute(connection);
            if (newUserId != null) {
                createConnectionRepository(newUserId).addConnection(connection);
                return Arrays.asList(newUserId);
            }
        }
        return localUserIds;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.social.connect.UsersConnectionRepository#findUserIdsConnectedTo(java.lang.String,
     * java.util.Set)
     */
    @Override
    public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        final Set<String> localUserIds = new HashSet<String>();
        for (Map.Entry<String, InMemoryConnectionRepository> entry : connectionRepositoriesByUserId.entrySet()) {
            for (String providerUserId : providerUserIds) {
                if (entry.getValue().hasConnection(new ConnectionKey(providerId, providerUserId))) {
                    localUserIds.add(entry.getKey());
                }
            }
        }
        return localUserIds;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.social.connect.UsersConnectionRepository#createConnectionRepository(java.lang.String)
     */
    @Override
    public ConnectionRepository createConnectionRepository(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }
        return createInMemoryConnectionRepository(userId);
    }

    /**
     * Creates in memory connection repository for given user id
     * 
     * @param userId - User id
     * @return - User Connection repository.
     */
    protected InMemoryConnectionRepository createInMemoryConnectionRepository(String userId) {
        InMemoryConnectionRepository connectionRepository = connectionRepositoriesByUserId.get(userId);
        if (connectionRepository == null) {
            connectionRepository = new InMemoryConnectionRepository(userId, connectionFactoryLocator);
            connectionRepositoriesByUserId.put(userId, connectionRepository);
        }

        return connectionRepository;
    }

}