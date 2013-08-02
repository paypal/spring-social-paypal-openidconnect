package org.springframework.social.openidconnect;

import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.social.openidconnect.support.OpenIdAccessGrant;
import org.springframework.social.support.ClientHttpRequestFactorySelector;
import org.springframework.util.MultiValueMap;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Collections;

/**
 * Implements an OAuth facility for PayPal with the predefined API URLs.
 */
public class PayPalOpenIdConnectOperation extends OAuth2Template {

    /**
     * Logger for {@link PayPalOpenIdConnectOperation}
     */
    private static Logger logger = Logger.getLogger(PayPalOpenIdConnectOperation.class);

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
     * @param isStrict -   Flag which determines Host name verifier
     */
    public PayPalOpenIdConnectOperation(String clientId, String clientSecret, String scope, boolean isStrict) {
        super(clientId, clientSecret, PayPalConnectionProperties.getAuthorizeEndpoint(), PayPalConnectionProperties
                .getTokenEndpoint());
        this.scope = scope;
        //Override request factory after rest template has been initialized
        setRequestFactory(HttpClientFactory.getRequestFactory(isStrict));

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
     * @param isStrict -   Flag which determines Host name verifier
     */
    public PayPalOpenIdConnectOperation(String clientId, String clientSecret, String scope, String authorizeEndPoint,
            String tokenEndPoint, boolean isStrict) {
        super(clientId, clientSecret, authorizeEndPoint, tokenEndPoint);
        this.scope = scope;
        //Override request factory after rest template has been initialized
        setRequestFactory(HttpClientFactory.getRequestFactory(isStrict));
    }

    /**
     * Creates an {@link org.springframework.social.oauth2.AccessGrant} given the response from the access token exchange with the provider.
     * May be overridden to create a custom AccessGrant that captures provider-specific information from the access token response.
     *
     * @param accessToken  the access token value received from the provider
     * @param scope        the scope of the access token
     * @param refreshToken a refresh token value received from the provider
     * @param expiresIn    the time (in seconds) remaining before the access token expires.
     * @param response     all parameters from the response received in the access token exchange.
     * @return an {@link org.springframework.social.oauth2.AccessGrant}
     */
    @Override
    protected AccessGrant createAccessGrant(String accessToken, String scope, String refreshToken, Integer expiresIn, Map<String, Object> response) {
        return new OpenIdAccessGrant(accessToken, scope, refreshToken, expiresIn, (String) response.get("id_token"));
    }

    /*
    * (non-Javadoc)
    *
    * @see org.springframework.social.oauth2.OAuth2Template#buildAuthenticateUrl
    * (org.springframework.social.oauth2.GrantType , org.springframework.social.oauth2.OAuth2Parameters)
    */
    @Override
    public String buildAuthenticateUrl(GrantType grantType, OAuth2Parameters parameters) {
        String authenticateUrl = super.buildAuthenticateUrl(grantType, getRequestParameters(parameters));
        if(logger.isDebugEnabled()){
            logger.debug("Authenticate url:" + authenticateUrl);
        }
        return authenticateUrl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.social.oauth2.OAuth2Template#buildAuthorizeUrl(org
     * .springframework.social.oauth2.GrantType, org.springframework.social.oauth2.OAuth2Parameters)
     */
    @Override
    public String buildAuthorizeUrl(GrantType grantType, OAuth2Parameters parameters) {
        String authorizeUrl = super.buildAuthorizeUrl(grantType, getRequestParameters(parameters));
        if(logger.isDebugEnabled()){
            logger.debug("Authorize url:" + authorizeUrl);
        }
        return authorizeUrl;
    }
	
	@Override
	protected RestTemplate createRestTemplate() {
		RestTemplate restTemplate = new RestTemplate(ClientHttpRequestFactorySelector.getRequestFactory());
		FormHttpMessageConverter formMessageConverter = new FormHttpMessageConverter() {
			public boolean canRead(Class<?> clazz, MediaType mediaType) {
				if(mediaType != null && mediaType.compareTo(MediaType.APPLICATION_JSON)==0){
					return false;
				}
				// always read non-json as x-www-url-formencoded even though PayPal sets contentType to text/plain				
				return true;
			}
		};
		
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(formMessageConverter);
		converters.add(jsonConverter);
		
		restTemplate.setMessageConverters(converters);
		return restTemplate;
	}

    /**
     * Sets parameters for request.
     * 
     * @param parameters - parametes which are included in request
     * @return - Filled up parameters.
     * 
     */
    private OAuth2Parameters getRequestParameters(OAuth2Parameters parameters) {
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
