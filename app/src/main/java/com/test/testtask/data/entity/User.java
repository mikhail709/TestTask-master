package com.test.testtask.data.entity;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("address")
    private Address address;

    @SerializedName("phone")
    private String phone;

    @SerializedName("website")
    private String website;

    @SerializedName("company")
    private Company company;

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public int getId() {
        return id;
    }

    public Company getCompany() {
        return company;
    }

    public class Address {
        @SerializedName("street")
        private String street;

        @SerializedName("suite")
        private String suite;

        @SerializedName("city")
        private String city;

        @SerializedName("zipcode")
        private String zipcode;

        @SerializedName("geo")
        private Geo geo;

        public String getCity() {
            return city;
        }

        public Geo getGeo() {
            return geo;
        }

        public String getStreet() {
            return street;
        }

        public String getSuite() {
            return suite;
        }

        public String getZipcode() {
            return zipcode;
        }
    }

    public class Geo {
        @SerializedName("lat")
        private String lat;

        @SerializedName("lng")
        private String lng;

        public String getLat() {
            return lat;
        }

        public String getLng() {
            return lng;
        }
    }

    public class Company {
        @SerializedName("name")
        private String name;

        @SerializedName("catchPhrase")
        private String catchPhrase;

        @SerializedName("bs")
        private String bs;

        public String getName() {
            return name;
        }

        public String getCatchPhrase() {
            return catchPhrase;
        }

        public String getBs() {
            return bs;
        }
    }

}