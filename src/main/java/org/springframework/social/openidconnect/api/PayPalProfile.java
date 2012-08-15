package org.springframework.social.openidconnect.api;

import java.io.Serializable;

/**
 * <p>
 * Represents the user information given by PayPal access.
 * </p>
 * Note: This has to be adapted according to the scope you are providing.
 * 
 * @author abprabhakar
 * 
 */
public class PayPalProfile implements Serializable {

    /**
     * Default serializable version id.
     */
    private static final long serialVersionUID = 1L;

    private String familyName;

    private boolean verified;

    private String locale;

    private String zoneinfo;

    private String name;

    private String email;

    private String givenName;

    private String userId;

    private Address address;

    private String password;

    private String birthday;

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

    public static class Address {

        private String postalCode;

        private String locality;

        private String region;

        private String country;

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

}