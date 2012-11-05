package org.springframework.social.openidconnect;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.log4j.Logger;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.social.ApiException;
import org.springframework.social.InternalServerErrorException;
import org.springframework.social.ServerException;
import org.springframework.social.support.ClientHttpRequestFactorySelector;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.List;

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
        HttpClient httpClient = new DefaultHttpClient(getPooledConnectionManager(isStrict));
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
    public static ClientConnectionManager getPooledConnectionManager(boolean isStrict){
        try {
            Scheme http = new Scheme("http", 80, PlainSocketFactory.getSocketFactory());
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, null, null);

            SSLSocketFactory sf = new SSLSocketFactory(sslcontext, getVerifier(isStrict));

            Scheme https = new Scheme("https", 443, sf);

            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(http);
            schemeRegistry.register(https);

            PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
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
     * @see  SSLSocketFactory
     */
    private static X509HostnameVerifier getVerifier(boolean isStrict){
        if(!isStrict) {
            if(logger.isInfoEnabled()){
                logger.info("Using Allow All HostName verifier. isStrict = " + isStrict);
            }
            return SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        }
        if(logger.isInfoEnabled()){
            logger.info("Using Strict HostName verifier. isStrict = " + isStrict);
        }
        return SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;
    }

}
