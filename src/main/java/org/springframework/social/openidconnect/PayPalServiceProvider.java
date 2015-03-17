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
    
    private String clientId;
    private String appSecret;

    /**
     * Creates a new instance of {@linkplain PayPalOpenIdConnectOperation} and passes it to superclass.
     * 
     * @param clientId - Provided by developer portal when you register your application.
     * @param appSecret - Provided by developer portal when you register your application.
     * @param scope - List with scopes
     * @param isStrict -   Flag which determines Host name verifier
     */
    public PayPalServiceProvider(String clientId, String appSecret, String scope, boolean isStrict) {
        super(new PayPalOpenIdConnectOperation(clientId, appSecret, scope, isStrict));
        this.clientId = clientId;
        this.appSecret = appSecret;
        this.isStrict = isStrict;
    }

    /**
     * Creates a new instance of {@linkplain PayPalOpenIdConnectOperation} and passes it to superclass.
     * 
     * @param clientId - Provided by developer portal when you register your application.
     * @param appSecret - Provided by developer portal when you register your application.
     * @param authorizeEndPoint - Autorize endpoint for PayPal Access
     * @param tokenServiceEndPoint - Token service endpoint
     * @param userInfoEndPoint - User info end point
     * @param scope - List with scopes
     * @param isStrict -   Flag which determines Host name verifier
     */
    public PayPalServiceProvider(String clientId, String appSecret, String scope, String authorizeEndPoint,
            String tokenServiceEndPoint, String userInfoEndPoint, boolean isStrict) {
        super(new PayPalOpenIdConnectOperation(clientId, appSecret, scope, authorizeEndPoint, tokenServiceEndPoint, isStrict));
        this.clientId = clientId;
        this.appSecret = appSecret;
        this.userInfoUrl = userInfoEndPoint;
        this.isStrict = isStrict;
    }

    @Override
    public PayPal getApi(String accessToken) {
        PayPalTemplate template = new PayPalTemplate(accessToken, userInfoUrl, isStrict);
        template.setClientId(clientId);
        template.setAppSecret(appSecret);
		return template;
    }

}
