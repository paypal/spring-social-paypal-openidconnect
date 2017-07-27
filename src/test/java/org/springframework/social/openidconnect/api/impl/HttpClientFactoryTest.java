package org.springframework.social.openidconnect.api.impl;


import com.sun.jndi.toolkit.url.Uri;
import junit.framework.Assert;
import org.apache.http.HttpEntity;
import org.apache.http.conn.ClientConnectionManager;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.*;
import org.springframework.social.openidconnect.HttpClientFactory;
import org.springframework.social.openidconnect.PayPalAccessException;

import java.io.IOException;
import java.net.URI;

/**
 * Tests whether TLS version parameter works
 */
public class HttpClientFactoryTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private static final String TLSVER = "jdk.tls.client.protocols";

    /**
     * Test variations of passing or not passing TLS version
     */
    @Test
    public void testTlsVersionSuccess() {
        ClientConnectionManager cm = HttpClientFactory.getPooledConnectionManager(true);
        Assert.assertEquals("https:443", cm.getSchemeRegistry().get("https").toString());
        cm = HttpClientFactory.getPooledConnectionManager(false);
        Assert.assertEquals("https:443", cm.getSchemeRegistry().get("https").toString());
        cm = HttpClientFactory.getPooledConnectionManager(true, "TLS");
        Assert.assertEquals("https:443", cm.getSchemeRegistry().get("https").toString());
        cm = HttpClientFactory.getPooledConnectionManager(true, "TLSv1.2");
        Assert.assertEquals("https:443", cm.getSchemeRegistry().get("https").toString());
    }

    /**
     * Test passing bad TLS version
     */
    @Test
    public void testTlsVersionException() {
        exception.expect(PayPalAccessException.class);
        ClientConnectionManager cm = HttpClientFactory.getPooledConnectionManager(true, "TLSvErrorTest");
    }

    @Test
    public void testRequestFactory() throws IOException {
        HttpComponentsClientHttpRequestFactory rf = HttpClientFactory.getRequestFactory(true);
        Assert.assertNotNull(rf);
        URI uri = URI.create("https://google.com");
        ClientHttpRequest request = rf.createRequest(uri, HttpMethod.GET);
        ClientHttpResponse response = request.execute();
        Assert.assertNotNull(response);
    }

    @Test
    public void testJdkTlsProperty() {
        String tlsProtocols = System.getProperty(TLSVER);
        Assert.assertNull(tlsProtocols);

        System.setProperty(TLSVER, "TLSv1.2");
        tlsProtocols = System.getProperty(TLSVER);
        Assert.assertEquals("TLSv1.2", tlsProtocols);
    }
}
