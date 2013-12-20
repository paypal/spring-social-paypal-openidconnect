package org.springframework.social.openidconnect;

import org.apache.log4j.Logger;
import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;
import org.springframework.social.openidconnect.api.PayPal;
import org.springframework.social.openidconnect.api.PayPalProfile;

/**
 * <p>
 * Adapter for PayPal Access. Used for setting connection values and adapting User Profile.
 * </p>
 * Note: If you change scope for your application, which essentially means more user information. Then User profile
 * should be adapted similarly. This profile uses scope 'openid email address'
 * 
 */
public class PayPalAdapter implements ApiAdapter<PayPal> {

    /**
     * Logger for {@link PayPalAdapter}
     */
    private static final Logger logger = Logger.getLogger(PayPalAdapter.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.social.connect.ApiAdapter#fetchUserProfile(java.lang.Object)
     */
    @Override
    public UserProfile fetchUserProfile(PayPal paypal) {
        PayPalProfile profile = paypal.getUserProfile();
        if (logger.isDebugEnabled()) {
            logger.debug("UserProfile fetched for userId:"+profile.getUserId());
        }
        return new UserProfileBuilder().setUsername(profile.getUserId()).setEmail(profile.getEmail())
                .setFirstName(profile.getGivenName()).setLastName(profile.getFamilyName()).build();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.social.connect.ApiAdapter#setConnectionValues(java.lang.Object,
     * org.springframework.social.connect.ConnectionValues)
     */
    @Override
    public void setConnectionValues(PayPal paypal, ConnectionValues values) {
        PayPalProfile profile = paypal.getUserProfile();
        if (logger.isDebugEnabled()) {
            logger.debug("ConnectionValues set for userId:"+profile.getUserId());
        }
        values.setProviderUserId(profile.getUserId());
        values.setDisplayName(profile.getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.social.connect.ApiAdapter#test(java.lang.Object)
     */
    @Override
    public boolean test(PayPal paypal) {

        try {
            PayPalProfile profile = paypal.getUserProfile();
            if (profile == null || profile.getUserId() == null) {
                return false;
            }
        } catch (ApiException e) {
            return false;
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.social.connect.ApiAdapter#updateStatus(java.lang.Object, java.lang.String)
     */
    @Override
    public void updateStatus(PayPal paypal, String status) {
        // This is a no-op because PayPal does not support this concept.

    }

}
