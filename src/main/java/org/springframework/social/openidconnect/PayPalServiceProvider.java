package org.springframework.social.openidconnect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.openidconnect.api.PayPal;
import org.springframework.social.openidconnect.api.impl.PayPalTemplate;

/**
 * Registers paypal as service provider.
 */
public class PayPalServiceProvider extends AbstractOAuth2ServiceProvider<PayPal> {

    /**
     * User Info endpoint.
     */
    private String userInfoUrl;

    private boolean isStrict;
    
    private String appId;
    private String appSecret;

    /**
     * Creates a new instance of {@linkplain PayPalOpenIdConnectOperation} and passes it to superclass.
     * 
     * @param appId - Provided by developer portal when you register your application.
     * @param appSecret - Provided by developer portal when you register your application.
     * @param scope - List with scopes
     * @param isStrict -   Flag which determines Host name verifier
     */
    public PayPalServiceProvider(String appId, String appSecret, String scope, boolean isStrict) {
        super(new PayPalOpenIdConnectOperation(appId, appSecret, scope, isStrict));
        this.appId = appId;
        this.appSecret = appSecret;
        this.isStrict = isStrict;
    }

    /**
     * Creates a new instance of {@linkplain PayPalOpenIdConnectOperation} and passes it to superclass.
     * 
     * @param appId - Provided by developer portal when you register your application.
     * @param appSecret - Provided by developer portal when you register your application.
     * @param authorizeEndPoint - Autorize endpoint for PayPal Access
     * @param tokenServiceEndPoint - Token service endpoint
     * @param userInfoEndPoint - User info end point
     * @param scope - List with scopes
     * @param isStrict -   Flag which determines Host name verifier
     */
    public PayPalServiceProvider(String appId, String appSecret, String scope, String authorizeEndPoint,
            String tokenServiceEndPoint, String userInfoEndPoint, boolean isStrict) {
        super(new PayPalOpenIdConnectOperation(appId, appSecret, scope, authorizeEndPoint, tokenServiceEndPoint, isStrict));
        this.appId = appId;
        this.appSecret = appSecret;
        this.userInfoUrl = userInfoEndPoint;
        this.isStrict = isStrict;
    }

    @Override
    public PayPal getApi(String accessToken) {
        PayPalTemplate template = new PayPalTemplate(accessToken, userInfoUrl, isStrict);
        template.setAppId(appId);
        template.setAppSecret(appSecret);
		return template;
    }

}
