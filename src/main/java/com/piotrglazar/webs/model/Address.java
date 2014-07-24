package com.piotrglazar.webs.model;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private String city;

    private String country;

    public Address() {

    }

    public Address(final String city, final String country) {
        this.city = city;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public void setCountry(final String country) {
        this.country = country;
    }
}
