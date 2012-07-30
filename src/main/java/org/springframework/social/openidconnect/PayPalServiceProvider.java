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
     * Creates a new instance of {@linkplain PayPalOpenIdConnectOperation} and passes it to superclass.
     * 
     * @param appId - Provided by developer portal when you register your application.
     * @param appSecret - Provided by developer portal when you register your application.
     */
    public PayPalServiceProvider(String appId, String appSecret) {
        super(new PayPalOpenIdConnectOperation(appId, appSecret));
    }

    @Override
    public PayPal getApi(String accessToken) {
        return new PayPalTemplate(accessToken);
    }

}
