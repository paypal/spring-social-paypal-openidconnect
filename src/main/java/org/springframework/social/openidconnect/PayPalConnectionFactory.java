package org.springframework.social.openidconnect;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.openidconnect.api.PayPal;
import org.springframework.social.openidconnect.support.OpenIdAccessGrant;
import org.springframework.social.openidconnect.support.OpenIdConnection;
import org.springframework.social.openidconnect.support.OpenIdConnectionData;

/**
 * Connection Factory implementation for PayPal Access. Uses OAuth2 itself for OpenId as both protocols are pretty much
 * similar. Both defer in identity layer which we don't care.
 * 
 */
public class PayPalConnectionFactory extends OAuth2ConnectionFactory<PayPal> {

    private  PayPalServiceProvider serviceProvider;

    /**
     * 
     * Registers connection as 'paypal'. This key is used for managing connections by spring-social.
     * 
     * @param serviceProvider - {@link PayPalServiceProvider}
     */
    public PayPalConnectionFactory(PayPalServiceProvider serviceProvider) {
        super("paypal", serviceProvider, new PayPalAdapter());
        this.serviceProvider = serviceProvider;
    }

    /**
     * Create a OAuth2-based {@link org.springframework.social.connect.Connection} from the {@link org.springframework.social.oauth2.AccessGrant} returned after {@link #getOAuthOperations() completing the OAuth2 flow}.
     * @param accessGrant the access grant
     * @return the new service provider connection
     * @see org.springframework.social.oauth2.OAuth2Operations#exchangeForAccess(String, String, org.springframework.util.MultiValueMap)
     */
    public Connection<PayPal> createConnection(AccessGrant accessGrant) {
        OpenIdAccessGrant openIdAccessGrant = (OpenIdAccessGrant) accessGrant;
        return new OpenIdConnection<PayPal>(getProviderId(), extractProviderUserId(openIdAccessGrant), openIdAccessGrant.getAccessToken(),
                openIdAccessGrant.getRefreshToken(), openIdAccessGrant.getIdToken(), openIdAccessGrant.getExpireTime(), this.serviceProvider, getApiAdapter());
    }

    /**
     * Create a OAuth2-based {@link org.springframework.social.connect.Connection} from the connection data.
     */
    @Override
    public Connection<PayPal> createConnection(ConnectionData data) {
    	OpenIdConnectionData connectionData = null;
    	if(data instanceof OpenIdConnectionData) {
    		connectionData = (OpenIdConnectionData) data;
    	} else {
    		connectionData = new OpenIdConnectionData(data.getProviderId(), data.getProviderUserId(),
    				data.getDisplayName(), data.getProfileUrl(), data.getImageUrl(), data.getAccessToken(), data.getSecret(),
    				data.getRefreshToken(), data.getExpireTime(), null);
    	}
    	return new OpenIdConnection<PayPal>(connectionData, this.serviceProvider, getApiAdapter());
    }
}
