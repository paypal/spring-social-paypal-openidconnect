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

import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;
import org.springframework.social.openidconnect.api.PayPal;
import org.springframework.social.openidconnect.api.PayPalProfile;

public class PayPalAdapter implements ApiAdapter<PayPal> {

	@Override
	public UserProfile fetchUserProfile(PayPal paypal) {
		PayPalProfile profile = paypal.getUserProfile();
		return new UserProfileBuilder().setUsername(profile.getUser_id()).setEmail(profile.getEmail()).setFirstName(profile.getGiven_name())
				.setLastName(profile.getFamily_name()).build();
	}

	@Override
	public void setConnectionValues(PayPal paypal, ConnectionValues values) {
		PayPalProfile profile = paypal.getUserProfile();
		values.setProviderUserId(profile.getUser_id());
		values.setDisplayName(profile.getName());
	}

	@Override
	public boolean test(PayPal paypal) {

		try {
			PayPalProfile profile = paypal.getUserProfile();
			if (profile == null || profile.getUser_id() == null)
				return false;
		} catch (ApiException e) {
			return false;
		}

		return true;
	}

	@Override
	public void updateStatus(PayPal paypal, String status) {
		// This is a no-op because PayPal does not support this concept.

	}

}
