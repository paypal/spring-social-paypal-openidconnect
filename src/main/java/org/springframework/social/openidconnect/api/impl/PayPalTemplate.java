package org.springframework.social.openidconnect.api.impl;

import java.net.URI;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.openidconnect.PayPalConnectionProperties;
import org.springframework.social.openidconnect.api.PayPal;
import org.springframework.social.openidconnect.api.PayPalProfile;
import org.springframework.social.support.URIBuilder;

/**
 * Templates which binds provider to spring social API. This template is also used to get {@code PayPalProfile} from
 * userinfo endpoint.
 * 
 * @author abprabhakar
 * 
 */
public class PayPalTemplate extends AbstractOAuth2ApiBinding implements PayPal {

    /**
     * Logger for PayPalTemplate
     */
    private Logger logger = Logger.getLogger(PayPalTemplate.class);

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
    public PayPalTemplate(String accessToken, String userInfoUrl) {
        super(accessToken);
        this.accessToken = accessToken;
        this.userInfoUrl = userInfoUrl;
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
        ResponseEntity<Map> response = getRestTemplate().exchange(buildURI(), HttpMethod.GET,
                new HttpEntity<byte[]>(headers), Map.class);
        Map<String, Object> jsonResponse = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug("access token  " + accessToken);
            for (String key : jsonResponse.keySet()) {
                logger.debug("Key:  " + key + "  Value : " + jsonResponse.get(key));
            }
        }
        return extractUserProfile(jsonResponse);
    }

    /**
     * Gets the {@code PayPalProfile} from given Json string.
     * 
     * @param jsonResponse - Json string
     * @return {@link PayPalProfile}
     */
    public PayPalProfile extractUserProfile(Map<String, Object> jsonResponse) {
        PayPalProfile profile = new PayPalProfile();
        if (jsonResponse.get("user_id") != null && this.accessToken != null) {
            profile.setUserId(jsonResponse.get("user_id").toString());
            profile.setPassword(this.accessToken);
        } else {
            throw new RuntimeException("User Id and password cannot be null");
        }

        if (jsonResponse.get("family_name") != null) {
            profile.setFamilyName(jsonResponse.get("family_name").toString());
        }
        if (jsonResponse.get("given_name") != null) {
            profile.setGivenName(jsonResponse.get("given_name").toString());
        }
        if (jsonResponse.get("email") != null) {
            profile.setEmail(jsonResponse.get("email").toString());
        }
        if (jsonResponse.get("name") != null) {
            profile.setName(jsonResponse.get("name").toString());
        }

        if (jsonResponse.get("verified") != null) {
            profile.setVerified(Boolean.valueOf(jsonResponse.get("verified").toString()));
        }
        if (jsonResponse.get("locale") != null) {
            profile.setLocale(jsonResponse.get("locale").toString());
        }
        if (jsonResponse.get("zoneinfo") != null) {
            profile.setZoneinfo(jsonResponse.get("zoneinfo").toString());
        }
        if (jsonResponse.get("address") != null) {
            Map<String, String> addressMap = (Map<String, String>) jsonResponse.get("address");
            PayPalProfile.Address addr = new PayPalProfile.Address();
            if (addressMap.get("street_address") != null) {
                addr.setStreetAddress(addressMap.get("street_address"));
            }

            if (addressMap.get("region") != null) {
                addr.setRegion(addressMap.get("region"));
            }

            if (addressMap.get("locality") != null) {
                addr.setLocality(addressMap.get("locality"));
            }

            if (addressMap.get("country") != null) {
                addr.setCountry(addressMap.get("country"));
            }

            if (addressMap.get("postal_code") != null) {
                addr.setPostal_code(addressMap.get("postal_code"));
            }
            profile.setAddress(addr);
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
        if (userInfoUrl != null) {
            uriBuilder = URIBuilder.fromUri(this.userInfoUrl);
        } else {
            logger.debug("Using default user info url  " + userInfoUrl);
            uriBuilder = URIBuilder.fromUri(PayPalConnectionProperties.getUserInfoEndpoint());
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
