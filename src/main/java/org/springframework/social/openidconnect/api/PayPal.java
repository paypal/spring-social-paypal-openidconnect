package org.springframework.social.openidconnect.api;

import org.springframework.social.ApiBinding;

/**
 * Interface representing the calls exposed by the PayPal Access API
 */
public interface PayPal extends ApiBinding {

    /**
     * Gets the userprofile by contacting userinfo endpoint of PayPal access.
     * 
     * @return - {@linkplain PayPalProfile}
     */
    PayPalProfile getUserProfile();

}
