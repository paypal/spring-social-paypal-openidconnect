package org.springframework.social.openidconnect;

import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.social.oauth2.OAuth2Template;

/**
 * Implements an OAuth facility for PayPal with the predefined API URLs.
 * 
 * @author abprabhakar
 * 
 */
public class PayPalOpenIdConnectOperation extends OAuth2Template {

    /**
     * Sets up Template to connect PayPal Access.
     * 
     * @param clientId - Provided by developer portal when you register your application.
     * @param clientSecret - Provided by developer portal when you register your application.
     */
    public PayPalOpenIdConnectOperation(String clientId, String clientSecret) {
        super(clientId, clientSecret, "https://www.paypal.com/webapps/auth/protocol/openidconnect/v1/authorize",
                "https://www.paypal.com/webapps/auth/protocol/openidconnect/v1/tokenservice");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.social.oauth2.OAuth2Template#buildAuthenticateUrl(org.springframework.social.oauth2.GrantType
     * , org.springframework.social.oauth2.OAuth2Parameters)
     */
    @Override
    public String buildAuthenticateUrl(GrantType grantType, OAuth2Parameters parameters) {
        return super.buildAuthenticateUrl(grantType, fixedScope(parameters));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.social.oauth2.OAuth2Template#buildAuthorizeUrl(org.springframework.social.oauth2.GrantType,
     * org.springframework.social.oauth2.OAuth2Parameters)
     */
    @Override
    public String buildAuthorizeUrl(GrantType grantType, OAuth2Parameters parameters) {
        return super.buildAuthorizeUrl(grantType, fixedScope(parameters));
    }

    /**
     * Sets parameters for request.
     * 
     * @param parameters - parametes which are included in request
     * @return - Filled up parameters.
     */
    private OAuth2Parameters fixedScope(OAuth2Parameters parameters) {
        parameters.setScope("openid profile email address");
        parameters.add("nonce", "demobml");
        return parameters;
    }

}
