package org.springframework.social.openidconnect;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.Assert;

/**
 * Implements an OAuth facility for PayPal with the predefined API URLs.
 */
public class PayPalOpenIdConnectOperation extends OAuth2Template {

    /**
     * Logger for {@link PayPalOpenIdConnectOperation}
     */
    private Logger logger = Logger.getLogger(PayPalOpenIdConnectOperation.class);

    /**
     * Scope to be included in auth request.
     */
    private String scope;

    /**
     * Sets up Template to connect PayPal Access.
     * 
     * @param clientId - Provided by developer portal when you register your application.
     * @param clientSecret - Provided by developer portal when you register your application.
     * @param scope - List with scopes
     */
    public PayPalOpenIdConnectOperation(String clientId, String clientSecret, String scope) {
        super(clientId, clientSecret, PayPalConnectionProperties.getAuthorizeEndpoint(), PayPalConnectionProperties
                .getTokenEndpoint());
        this.scope = scope;
    }

    /**
     * Sets up Template to connect PayPal Access using injected authorize endpoint and token endpoint. Use this if you
     * do not want to use default endpoint values.
     * 
     * @param clientId - Provided by developer portal when you register your application.
     * @param clientSecret - Provided by developer portal when you register your application.
     * @param authorizeEndPoint - PayPal Access authorize end point.
     * @param scope - List with scopes
     * @param tokenEndPoint - PayPal Access token end point.
     */
    public PayPalOpenIdConnectOperation(String clientId, String clientSecret, String scope, String authorizeEndPoint,
            String tokenEndPoint) {
        super(clientId, clientSecret, authorizeEndPoint, tokenEndPoint);
        this.scope = scope;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.social.oauth2.OAuth2Template#buildAuthenticateUrl
     * (org.springframework.social.oauth2.GrantType , org.springframework.social.oauth2.OAuth2Parameters)
     */
    @Override
    public String buildAuthenticateUrl(GrantType grantType, OAuth2Parameters parameters) {
        return super.buildAuthenticateUrl(grantType, fixedScope(parameters));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.social.oauth2.OAuth2Template#buildAuthorizeUrl(org
     * .springframework.social.oauth2.GrantType, org.springframework.social.oauth2.OAuth2Parameters)
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
     * 
     */
    private OAuth2Parameters fixedScope(OAuth2Parameters parameters) {
        Assert.hasText(scope, "scope cannot be null or empty");
        parameters.setScope(scope);
        parameters.add("nonce", createNonce());
        return parameters;
    }

    /**
     * Generates a unique nonce for every request. Created this way based on recommendation from PayPal Access team.
     * 
     * @return - generated nonce
     */
    private String createNonce() {
        Random random = new Random();
        int randomInt = random.nextInt();
        byte[] randomByte = {Integer.valueOf(randomInt).byteValue()};
        String encodedValue;
        String retValue;
        try {
            encodedValue = new String(Base64.encode(randomByte), "UTF-8");
            retValue = (System.currentTimeMillis() + encodedValue);
            if (logger.isDebugEnabled()) {
                logger.debug("nonce for this request " + retValue);
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("encoding was not supported.", e);
            retValue = "defaultNounce";
        }
        return retValue;

    }
}
