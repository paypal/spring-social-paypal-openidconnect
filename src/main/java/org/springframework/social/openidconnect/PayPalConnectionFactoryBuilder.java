package org.springframework.social.openidconnect;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class PayPalConnectionFactoryBuilder {

    private String appId;

    private String appSecret;

    private String scope;

    private String authUrl;

    private String tokenUrl;

    private String userInfoUrl;

    public static PayPalConnectionFactoryBuilder builder() {
        return new PayPalConnectionFactoryBuilder();
    }

    public PayPalConnectionFactory build() {
        Assert.hasText(appId, "AppId is required");
        Assert.hasText(appSecret, "appSecret is required");
        Assert.hasText(scope, "Minimum scope is requried");
        PayPalConnectionFactory factory;
        if (StringUtils.hasText(authUrl) || StringUtils.hasText(tokenUrl) || StringUtils.hasText(userInfoUrl)) {
            Assert.hasText(authUrl, "AuthUrl, tokenUrl and userInfo all are required");
            Assert.hasText(tokenUrl, "AuthUrl, tokenUrl and userInfo all are required");
            Assert.hasText(userInfoUrl, "AuthUrl, tokenUrl and userInfo all are required");
            factory = new PayPalConnectionFactory(appId, appSecret, scope, authUrl, tokenUrl, userInfoUrl);
        } else {
            factory = new PayPalConnectionFactory(appId, appSecret, scope);
        }
        return factory;
    }

    public PayPalConnectionFactoryBuilder withAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public PayPalConnectionFactoryBuilder withAppSecret(String appSecret) {
        this.appSecret = appSecret;
        return this;
    }

    public PayPalConnectionFactoryBuilder withScope(String scope) {
        this.scope = scope;
        return this;
    }

    public PayPalConnectionFactoryBuilder withAuthUrl(String authUrl) {
        this.authUrl = authUrl;
        return this;
    }

    public PayPalConnectionFactoryBuilder withTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
        return this;
    }

    public PayPalConnectionFactoryBuilder withUserInfoUrl(String userInfoUrl) {
        this.userInfoUrl = userInfoUrl;
        return this;
    }

}
