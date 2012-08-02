package org.springframework.social.openidconnect.api.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.Assert;

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
     */
    @Test
    public void testIfResponseCouldBeParsed() throws IOException {
        InputStream stream = getClass().getResourceAsStream("/paypal-api-openidconnect-response.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        System.out.println(sb.toString());
        PayPalProfile userProfile = template.extractUserProfile(sb.toString());
        Assert.assertEquals("Prabhakar", userProfile.getFamily_name());
        Assert.assertEquals("abhijith@hotmail.com", userProfile.getEmail());
    }
}
