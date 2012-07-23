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
package org.springframework.social.openidconnect.api;

public class PayPalProfile {

	private String family_name;
	private boolean verified;
	private String locale;
	private String zoneinfo;
	private String name;
	private String email;
	private String given_name;
	private String user_id;
	private Address address;
	private String password;

	public String getFamily_name() {
		return family_name;
	}

	public void setFamily_name(String family_name) {
		this.family_name = family_name;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getZoneinfo() {
		return zoneinfo;
	}

	public void setZoneinfo(String zoneinfo) {
		this.zoneinfo = zoneinfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGiven_name() {
		return given_name;
	}

	public void setGiven_name(String given_name) {
		this.given_name = given_name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static class Address {

		private String postal_code;
		private String locality;
		private String region;
		private String country;
		private String street_address;

		public String getPostal_code() {
			return postal_code;
		}

		public void setPostal_code(String postal_code) {
			this.postal_code = postal_code;
		}

		public String getLocality() {
			return locality;
		}

		public void setLocality(String locality) {
			this.locality = locality;
		}

		public String getRegion() {
			return region;
		}

		public void setRegion(String region) {
			this.region = region;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public String getStreet_address() {
			return street_address;
		}

		public void setStreet_address(String street_address) {
			this.street_address = street_address;
		}

		@Override
		public String toString() {

			return this.street_address + "  " + this.locality + " " + this.region + " " + this.country + "  " + this.postal_code;
		}

	}

}