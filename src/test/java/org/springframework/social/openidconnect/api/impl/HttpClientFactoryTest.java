package org.springframework.social.openidconnect.api.impl;

import junit.framework.Assert;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.pool.PoolEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.*;
import org.springframework.social.openidconnect.HttpClientFactory;
import org.springframework.social.openidconnect.PayPalAccessException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URI;


/**
 * Tests whether TLS version parameter works.  Tested by running under jdk 1.6 and 1.7
 */
public class HttpClientFactoryTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    //private static final String TLSVER = "jdk.tls.client.protocols";
    private static final String TLSVER = "tls.protocol";

    private static final double javaVersion = Double.parseDouble(System.getProperty("java.specification.version"));

    @After
    public void afterEach() {
        System.clearProperty(TLSVER);
    }


    /**
     * Test NOT passing TLS version and relying on default
     */
    @Test
    public void testDefaultTlsVersionSuccess() {
        if(javaVersion <= 1.6) {
            exception.expect(PayPalAccessException.class);
        }
        ClientConnectionManager cm = HttpClientFactory.getPooledConnectionManager(true);
        Assert.assertEquals("https:443", cm.getSchemeRegistry().get("https").toString());
        cm = HttpClientFactory.getPooledConnectionManager(false);
        Assert.assertEquals("https:443", cm.getSchemeRegistry().get("https").toString());
    }

    /**
     * Test variations of passing un-versioned TLS
     */
    @Test
    public void testSetTlsVersionSuccess() {
        System.setProperty(TLSVER, "TLS");
        ClientConnectionManager cm = HttpClientFactory.getPooledConnectionManager(true);
        Assert.assertEquals("https:443", cm.getSchemeRegistry().get("https").toString());
    }

    /**
     * Test variations of passing TLS 1.2 version
     */
    @Test
    public void testSetTls12VersionSuccess() {
        System.setProperty(TLSVER, "TLSv1.2");

        if(javaVersion <= 1.6) {
            exception.expect(PayPalAccessException.class);
        }
        ClientConnectionManager cm = HttpClientFactory.getPooledConnectionManager(true);
        Assert.assertEquals("https:443", cm.getSchemeRegistry().get("https").toString());
    }

    /**
     * Test passing bad TLS version
     */
    @Test
    public void testTlsVersionException() {
        System.setProperty(TLSVER, "TLSvErrorTest");
        exception.expect(PayPalAccessException.class);
        ClientConnectionManager cm = HttpClientFactory.getPooledConnectionManager(true);
    }

    /**
     * Test making HTTP call without passing TLS version
     * @throws IOException
     */
    @Test
    public void testDefaultRequestFactory() throws IOException, NoSuchFieldException, IllegalAccessException {
        if(javaVersion <= 1.6) {
            exception.expect(PayPalAccessException.class);
        }

        Object protocolVersion = makeTestHttpsRequest();
        Assert.assertEquals("TLSv1.2", protocolVersion.toString());
    }

    /**
     * Test making HTTP call and using TLS override
     * @throws IOException
     */
    @Test
    public void testTlsRequestFactory() throws IOException, NoSuchFieldException, IllegalAccessException {
        System.setProperty(TLSVER, "TLS");
        Object protocolVersion = makeTestHttpsRequest();

        if(javaVersion <= 1.7) {
            Assert.assertEquals("TLSv1", protocolVersion.toString());
        } else {
            Assert.assertEquals("TLSv1.2", protocolVersion.toString());
        }
    }

    /**
     * Test making HTTP call and using TLSv1 override
     * @throws IOException
     */
    @Test
    public void testTls1RequestFactory() throws IOException, NoSuchFieldException, IllegalAccessException {
        System.setProperty(TLSVER, "TLSv1");
        Object protocolVersion = makeTestHttpsRequest();

        Assert.assertEquals("TLSv1", protocolVersion.toString());
    }

    /**
     * Test making HTTP call and using TLSv1.2 version
     * @throws IOException
     */
    @Test
    public void testTls12RequestFactory() throws IOException, NoSuchFieldException, IllegalAccessException {
        System.setProperty(TLSVER, "TLSv1.2");

        if(javaVersion <= 1.6) {
            //java 6 does not support TLSv1.2
            exception.expect(PayPalAccessException.class);
        }

        Object protocolVersion = makeTestHttpsRequest();
        Assert.assertEquals("TLSv1.2", protocolVersion.toString());
    }


    /****** HELPER METHODS ****/

    /**
     * Makes a test request over https
     * @return Protocol version used for connection. eg. TLSv1.2
     * @throws IOException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private String makeTestHttpsRequest() throws IOException, NoSuchFieldException, IllegalAccessException {
        HttpComponentsClientHttpRequestFactory rf = HttpClientFactory.getRequestFactory(true);
        Assert.assertNotNull(rf);
        URI uri = URI.create("https://google.com");
        ClientHttpRequest request = rf.createRequest(uri, HttpMethod.GET);
        ClientHttpResponse response = request.execute();
        Assert.assertNotNull(response);

        // extract the tls version
        Proxy responseProxy = (Proxy) getPropertyFromObject(response, "httpResponse");
        InvocationHandler responseHandler = Proxy.getInvocationHandler(responseProxy);
        PoolEntry poolEntry = (PoolEntry) getPropertyFromObject(responseHandler, "original.entity.managedConn.poolEntry");

        Object protocolVersion = getPropertyFromObject(poolEntry.getConnection(), "socket.protocolVersion");
        return protocolVersion.toString();
    }

    /**
     * Returns the property value for the object.  You can pass a property chain like p1.p2.p3 and p3 value will be returned
     * @param obj object that contains the property
     * @param propChain You can pass a property chain like p1.p2.p3 and p3 value will be returned
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getPropertyFromObject(Object obj, String propChain) throws NoSuchFieldException, IllegalAccessException {
        String[] props = propChain.split("\\.");
        Object currObj = obj;
        Field field;

        for (String prop : props) {
            field = currObj.getClass().getDeclaredField(prop);
            field.setAccessible(true);
            currObj = field.get(currObj);
        }

        return currObj;
    }
}
