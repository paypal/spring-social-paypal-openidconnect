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
     * 
     * Registers connection as 'paypal'. This key is used for managing connections by spring-social.
     * 
     * @param appId - Provided by developer portal when you register your application.
     * @param appSecret - Provided by developer portal when you register your application.
     * @param scope - List with scopes
     */
    public PayPalConnectionFactory(String appId, String appSecret, String scope) {
        super("paypal", new PayPalServiceProvider(appId, appSecret, scope), new PayPalAdapter());
    }

    /**
     * <p>
     * Registers connection as 'paypal'. This key is used for managing connections by spring-social.
     * </p>
     * 
     * <p>
     * Note: Tried to introduce Builder or Essence pattern instead of having these many arguments in constructor. But
     * was very complicated because the parent class lacks setter method. Maybe a improvement to look at future.
     * </p>
     * 
     * @param appId - Provided by developer portal when you register your application.
     * @param appSecret - Provided by developer portal when you register your application.
     * @param authorizeEndPoint - Autorize endpoint for PayPal Access
     * @param tokenServiceEndPoint - Token service endpoint
     * @param userInfoEndPoint - User info end point
     * @param scope - List with scopes
     */
    public PayPalConnectionFactory(String appId, String appSecret, String scope, String authorizeEndPoint,
            String tokenServiceEndPoint, String userInfoEndPoint) {
        super("paypal", new PayPalServiceProvider(appId, appSecret, scope, authorizeEndPoint, tokenServiceEndPoint,
                userInfoEndPoint), new PayPalAdapter());
    }

}
