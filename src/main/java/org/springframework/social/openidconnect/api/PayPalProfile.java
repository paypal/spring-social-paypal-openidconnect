package org.springframework.social.openidconnect.api;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * Represents the user information given by PayPal access.
 * </p>
 * Note: This has to be adapted according to the scope you are providing.
 * 
 */
@JsonIgnoreProperties(ignoreUnknown=true,value={"password"})
public class PayPalProfile implements Serializable {

    /**
     * Default serializable version id.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Last name
     */
    @JsonProperty("family_name")
    private String familyName;

    /**
     * Account status
     */
    @JsonProperty("verified")
    private boolean verified;

    /**
     * Locate
     */
    @JsonProperty("locale")
    private String locale;

    /**
     * Zone info
     */
    @JsonProperty("zoneinfo")
    private String zoneinfo;

    /**
     * First name
     */
    @JsonProperty("name")
    private String name;

    /**
     * Email
     */
    @JsonProperty("email")
    private String email;

    /**
     * Given name
     */
    @JsonProperty("given_name")
    private String givenName;

    /**
     * PayPal User id
     */
    @JsonProperty("user_id")
    private String userId;

    /**
     * Given user address
     */
    @JsonProperty("address")
    private Address address;

    /**
     * Password is not returned by PayPal Access. Mainly here to satisfy Spring Security requirements
     */
    private String password;

    /**
     * Birth date.
     */
    @JsonProperty("birthday")
    private String birthday;

    /**
     * PayPal Payer Id
     */
    @JsonProperty("payer_id")
    private String payerId;

    /**
     * Account type.
     */
    @JsonProperty("account_type")
    private String accountType;

    /**
     * PayPal language
     */
    @JsonProperty("language")
    private String language;

    /**
     * Phone number
     */
    @JsonProperty("phone_number")
    private String phoneNumber;

    /**
     * Business name
     */
    @JsonProperty("businessName")
    private String businessName;

    /**
     * Business Sub Category
     */
    @JsonProperty("businessSubCategory")
    private String businessSubCategory;

    /**
     * Business Category
     */
    @JsonProperty("businessCategory")
    private String businessCategory;

    /**
     * Account number
     */
    @JsonProperty("account_number")
    private String accountNumber;

    /**
     * Account creation date
     */
    @JsonProperty("account_creation_date")
    private Date accountCreationDate;

    /**
     * Gets Last name
     * 
     * @return - last name
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * Set Last name
     * 
     * @param familyName - Last name
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     * Gets account verified status
     * 
     * @return - true if paypal account is verified.
     */
    public boolean isVerified() {
        return verified;
    }

    /**
     * Set verified status
     * 
     * @param verified - status
     */
    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    /**
     * Get user locale
     * 
     * @return - Locale info
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Set locale
     * 
     * @param locale - User locale
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * Get zone info
     * 
     * @return - zone info
     */
    public String getZoneinfo() {
        return zoneinfo;
    }

    /**
     * Set zone info
     * 
     * @param zoneinfo - zone info
     */
    public void setZoneinfo(String zoneinfo) {
        this.zoneinfo = zoneinfo;
    }

    /**
     * Get first name
     * 
     * @return - first name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name
     * 
     * @param name - set first name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get user email
     * 
     * @return - email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set user email
     * 
     * @param email - email of user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets user full name
     * 
     * @return - full name
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * Sets full name
     * 
     * @param givenName - full name
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     * Get PayPal user id
     * 
     * @return - user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Set PayPal user id
     * 
     * @param userId - user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * PayPal User address
     * 
     * @return - user address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Sets user address
     * 
     * @param address - user address
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Get user password(usually access token as PayPal access does not share password)
     * 
     * @return - user password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets given string as user password
     * 
     * @param password - user password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * User birth date
     * 
     * @return - birth date
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * Sets user birth date
     * 
     * @param birthday - birth date
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * Address is listed as inner class to satisfy Jackson object mapper.
     * 
     */
    public static class Address implements Serializable {

