package org.springframework.social.openidconnect.api.impl;

import java.net.URI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
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
     * User info endpoint as specified by PayPal Access team.
     */
    private static final String USERINFO_SERVICE_ENDPOINT = "https://www.paypal.com/webapps/auth/protocol/openidconnect/v1/userinfo";

    /**
     * Access token given by PayPal Access.
     */
    private String accessToken;

    /**
     * Default constructor.
     */
    public PayPalTemplate() {
    } // Used if token was not obtained

    /**
     * Constructors which accept acess token
     * 
     * @param accessToken - Access token given by PayPal Access.
     */
    public PayPalTemplate(String accessToken) {
        super(accessToken);
        this.accessToken = accessToken;
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

        ResponseEntity<PayPalProfile> response = getRestTemplate().exchange(buildURI(), HttpMethod.GET,
                new HttpEntity<byte[]>(headers), PayPalProfile.class);
        PayPalProfile payPalProfile = response.getBody();
        payPalProfile.setPassword(accessToken);
        return payPalProfile;
    }

    /**
     * Builds uri for user info service endpoint.
     * 
     * @return - Uri with parameter
     */
    private URI buildURI() {
        return URIBuilder.fromUri(USERINFO_SERVICE_ENDPOINT).queryParam("schema", "openid").build();
    }

}
