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
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.DuplicateConnectionException;

import java.util.*;

/**
 * Stroes user connection data such as provider data and connection data in memory instead of database.
 * 
 * @author Michael Lavelle
 */
public class InMemoryProviderConnectionRepository {
    /**
     * Logger for {@link InMemoryProviderConnectionRepository}
     */
    private static final Logger logger = Logger.getLogger(InMemoryProviderConnectionRepository.class);


    /**
     * Stores {@linkplain org.springframework.social.connect.ConnectionData} base don given rank
     */
    protected SortedMap<Integer, ConnectionData> connectionDataByRank = new TreeMap<Integer, ConnectionData>();

    /**
     * User id.
     */
    private final String userId;

    /**
     * Service provider id(in our case it is PayPal Access)
     */
    private final String providerId;

    /**
     * Constructor which accepts user id for given provider
     *
     * @param userId - User id
     * @param providerId - Service provider id
     */
    public InMemoryProviderConnectionRepository(String userId, String providerId) {
        this.userId = userId;
        this.providerId = providerId;
    }

    /**
     * Checks if given user is present for a provider.
     *
     * @param providerUserId - user id
     * @return - true if user exists
     */
    public boolean hasProviderUserId(String providerUserId) {
        return findByProviderUserId(providerUserId) != null;
    }

    /**
     * Gets all connection data as a list.
     *
     * @return - List of connections
     */
    public List<ConnectionData> findAllOrderByRank() {
        List<ConnectionData> connectionDatas = new ArrayList<ConnectionData>();
        connectionDatas.addAll(connectionDataByRank.values());
        return connectionDatas;
    }

    /**
     * Checks if connection data exits for a given user id and provider id
     *
     * @param providerUserId - provider and user id.
     * @return - {@link org.springframework.social.connect.ConnectionData}
     */
    public ConnectionData findByProviderUserId(String providerUserId) {
        for (ConnectionData connectionData : connectionDataByRank.values()) {
            if (connectionData.getProviderUserId().equals(providerUserId)) {
                return connectionData;
            }
        }
        return null;
    }

    /**
     * Gets Connection data given rank
     *
     * @param rank - value aided in sorting
     * @return - {@link org.springframework.social.connect.ConnectionData}
     */
    public ConnectionData findByRank(int rank) {
        return connectionDataByRank.get(rank);
    }

    /**
     * Removes connection data for a given user for a provider
     *
     * @param providerUserId - User id and provider id combination
     */
    public void deleteByProviderUserId(String providerUserId) {
        Integer rankOfMatchingConnectionData = null;
        for (Map.Entry<Integer, ConnectionData> connectionDataWithRank : connectionDataByRank.entrySet()) {
            if (connectionDataWithRank.getValue().getProviderUserId().equals(providerUserId)) {
                rankOfMatchingConnectionData = connectionDataWithRank.getKey();
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Removing connectionData in map for providerUserId:"+providerUserId);
        }
        if (rankOfMatchingConnectionData != null) {
            connectionDataByRank.remove(rankOfMatchingConnectionData);
        }

    }

    /**
     * Resets connection repository.
     */
    public void deleteAll() {

        if (logger.isDebugEnabled()) {
            logger.debug("Removing connectionData for all providers");
        }
        connectionDataByRank = new TreeMap<Integer, ConnectionData>();
    }

    /**
     * Updates a given connection for a given provider user id combo.
     *
     * @param connection - {@linkplain org.springframework.social.connect.Connection}
     * @param providerUserId
     */
    public void updateByProviderUserId(ConnectionData connection, String providerUserId) {

        if (logger.isDebugEnabled()) {
            logger.debug("UpdateByProviderUserid for providerUserId:"+providerUserId);
        }
        try {
            for (Map.Entry<Integer, ConnectionData> cd : connectionDataByRank.entrySet()) {
                ConnectionData tempConnectionData = cd.getValue();
                if (tempConnectionData.getProviderUserId().equals(providerUserId)) {
                    connectionDataByRank.put(cd.getKey(), connection);
                }
            }
        } catch (Exception exc) {
            logger.error("Exception thrown while updating connection data for providerUserId-" + providerUserId, exc);
            throw new InMemoryDataAccessException("Exception thrown while updating connection data for providerUserId-" + providerUserId, exc);
        }
    }

    /**
     * Gets the connection data sotred by provider id and rank
     *
     * @param providerUserIdsByProviderId - Provider and user id by rank
     * @return - List of connection data.
     */
    public List<ConnectionData> findByProviderUserIdsOrderByProviderIdAndRank(List<String> providerUserIdsByProviderId) {
        List<ConnectionData> returnConnectionDatas = new ArrayList<ConnectionData>();
        for (ConnectionData connectionData : connectionDataByRank.values()) {
            if (providerUserIdsByProviderId.contains(connectionData.getProviderUserId())) {
                returnConnectionDatas.add(connectionData);
            }
        }
        return returnConnectionDatas;
    }

    /**
     * Adds a given connection data into repository
     *
     * @param connectionData - Given connection data
     * @throws org.springframework.social.connect.DuplicateConnectionException - If given connection already exits5
     */
    public void add(ConnectionData connectionData) throws DuplicateConnectionException {
        for (ConnectionData cd : connectionDataByRank.values()) {
            if (cd.getProviderUserId().equals(connectionData.getProviderUserId())) {
                throw new DuplicateConnectionException(new ConnectionKey(connectionData.getProviderId(),
                        connectionData.getProviderUserId()));
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Adding connectionData in map for providerUserId:"+connectionData.getProviderUserId());
        }
        connectionDataByRank.put(getNextRank(), connectionData);

    }

    /**
     * Adds a given connection into a specific rank
     *
     * @param connectionData - Given connection data
     * @param rank - value which aids in sorting
     * @throws org.springframework.social.connect.DuplicateConnectionException - If given connection already exists
     */
    public void add(ConnectionData connectionData, int rank) throws DuplicateConnectionException {
        for (ConnectionData cd : connectionDataByRank.values()) {
            if (cd.getProviderUserId().equals(connectionData.getProviderUserId())) {
                throw new DuplicateConnectionException(new ConnectionKey(connectionData.getProviderId(),
                        connectionData.getProviderUserId()));
            }
        }
        connectionDataByRank.put(rank, connectionData);

    }

    /**
     * Gets next rank based on no of existing connections.
     * 
     * @return - Next rank.
     */
    protected int getNextRank() {
        Integer maxRank = null;
        for (Integer rank : connectionDataByRank.keySet()) {
            if (maxRank == null || rank.intValue() > maxRank.intValue()) {
                maxRank = rank.intValue();
            }
        }
        return maxRank == null ? 1 : (maxRank.intValue() + 1);
    }

}
