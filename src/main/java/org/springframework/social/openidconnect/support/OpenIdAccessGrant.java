package org.springframework.social.openidconnect.support;

import org.springframework.social.oauth2.AccessGrant;

public class OpenIdAccessGrant extends AccessGrant {


    private String idToken;

    public OpenIdAccessGrant(String accessToken){
         super(accessToken);
    }

    public OpenIdAccessGrant(String accessToken, String scope, String refreshToken, Integer expiresIn, String idToken) {
          super(accessToken, scope, refreshToken, expiresIn);
          this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }
}