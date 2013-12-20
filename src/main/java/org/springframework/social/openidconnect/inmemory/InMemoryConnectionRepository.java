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

import org.apache.log4j.Logger;
import org.springframework.social.connect.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.Map.Entry;

/**
 * Repository which manages user connections in memory instead of database.
 * 
 * @author Michael Lavelle
 */
public class InMemoryConnectionRepository implements ConnectionRepository {
    /**
     * Logger for {@link InMemoryConnectionRepository}
     */
    private static final Logger logger = Logger.getLogger(InMemoryConnectionRepository.class);

    /**
     * user identifier
     */
    protected final String userId;

    /**
     * Connection factory locator.
     */
    protected final ConnectionFactoryLocator connectionFactoryLocator;

    /**
     * Map which holds user connections
     */
    protected SortedMap<String, InMemoryProviderConnectionRepository> providerRepositories = new TreeMap<String, InMemoryProviderConnectionRepository>();

    /**
     * Constructor which requires a locator and user id to find connection.
     * 
     * @param userId - PayPal Access user id
     * @param connectionFactoryLocator - Connection Factory locator.
     */
    public InMemoryConnectionRepository(String userId, ConnectionFactoryLocator connectionFactoryLocator) {
        this.userId = userId;
        this.connectionFactoryLocator = connectionFactoryLocator;
    }

