package org.springframework.social.openidconnect;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.net.ssl.SSLContext;

import static org.apache.http.conn.ssl.SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;

/**
 * Factory which gives more fine grained control over creation of <code>RestTemplate</code> and allows configuration such as connection time out,
 * read time out and pooled connection manager.
 */
public final class HttpClientFactory {

    /**
     * Logger for {@link HttpClientFactory}
     */
    private static final Logger logger = Logger.getLogger(HttpClientFactory.class);

    /**
     * Constructor is private since intended to use it as Factory.
     */
    private HttpClientFactory(){

    }

    /**
     * Creates a Http Request Factory with user defined settings.
     * @param isStrict -   Flag which determines Host name verifier
     * @return - {@link HttpComponentsClientHttpRequestFactory}
     */
    public static HttpComponentsClientHttpRequestFactory getRequestFactory(boolean isStrict){
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);

        HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(getPooledConnectionManager(isStrict)).build();
        factory.setHttpClient(httpClient);
        if(logger.isDebugEnabled()){
            logger.debug("Factory is set to use connection time out and read time out");
        }
        return factory;
    }

    /**
     * Pooled connection manager definition.  Has been setup for both HTTP and HTTPS connections.
     * @return - Client connection manager
     * @see org.apache.http.conn.ssl.X509HostnameVerifier
     */
    public static HttpClientConnectionManager getPooledConnectionManager(boolean isStrict){
        try {
            HttpClientBuilder builder = HttpClientBuilder.create();

            SSLContext sslContext = SSLContexts.custom()
                    .useTLS()
                    .build();

            SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(
                    sslContext,
                    new String[]{getTlsVersion()},
                    null,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            builder.setSSLSocketFactory(sslConnectionFactory);

            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslConnectionFactory)
                    .build();

            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);

            // Increase max total connection to 200
            cm.setMaxTotal(200);
            // Increase default max connection per route to 20
            cm.setDefaultMaxPerRoute(20);
            if(logger.isDebugEnabled()){
                logger.debug("Pooling connection manager configured successfully.");
            }
            return cm;
        }catch (Exception ex){
            logger.error("Exception thrown while configuring HttpConnectionFactory", ex);
            throw new PayPalAccessException("Not able to get HTTP Connection factory", ex);
        }
    }


    /**
     * Gets HostName verifier
     * @param isStrict - Flag which determines Host name verifier
     * @return - If false uses  ALLOW_ALL_HOSTNAME_VERIFIER
     * @see  SSLConnectionSocketFactory
     */
    private static X509HostnameVerifier getVerifier(boolean isStrict){
        if(!isStrict) {
            if(logger.isInfoEnabled()){
                logger.info("Using Allow All HostName verifier. isStrict = " + isStrict);
            }
            return SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        }
        if(logger.isInfoEnabled()){
            logger.info("Using Strict HostName verifier. isStrict = " + isStrict);
        }
        return SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER;
    }

    /**
     * Gets the TLS version from jdk parameter.  Defaults to version 1.2 if not provided
     * @return
     */
    public static String getTlsVersion() {
        // don't use "jdk.tls.client.protocols" as it was causing unexpected exception during unit testing on jdk 1.8
        String tlsVer = System.getProperty("tls.protocol");

        if(StringUtils.isEmpty(tlsVer)) {
            logger.info("defaulting to TLSv1.2 on jdk " + System.getProperty("java.specification.version"));
            // note: java 7 defaults to version 1 but supports 1.2
            tlsVer = "TLSv1.2";
        }

        return tlsVer;
    }
}
