package org.springframework.social.openidconnect.support;

import org.springframework.social.connect.ConnectionData;

/**
 * Created with IntelliJ IDEA.
 * User: abprabhakar
 * Date: 2/13/13
 * Time: 5:42 PM
 * To change this template use File | Settings | File Templates.
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