    /**
     * Gets connection repository if it already exists for given provider id. Else creates a new one for given provider
     * and returns.
     * 
     * @param providerId - OpenId Connect service provider id
     * @return - Connection repository
     */
    public InMemoryProviderConnectionRepository getInMemoryProviderConnectionRepository(String providerId) {
        InMemoryProviderConnectionRepository repository = providerRepositories.get(providerId);
        if (repository == null) {
            repository = new InMemoryProviderConnectionRepository(userId, providerId);
            providerRepositories.put(providerId, repository);
        }
        return repository;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.social.connect.ConnectionRepository#findAllConnections()
     */
    @Override
    public MultiValueMap<String, Connection<?>> findAllConnections() {

        List<ConnectionData> connectionData = new ArrayList<ConnectionData>();

        for (Entry<String, InMemoryProviderConnectionRepository> providerConnectionRepository : providerRepositories
                .entrySet()) {
            connectionData.addAll(providerConnectionRepository.getValue().findAllOrderByRank());
        }
        List<Connection<?>> resultList = createConnections(connectionData);

        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        Set<String> registeredProviderIds = connectionFactoryLocator.registeredProviderIds();
        for (String registeredProviderId : registeredProviderIds) {
            connections.put(registeredProviderId, Collections.<Connection<?>> emptyList());
        }
        for (Connection<?> connection : resultList) {
            String providerId = connection.getKey().getProviderId();
            if (connections.get(providerId).size() == 0) {
                connections.put(providerId, new LinkedList<Connection<?>>());
            }
            connections.add(providerId, connection);
        }

        return connections;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.social.connect.ConnectionRepository#findConnections(java.lang.String)
     */
    @Override
    public List<Connection<?>> findConnections(String providerId) {
        return createConnections(getInMemoryProviderConnectionRepository(providerId).findAllOrderByRank());

    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.social.connect.ConnectionRepository#findConnections(java.lang.Class)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <A> List<Connection<A>> findConnections(Class<A> apiType) {
        List<?> connections = findConnections(getProviderId(apiType));
        return (List<Connection<A>>) connections;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.social.connect.ConnectionRepository#findConnectionsToUsers(org.springframework.util.MultiValueMap
     * )
     */
    @Override
    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUsers) {
        if (providerUsers == null || providerUsers.isEmpty()) {
            throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
        }

        Map<String, List<String>> providerUserIdsByProviderId = new HashMap<String, List<String>>();
        for (Iterator<Entry<String, List<String>>> it = providerUsers.entrySet().iterator(); it.hasNext();) {
            Entry<String, List<String>> entry = it.next();
            String providerId = entry.getKey();
            providerUserIdsByProviderId.put(providerId, entry.getValue());
        }

        List<ConnectionData> connectionDatas = new ArrayList<ConnectionData>();
        for (Entry<String, List<String>> entry : providerUserIdsByProviderId.entrySet()) {
            connectionDatas.addAll(getInMemoryProviderConnectionRepository(entry.getKey())
                    .findByProviderUserIdsOrderByProviderIdAndRank(entry.getValue()));
        }

        List<Connection<?>> resultList = createConnections(connectionDatas);
        MultiValueMap<String, Connection<?>> connectionsForUsers = new LinkedMultiValueMap<String, Connection<?>>();
        for (Connection<?> connection : resultList) {
            String providerId = connection.getKey().getProviderId();
            List<String> userIds = providerUsers.get(providerId);
            List<Connection<?>> connections = connectionsForUsers.get(providerId);
            if (connections == null) {
                connections = new ArrayList<Connection<?>>(userIds.size());
                for (int i = 0; i < userIds.size(); i++) {
                    connections.add(null);
                }
                connectionsForUsers.put(providerId, connections);
            }
            String providerUserId = connection.getKey().getProviderUserId();
            int connectionIndex = userIds.indexOf(providerUserId);
            connections.set(connectionIndex, connection);
        }
        return connectionsForUsers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.social.connect.ConnectionRepository#getConnection(org.springframework.social.connect.
     * ConnectionKey)
     */
    @Override
    public Connection<?> getConnection(ConnectionKey connectionKey) {

        ConnectionData connectionData = getInMemoryProviderConnectionRepository(connectionKey.getProviderId())
                .findByProviderUserId(connectionKey.getProviderUserId());

        if (connectionData == null) {
            throw new NoSuchConnectionException(connectionKey);
        } else {
            return createConnection(connectionData);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.social.connect.ConnectionRepository#getConnection(java.lang.Class, java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
        String providerId = getProviderId(apiType);
        return (Connection<A>) getConnection(new ConnectionKey(providerId, providerUserId));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.social.connect.ConnectionRepository#getPrimaryConnection(java.lang.Class)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
        String providerId = getProviderId(apiType);
        Connection<A> connection = (Connection<A>) findPrimaryConnection(providerId);
        if (connection == null) {
            throw new NotConnectedException(providerId);
        }
        return connection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.social.connect.ConnectionRepository#findPrimaryConnection(java.lang.Class)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
        String providerId = getProviderId(apiType);
        return (Connection<A>) findPrimaryConnection(providerId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.social.connect.ConnectionRepository#removeConnections(java.lang.String)
     */
    @Override
    public void removeConnections(String providerId) {
        getInMemoryProviderConnectionRepository(providerId).deleteAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.social.connect.ConnectionRepository#removeConnection(org.springframework.social.connect.
     * ConnectionKey)
     */
    @Override
    public void removeConnection(ConnectionKey connectionKey) {
        getInMemoryProviderConnectionRepository(connectionKey.getProviderId()).deleteByProviderUserId(
                connectionKey.getProviderUserId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.social.connect.ConnectionRepository#addConnection(org.springframework.social.connect.Connection
     * )
     */
    @Override
    public void addConnection(Connection<?> connection) {
        ConnectionData connectionData = connection.createData();
        getInMemoryProviderConnectionRepository(connectionData.getProviderId()).add(connectionData);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.social.connect.ConnectionRepository#updateConnection(org.springframework.social.connect.
     * Connection)
     */
    @Override
    public void updateConnection(Connection<?> connection) {
        ConnectionData data = connection.createData();
        getInMemoryProviderConnectionRepository(data.getProviderId()).updateByProviderUserId(data,
                data.getProviderUserId());
    }

    /**
     * Gets the primary connection for a given provider id.
     * 
     * @param providerId - Given provider id
     * @return user connection
     */
    private Connection<?> findPrimaryConnection(String providerId) {
        ConnectionData connectionData = getInMemoryProviderConnectionRepository(providerId).findByRank(1);
        if (connectionData != null) {
            return createConnection(connectionData);
        } else {
            return null;
        }
    }

    /**
     * Creates a connection to given connection data
     * 
     * @param connectionData - Instance which holds connection information such as refresh token, provider id, user id
     *            etc.
     * @return - new Connection
     */
    protected Connection<?> createConnection(ConnectionData connectionData) {
        ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(connectionData
                .getProviderId());
        return connectionFactory.createConnection(connectionData);
    }

    /**
     * Creates connections for the given list of connection data.
     * 
     * @param connectionDataList - Connection data list
     * @return - List of connections
     */
    protected List<Connection<?>> createConnections(List<ConnectionData> connectionDataList) {
        List<Connection<?>> connections = new ArrayList<Connection<?>>();
        for (ConnectionData connectionData : connectionDataList) {
            connections.add(createConnection(connectionData));
        }
        return connections;
    }

    /**
     * Gets the provider id for given connection type.
     * 
     * @param apiType - Service provider
     * @return - provider id
     */
    private <A> String getProviderId(Class<A> apiType) {
        return connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
    }

    /**
     * Adds the given connection data into repository at given rank.
     * 
     * @param connectionData
     * @param rank
     */
    public void addConnectionData(ConnectionData connectionData, int rank) {
        getInMemoryProviderConnectionRepository(connectionData.getProviderId()).add(connectionData, rank);
    }

    /**
     * Checks if given key has provider user id present.
     * 
     * @param key - connection stored user id
     * @return - true if connection exists for this user.
     */
    protected boolean hasConnection(ConnectionKey key) {
        InMemoryProviderConnectionRepository providerConnectionRepository = getInMemoryProviderConnectionRepository(key
                .getProviderId());
        return providerConnectionRepository.hasProviderUserId(key.getProviderUserId());
    }

}