        /**
         * default serial id
         */
        private static final long serialVersionUID = 1L;

        /**
         * Postal code
         */
        @JsonProperty("postal_code")
        private String postalCode;

        /**
         * User locality
         */
        @JsonProperty("locality")
        private String locality;

        /**
         * User state
         */
        @JsonProperty("region")
        private String region;

        /**
         * User country
         */
        @JsonProperty("country")
        private String country;

        /**
         * User street address.
         */
        @JsonProperty("street_address")
        private String streetAddress;

        /**
         * Gets user postal code
         * 
         * @return - Postal code
         */
        public String getPostalCode() {
            return postalCode;
        }

        /**
         * Sets user postal code
         * 
         * @param postalCode - user postal code
         */
        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        /**
         * Gets User locality
         * 
         * @return - user locality
         */
        public String getLocality() {
            return locality;
        }

        /**
         * Sets user locality
         * 
         * @param locality - user locality
         */
        public void setLocality(String locality) {
            this.locality = locality;
        }

        /**
         * Gets user region
         * 
         * @return - user region
         */
        public String getRegion() {
            return region;
        }

        /**
         * Sets user region
         * 
         * @param region - set region
         */
        public void setRegion(String region) {
            this.region = region;
        }

        /**
         * Get user country
         * 
         * @return - get country
         */
        public String getCountry() {
            return country;
        }

        /**
         * Set User country
         * 
         * @param country - get country
         */
        public void setCountry(String country) {
            this.country = country;
        }

        /**
         * Gets user address
         * 
         * @return - user address
         */
        public String getStreetAddress() {
            return streetAddress;
        }

        /**
         * Sets user address
         * 
         * @param streetAddress - user address
         */
        public void setStreetAddress(String streetAddress) {
            this.streetAddress = streetAddress;
        }

        /*
         * Address should encapsulate all properties of this instance.
         */
        @Override
        public String toString() {

            return this.streetAddress + "  " + this.locality + " " + this.region + " " + this.country + "  "
                    + this.postalCode;
        }

    }

    /**
     * Gets PayPal payer id
     * 
     * @return - payer id
     */
    public String getPayerId() {
        return payerId;
    }

    /**
     * Sets PayPal payer id
     * 
     * @param payerId - payer id
     */
    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    /**
     * Gets paypal account type
     * 
     * @return - paypal account
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Sets paypal account type
     * 
     * @param accountType - account type
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * Gets user language
     * 
     * @return - user language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets user language
     * 
     * @param language - language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Gets business name
     * 
     * @return business name
     */
    public String getBusinessName() {
        return businessName;
    }

    /**
     * Sets business name
     * 
     * @param businessName - user business name
     */
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    /**
     * Get business sub category
     * 
     * @return - sub category
     */
    public String getBusinessSubCategory() {
        return businessSubCategory;
    }

    /**
     * Set business sub category
     * 
     * @param businessSubCategory - sub category
     */
    public void setBusinessSubCategory(String businessSubCategory) {
        this.businessSubCategory = businessSubCategory;
    }

    /**
     * Get businsess category
     * 
     * @return - category
     */
    public String getBusinessCategory() {
        return businessCategory;
    }

    /**
     * Set business category
     * 
     * @param businessCategory - category
     */
    public void setBusinessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
    }

    /**
     * Account number
     * 
     * @return - Account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets account number
     * 
     * @param accountNumber - accountNumber
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Get Account creation date
     * 
     * @return - creation date
     */
    public Date getAccountCreationDate() {
        return accountCreationDate;
    }

    /**
     * Set Account creation date
     * 
     * @param accountCreationDate - creation date
     */
    public void setAccountCreationDate(Date accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
    }

    /**
     * Get user primary phone number
     * 
     * @return - phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set user phone number
     * 
     * @param phoneNumber - phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}