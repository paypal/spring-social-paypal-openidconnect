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
     * Constructor is private since intended to use it as Factory.
     */
    private HttpClientFactory(){

    }

    /**
     * Creates a RestTemplate with default error handler and message converters.
     * @param isStrict - Flag which determines Host name verifier
     * @return - {@link RestTemplate}
     */
    public static RestTemplate getRestTemplateWithPooledConnectionManager(boolean isStrict){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>(2);
        converters.add(new FormHttpMessageConverter());
        converters.add(new MappingJacksonHttpMessageConverter());
        restTemplate.setMessageConverters(converters);

        restTemplate.setRequestFactory(getRequestFactory(isStrict));
        return restTemplate;
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

            SSLSocketFactory sf = new SSLSocketFactory(sslcontext,
                getVerifier(isStrict));

       Scheme https = new Scheme("https", 443, sf);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(http);
            schemeRegistry.register(https);

        PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
        // Increase max total connection to 200
        cm.setMaxTotal(200);
        // Increase default max connection per route to 20
        cm.setDefaultMaxPerRoute(20);
        return cm;
        }catch (Exception ex){
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
        if(isStrict){
            return SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;
        } else {
            return SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        }
    }

}
