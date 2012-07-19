/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.openidconnect;

import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.social.oauth2.OAuth2Template;

/**
 * Implements an OAuth facility for PayPal with the predefined API URLs.
 * 
 * @author Felipe Albetao
 * 
 */
public class PayPalOpenIdConnectOperation extends OAuth2Template {

	public PayPalOpenIdConnectOperation(String clientId, String clientSecret) {
		super(clientId, clientSecret, "https://www.paypal.com/webapps/auth/protocol/openidconnect/v1/authorize",
				"https://www.paypal.com/webapps/auth/protocol/openidconnect/v1/tokenservice");
	}

	@Override
	public String buildAuthenticateUrl(GrantType grantType, OAuth2Parameters parameters) {
		return super.buildAuthenticateUrl(grantType, fixedScope(parameters));
	}

	@Override
	public String buildAuthorizeUrl(GrantType grantType, OAuth2Parameters parameters) {
		return super.buildAuthorizeUrl(grantType, fixedScope(parameters));
	}

	private OAuth2Parameters fixedScope(OAuth2Parameters parameters) {
		parameters.setScope("openid profile email address");
		parameters.add("nonce", "demobml");
		return parameters;
	}

}
