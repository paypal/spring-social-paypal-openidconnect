Spring Social PayPal with OpenIdConnect - endsession-Log In Variant branch
==============================================

Spring Social PayPal is a Spring Social extension that provides support for the PayPal Access API. PayPal Access API supports both OAuth2 and OpenId Connect protocol.  This module is specifically for using openid connect(http://openid.net/connect/).

This branch was created mainly for satisfying PayPal Access endsession endpoint and provide a config parameter which would allow to disable login varaiant(Not You?) page.

To disable log in variant screen, you need to send a boolean flag while creating the PayPal Connection Factory.  There is new builder method added 
    PayPalConnectionFactoryBuilder.hasLogInVariant

By setting hasLoginVariant(true) loging variant will be disabled.

In order to invoke endsession endpoint OpenId Connect specification specifies to use id_token which is by default not persisted in Connection by Spring Social.  I had used OAuth2 extension of Spring Social and OAuth2 specification does not define id_token attribute at all.  In order to extend current module, I had to add 3 new classes which extends OAuth specifics.

New Classes which are added are:
	org.springframework.social.openidconnect.support.OpenIdAccessGrant
	org.springframework.social.openidconnect.support.OpenIdConnection
	org.springframework.social.openidconnect.support.OpenIdConnectionData
	
In order to support adding OpenId connection couple of methods are overriden in PayPalOpenIdConnectOperation and PayPalConnectionFactory.

Since JDBC connection repository does not support persisting id_token, you can use this only with inmemory repository.  I have added a open source implementation of InmemoryUserconnectionRepository which works great also here.



