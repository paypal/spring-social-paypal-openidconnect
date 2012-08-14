package org.springframework.social.openidconnect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.openidconnect.api.PayPal;
import org.springframework.social.openidconnect.api.impl.PayPalTemplate;

/**
 * Registers paypal as service provider.
 * 
 * @author abprabhakar
 * 
 */
public class PayPalServiceProvider extends AbstractOAuth2ServiceProvider<PayPal> {

    /**
     * User Info endpoint.
     */
    private String userInfoUrl;

    /**
     * Creates a new instance of {@linkplain PayPalOpenIdConnectOperation} and passes it to superclass.
     * 
     * @param appId - Provided by developer portal when you register your application.
     * @param appSecret - Provided by developer portal when you register your application.
     */
    public PayPalServiceProvider(String appId, String appSecret) {
        super(new PayPalOpenIdConnectOperation(appId, appSecret));
    }

    /**
     * Creates a new instance of {@linkplain PayPalOpenIdConnectOperation} and passes it to superclass.
     * 
     * @param appId - Provided by developer portal when you register your application.
     * @param appSecret - Provided by developer portal when you register your application.
     * @param authorizeEndPoint - Autorize endpoint for PayPal Access
     * @param tokenServiceEndPoint - Token service endpoint
     * @param userInfoEndPoint - User info end point
     */
    public PayPalServiceProvider(String appId, String appSecret, String authorizeEndPoint, String tokenServiceEndPoint,
            String userInfoEndPoint) {
        super(new PayPalOpenIdConnectOperation(appId, appSecret, authorizeEndPoint, tokenServiceEndPoint));
        this.userInfoUrl = userInfoEndPoint;
    }

    @Override
    public PayPal getApi(String accessToken) {
        return new PayPalTemplate(accessToken, userInfoUrl);
    }

}
