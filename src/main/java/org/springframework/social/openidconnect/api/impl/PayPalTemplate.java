package org.springframework.social.openidconnect.api.impl;

import java.net.URI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.openidconnect.PayPalConnectionProperties;
import org.springframework.social.openidconnect.api.PayPal;
import org.springframework.social.openidconnect.api.PayPalProfile;
import org.springframework.social.support.URIBuilder;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Templates which binds provider to spring social API. This template is also used to get {@code PayPalProfile} from
 * userinfo endpoint.
 * 
 * @author abprabhakar
 * 
 */
public class PayPalTemplate extends AbstractOAuth2ApiBinding implements PayPal {

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

        ResponseEntity<String> response = getRestTemplate().exchange(buildURI(), HttpMethod.GET,
                new HttpEntity<byte[]>(headers), String.class);
        String jsonResponse = response.getBody();
        return extractUserProfile(jsonResponse);
    }

    /**
     * Gets the {@code PayPalProfile} from given Json string.
     * 
     * @param jsonResponse - Json string
     * @return {@link PayPalProfile}
     */
    public PayPalProfile extractUserProfile(String jsonResponse) {
        PayPalProfile profile = new PayPalProfile();
        JsonElement jsonRoot = new JsonParser().parse(jsonResponse);
        if (jsonRoot.getAsJsonObject().get("user_id") != null && this.accessToken != null) {
            profile.setUser_id(jsonRoot.getAsJsonObject().get("user_id").getAsString());
            profile.setPassword(this.accessToken);
        } else {
            throw new RuntimeException("User Id and password cannot be null");
        }

        if (jsonRoot.getAsJsonObject().get("family_name") != null) {
            profile.setFamily_name(jsonRoot.getAsJsonObject().get("family_name").getAsString());
        }
        if (jsonRoot.getAsJsonObject().get("given_name") != null) {
            profile.setGiven_name(jsonRoot.getAsJsonObject().get("given_name").getAsString());
        }
        if (jsonRoot.getAsJsonObject().get("email") != null) {
            profile.setEmail(jsonRoot.getAsJsonObject().get("email").getAsString());
        }
        if (jsonRoot.getAsJsonObject().get("name") != null) {
            profile.setName(jsonRoot.getAsJsonObject().get("name").getAsString());
        }

        if (jsonRoot.getAsJsonObject().get("verified") != null) {
            profile.setVerified(jsonRoot.getAsJsonObject().get("verified").getAsBoolean());
        }
        if (jsonRoot.getAsJsonObject().get("locale") != null) {
            profile.setLocale(jsonRoot.getAsJsonObject().get("locale").getAsString());
        }
        if (jsonRoot.getAsJsonObject().get("zoneinfo") != null) {
            profile.setZoneinfo(jsonRoot.getAsJsonObject().get("zoneinfo").getAsString());
        }
        if (jsonRoot.getAsJsonObject().get("address") != null) {
            JsonElement address = jsonRoot.getAsJsonObject().get("address").getAsJsonObject();
            PayPalProfile.Address addr = new PayPalProfile.Address();
            if (address.getAsJsonObject().get("street_address") != null) {
                addr.setStreet_address(address.getAsJsonObject().get("street_address").getAsString());
            }

            if (address.getAsJsonObject().get("region") != null) {
                addr.setRegion(address.getAsJsonObject().get("region").getAsString());
            }

            if (address.getAsJsonObject().get("locality") != null) {
                addr.setLocality(address.getAsJsonObject().get("locality").getAsString());
            }

            if (address.getAsJsonObject().get("country") != null) {
                addr.setCountry(address.getAsJsonObject().get("country").getAsString());
            }

            if (address.getAsJsonObject().get("postal_code") != null) {
                addr.setPostal_code(address.getAsJsonObject().get("postal_code").getAsString());
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
