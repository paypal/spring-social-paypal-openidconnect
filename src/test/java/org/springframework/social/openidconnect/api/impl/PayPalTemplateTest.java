package org.springframework.social.openidconnect.api.impl;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.springframework.social.openidconnect.api.PayPalProfile;

/**
 * Tests whether JSON response can be parsed or not.
 * 
 * @author abprabhakar
 * 
 */
public class PayPalTemplateTest {

    /**
     * Object on test.
     */
    private final PayPalTemplate template = new PayPalTemplate();

    /**
     * Tests whether a JSON response could be parsed or not.
     * 
     * @throws IOException - If file could not be read.
     * @throws NoSuchFieldException - If access field is not found
     * @throws SecurityException - If not accessible
     * @throws IllegalAccessException - Illegal access to field
     * @throws IllegalArgumentException - If argument is not valid
     */
    @Test
    public void testIfResponseCouldBeParsed() throws IOException, SecurityException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        template.setAccessToken("test");
        InputStream stream = getClass().getResourceAsStream("/paypal-api-openidconnect-response.json");
        ObjectMapper mapper = new ObjectMapper();
        PayPalProfile userProfile = mapper.readValue(stream, PayPalProfile.class);
        // PayPalProfile userProfile = template.extractUserProfile(jsonMap);
        Assert.assertEquals("Prabhakar", userProfile.getFamilyName());
        Assert.assertEquals("abhijith@hotmail.com", userProfile.getEmail());
        Assert.assertNull(userProfile.getLocale());
    }
}
