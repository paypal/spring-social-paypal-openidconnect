package org.springframework.social.openidconnect.support;

import org.springframework.social.oauth2.AccessGrant;

/**
 * Access grant for OpenId Connect which extends <code>AccessGrant</code> from
 * OAuth2 and has additional property to hold id_token.
 */
public class OpenIdAccessGrant extends AccessGrant {

    /**
     * id_token returned from PP Access
     */
    private String idToken;


    public OpenIdAccessGrant(String accessToken){
         super(accessToken);
    }

    public OpenIdAccessGrant(String accessToken, String scope, String refreshToken, Long expiresIn, String idToken) {
          super(accessToken, scope, refreshToken, expiresIn);
          this.idToken = idToken;
    }

    /**
     * Gets Id Token
     * @return idToken
     */
    public String getIdToken() {
        return idToken;
    }
}