package org.springframework.social.openidconnect;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

/**
 * <p>
 * Properties for connecting PayPal Access. Used to give default values for PayPal Access.
 * </p>
 * 
 * @author paypal
 * 
 */
public final class PayPalConnectionProperties {

    /**
     * Logger for the class.
     */
    private static Logger logger = Logger.getLogger(PayPalConnectionProperties.class);

    /**
     * Properties Holder.
     */
    private static final Properties PROPS = new Properties();

    /**
     * Properties should be accessed in static way.
     */
    private PayPalConnectionProperties() {

    }

    /**
     * Loads properties file based on environment. A default one is included in the jar which is named as
     * connection-prod.properties. If you wish to override, and not use these properties, you can do that while creating
     * connection factory
     * 
     */
    static {
        if (PROPS.isEmpty()) {
            try {
                ClassPathResource resource = new ClassPathResource("connection-prod.properties");
                PROPS.load(resource.getInputStream());
                logger.debug("default properties loaded");
                Assert.notNull(PROPS.get("authorizeEndpoint"), "Authrozie endpoint should be specified");
                Assert.notNull(PROPS.get("tokenEndpoint"), "Token endpoint should be specified");
                Assert.notNull(PROPS.get("checkidEndpoint"), "Check Id Endpoint should be specified");
                Assert.notNull(PROPS.get("userinfoEndpoint"), "User Info endpoint should be specified");
                Assert.notNull(PROPS.get("disconnectEndpoint"), "Disconnect endpoint should be specified");
            } catch (IOException e) {
                logger.error("properties file not found...you have to override url values.");
                throw new RuntimeException("connection-{env} properties file not found");
            }
        }
    }

    /**
     * Gets Authorization endpoint from properties.
     * 
     * @return - Authorize endpoint uri
     */
    public static String getAuthorizeEndpoint() {
        return (String) PROPS.get("authorizeEndpoint");
    }

    /**
     * Gets Token endpoint from properties.
     * 
     * @return - Token endpoint uri
     */
    public static String getTokenEndpoint() {
        return (String) PROPS.get("tokenEndpoint");
    }

    /**
     * Gets CheckId endpoint from properties.
     * 
     * @return - CheckId endpoint uri
     */
    public static String getCheckIdEndpoint() {
        return (String) PROPS.get("checkidEndpoint");
    }

    /**
     * Gets UserInfo endpoint from properties.
     * 
     * @return - Userinfo endpoint uri
     */
    public static String getUserInfoEndpoint() {
        return (String) PROPS.get("userinfoEndpoint");
    }

    /**
     * Gets Disconnect endpoint from properties.
     * 
     * @return - Disconnect endpoint uri
     */
    public static String getDisconnectEndpoint() {
        return (String) PROPS.get("disconnectEndpoint");
    }

}
