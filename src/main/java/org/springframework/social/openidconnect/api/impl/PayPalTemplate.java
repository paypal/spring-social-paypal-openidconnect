package org.springframework.social.openidconnect.api.impl;

import java.net.URI;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.openidconnect.HttpClientFactory;
import org.springframework.social.openidconnect.PayPalConnectionProperties;
import org.springframework.social.openidconnect.api.PayPal;
import org.springframework.social.openidconnect.api.PayPalProfile;
import org.springframework.social.support.URIBuilder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Templates which binds provider to spring social API. This template is also used to get {@code PayPalProfile} from
 * userinfo endpoint.
 */
public class PayPalTemplate extends AbstractOAuth2ApiBinding implements PayPal {

    /**
     * Logger for PayPalTemplate
     */
    private static Logger logger = Logger.getLogger(PayPalTemplate.class);

    /**
     * Access token given by PayPal Access.
     */
    private String accessToken;

    /**
     * User Info end point.
     */
    private String userInfoUrl;


    /**
     * Default constructor.
     */
    public PayPalTemplate() {
    } // Used if token was not obtained

    /**
     * Constructors which accept acess token
     * 
     * @param accessToken - Access token given by PayPal Access.
     * 
     * @param userInfoUrl - User Info endpoint.
     */
    public PayPalTemplate(String accessToken, String userInfoUrl, boolean isStrict) {
        super(accessToken);
        this.accessToken = accessToken;
        this.userInfoUrl = userInfoUrl;
        if(logger.isDebugEnabled()){
            logger.debug("user info url is " + userInfoUrl + " and host name verifier isStrict = " + isStrict);
        }
        //could not override request factory using configureRestTemplate method as isStrict instance variable is not
        //initialized before constructor completes.  So setting request factory just before constructor completes
        setRequestFactory(HttpClientFactory.getRequestFactory(isStrict));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.social.openidconnect.api.PayPal#getUserProfile()
     */
    @Override
    public PayPalProfile getUserProfile() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", this.accessToken);
        PayPalProfile profile = null;
        try {

            ResponseEntity<PayPalProfile> jsonResponse = getRestTemplate().exchange(buildURI(), HttpMethod.GET,
                    new HttpEntity<byte[]>(headers), PayPalProfile.class);
            profile = jsonResponse.getBody();
            // Password cannot be blank for Spring security. Setting it to access token rather keeping it as "N/A".
            profile.setPassword(this.accessToken);
            if (logger.isDebugEnabled()) {
                logger.debug("access token  " + accessToken);
                logger.debug("PayPal Profile received  " + profile);
            }
        } catch (HttpClientErrorException ex){
            logger.error("User info could not be retrieved " + ex.getMessage() + "  " + ex.getResponseBodyAsString());
        }
        return profile;
    }

    /**
     * Builds uri for user info service endpoint. Default one given by {@linkplain PayPalConnectionProperties} will be
     * {@code userInfoUrl} if null.
     * 
     * @return - Uri with parameter
     */
    private URI buildURI() {
        URIBuilder uriBuilder;
        if (userInfoUrl == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Using default user info url");
            }
            uriBuilder = URIBuilder.fromUri(PayPalConnectionProperties.getUserInfoEndpoint());
        } else {
            uriBuilder = URIBuilder.fromUri(this.userInfoUrl);
        }
        return uriBuilder.queryParam("schema", "openid").build();
    }

    /**
     * Sets the access token which is internally used as password.
     * 
     * @param accessToken - Token given by token service.
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Sets user info endpoint for this template.
     * 
     * @param userInfoUrl - User Info Endpoint
     */
    public void setUserInfoUrl(String userInfoUrl) {
        this.userInfoUrl = userInfoUrl;
    }
}
