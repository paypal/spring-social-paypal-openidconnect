package org.springframework.social.openidconnect;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.openidconnect.api.PayPal;

/**
 * Connection Factory implementation for PayPal Access. Uses OAuth2 itself for OpenId as both protocols are pretty much
 * similar. Both defer in identity layer which we don't care.
 * 
 * @author BML
 * 
 */
public class PayPalConnectionFactory extends OAuth2ConnectionFactory<PayPal> {

    /**
     * Registers connection as 'paypal'. This key is used for managing connections by spring-social.
     * 
     * @param appId - Provided by developer portal when you register your application.
     * @param appSecret - Provided by developer portal when you register your application.
     */
    public PayPalConnectionFactory(String appId, String appSecret) {
        super("paypal", new PayPalServiceProvider(appId, appSecret), new PayPalAdapter());
    }

    /**
     * Registers connection as 'paypal'. This key is used for managing connections by spring-social.
     * 
     * @param appId - Provided by developer portal when you register your application.
     * @param appSecret - Provided by developer portal when you register your application.
     * @param authorizeEndPoint - Autorize endpoint for PayPal Access
     * @param tokenServiceEndPoint - Token service endpoint
     * @param userInfoEndPoint - User info end point
     */
    public PayPalConnectionFactory(String appId, String appSecret, String authorizeEndPoint,
            String tokenServiceEndPoint, String userInfoEndPoint) {
        super("paypal", new PayPalServiceProvider(appId, appSecret, authorizeEndPoint, tokenServiceEndPoint,
                userInfoEndPoint), new PayPalAdapter());
    }

}
