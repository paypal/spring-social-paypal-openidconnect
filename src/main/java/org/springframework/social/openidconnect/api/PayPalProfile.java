package org.springframework.social.openidconnect.api;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * <p>
 * Represents the user information given by PayPal access.
 * </p>
 * Note: This has to be adapted according to the scope you are providing.
 * 
 * @author abprabhakar
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayPalProfile implements Serializable {

    /**
     * Default serializable version id.
     */
    private static final long serialVersionUID = 1L;

    @JsonProperty("family_name")
    private String familyName;

    @JsonProperty("verified")
    private boolean verified;

    @JsonProperty("locale")
    private String locale;

    @JsonProperty("zoneinfo")
    private String zoneinfo;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("address")
    private Address address;

    @JsonIgnore
    private String password;

    @JsonProperty("birthday")
    private String birthday;

    @JsonProperty("payer_id")
    private String payerId;

    @JsonProperty("account_type")
    private String accountType;

    @JsonProperty("language")
    private String language;

    @JsonProperty("businessName")
    private String businessName;

    @JsonProperty("businessSubCategory")
    private String businessSubCategory;

    @JsonProperty("businessCategory")
    private String businessCategory;

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
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

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public static class Address implements Serializable {

        private static final long serialVersionUID = 1L;

        @JsonProperty("postal_code")
        private String postalCode;

        @JsonProperty("locality")
        private String locality;

        @JsonProperty("region")
        private String region;

        @JsonProperty("country")
        private String country;

        @JsonProperty("street_address")
        private String streetAddress;

        public String getPostal_code() {
            return postalCode;
        }

        public void setPostal_code(String postal_code) {
            this.postalCode = postal_code;
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

        public String getStreetAddress() {
            return streetAddress;
        }

        public void setStreetAddress(String streetAddress) {
            this.streetAddress = streetAddress;
        }

        /*
         * {@inheritDoc}
         */
        @Override
        public String toString() {

            return this.streetAddress + "  " + this.locality + " " + this.region + " " + this.country + "  "
                    + this.postalCode;
        }

    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessSubCategory() {
        return businessSubCategory;
    }

    public void setBusinessSubCategory(String businessSubCategory) {
        this.businessSubCategory = businessSubCategory;
    }

    public String getBusinessCategory() {
        return businessCategory;
    }

    public void setBusinessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
    }

}