package org.springframework.social.openidconnect.support;

import org.springframework.social.connect.ConnectionData;

/**
 * Connection data which extends OAuth2 specific ConnectionData and add idToken property.
 */
public class OpenIdConnectionData extends ConnectionData {

    private String idToken;

    public OpenIdConnectionData(String providerId, String providerUserId, String displayName, String profileUrl, String imageUrl, String accessToken, String secret, String refreshToken, Long expireTime, String idToken) {
        super(providerId, providerUserId, displayName, profileUrl, imageUrl, accessToken, secret, refreshToken, expireTime);
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }
}
