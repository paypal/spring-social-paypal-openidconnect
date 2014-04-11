package org.springframework.social.openidconnect.api.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.openidconnect.HttpClientFactory;
import org.springframework.social.openidconnect.PayPalAccessException;
import org.springframework.social.openidconnect.PayPalConnectionProperties;
import org.springframework.social.openidconnect.PreemptiveBasicAuthClientHttpRequestInterceptor;
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
    
    private String appId;
	private String appSecret;


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
		String authorisation = appId + ":" + appSecret;
		byte[] encodedAuthorisation = Base64.encode(authorisation.getBytes());
		headers.add("Authorization", "Basic "
				+ new String(encodedAuthorisation));
		PayPalProfile profile = null;
		RestTemplate restTemplate = getRestTemplate();
		try {
			ResponseEntity<PayPalProfile> jsonResponse = restTemplate.exchange(
					buildURI(accessToken), HttpMethod.GET,
					new HttpEntity<byte[]>(headers), PayPalProfile.class);
			profile = jsonResponse.getBody();
            // Password cannot be blank for Spring security. Setting it to access token rather keeping it as "N/A".
            profile.setPassword(this.accessToken);

            //logging returned back user info
            if(logger.isInfoEnabled()) {
                BeanMap beanMap = new BeanMap(profile);
                for (Iterator i = beanMap.keySet().iterator(); i.hasNext();) {
                    String property = (String) i.next();
                    Object value = beanMap.get(property);
                    logger.info(property + " -> " + value);
                }
            }

            validatePayPalAccessResponse(profile);

          } catch (HttpClientErrorException ex){
            logger.error("User info could not be retrieved " + ex.getMessage() + "  " + ex.getResponseBodyAsString());
        }
        return profile;
    }

    /*
     * This validation is required to check mandatory field/s being returned by PPAccess or not.
     * If not returned then this api will not do any processing and spit exception.
     * :TODO We could have use @JSonproperty- required=true to check null, but
     * since fasterxml's version 2.0 onwards, RequiredProperty field of jsonproperty
     * is not working while deserializing, current latest
     * fasterxml version 2.3.4 also doesn't support.
     * there is github issue link @
     * https://github.com/FasterXML/jackson-databind/issues/230
     *
     */
    private void validatePayPalAccessResponse(PayPalProfile profile) {
        if (StringUtils.isEmpty(profile.getUserId())) {
            logger.error("user_id is coming as null from PPAccess response");
              throw new PayPalAccessException("user_id is coming as null from PPAccess response", new NullPointerException());
        }
    }

    @Override
	protected void configureRestTemplate(RestTemplate restTemplate) {
		super.configureRestTemplate(restTemplate);
		FormHttpMessageConverter formMessageConverter = new FormHttpMessageConverter() {
			public boolean canRead(Class<?> clazz, MediaType mediaType) {
				if(mediaType == null || mediaType.compareTo(MediaType.APPLICATION_JSON)==0){
					return false;
				}
				// always read non-json as x-www-url-formencoded even though PayPal sets contentType to text/plain				
				return true;
			}
		};
		
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(jsonConverter);
		converters.add(formMessageConverter);
		
		restTemplate.setMessageConverters(converters);
//		restTemplate.getInterceptors().add(new PreemptiveBasicAuthClientHttpRequestInterceptor(appId, appSecret));
	}

    /**
     * Builds uri for user info service endpoint. Default one given by {@linkplain PayPalConnectionProperties} will be
     * {@code userInfoUrl} if null.
     * 
     * @return - Uri with parameter
     */
    private URI buildURI(String accesToken) {
        URIBuilder uriBuilder;
        if (userInfoUrl == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Using default user info url");
            }
            uriBuilder = URIBuilder.fromUri(PayPalConnectionProperties.getUserInfoEndpoint());
        } else {
            uriBuilder = URIBuilder.fromUri(this.userInfoUrl);
        }
        URI returnURI = uriBuilder.queryParam("schema", "openid").queryParam("code", accessToken).build();
        if(logger.isInfoEnabled()){
            logger.info("User info uri " + returnURI.toString());
        }
        return returnURI;
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
    
    public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
}
