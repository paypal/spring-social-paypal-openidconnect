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
package org.springframework.social.openidconnect.api.impl;

import java.net.URI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.openidconnect.api.PayPal;
import org.springframework.social.openidconnect.api.PayPalProfile;
import org.springframework.social.support.URIBuilder;

public class PayPalTemplate extends AbstractOAuth2ApiBinding implements PayPal {

	private static final String USERINFO_SERVICE_ENDPOINT = "https://www.paypal.com/webapps/auth/protocol/openidconnect/v1/userinfo";

	private String accessToken;

	public PayPalTemplate() {
	} // Used if token was not obtained

	public PayPalTemplate(String accessToken) {
		super(accessToken);
		this.accessToken = accessToken;
	}

	@Override
	public PayPalProfile getUserProfile() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", this.accessToken);

		ResponseEntity<PayPalProfile> response = getRestTemplate().exchange(buildURI(), HttpMethod.GET, new HttpEntity<byte[]>(headers),
				PayPalProfile.class);
		PayPalProfile payPalProfile = response.getBody();
		payPalProfile.setPassword(accessToken);
		return payPalProfile;
	}

	URI buildURI() {
		return URIBuilder.fromUri(USERINFO_SERVICE_ENDPOINT).queryParam("schema", "openid").build();
	}

}
