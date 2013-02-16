package org.springframework.social.openidconnect.support;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

public class OpenIdConnection<A> extends OAuth2Connection<A> {


    private String idToken;

    private String accessToken;

    private String refreshToken;

    private Long expireTime;

    public OpenIdConnection(String providerId, String providerUserId, String accessToken, String refreshToken, String idToken, Long expireTime,
                            OAuth2ServiceProvider<A> serviceProvider, ApiAdapter<A> apiAdapter) {
        super(providerId, providerUserId, accessToken, refreshToken, expireTime, serviceProvider, apiAdapter);
        this.idToken = idToken;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expireTime = expireTime;
    }

    public OpenIdConnection(OpenIdConnectionData data, OAuth2ServiceProvider<A> serviceProvider, ApiAdapter<A> apiAdapter) {
        super(data, serviceProvider, apiAdapter);
        this.idToken = data.getIdToken();
        this.accessToken = data.getAccessToken();
        this.refreshToken = data.getRefreshToken();
        this.expireTime = data.getExpireTime();
    }

    @Override
    public ConnectionData createData() {
        synchronized (getMonitor()) {
            return new OpenIdConnectionData(getKey().getProviderId(), getKey().getProviderUserId(), getDisplayName(), getProfileUrl(), getImageUrl(), accessToken, null, refreshToken, expireTime, idToken);
        }
    }

    public String getIdToken() {
        return idToken;
    }
}