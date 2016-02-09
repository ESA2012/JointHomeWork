package com.logistic.impl.model.person;

import com.logistic.api.model.person.Address;

import java.io.Serializable;


/**
 * Created by SnakE on 02.11.2015.
 */
public class AddressImpl implements Address, Serializable {
    private String street;
    private String city;
    private String country;
    private int code;

    public AddressImpl(int code, String street, String city, String country) {
        this.code = code;
        this.street = street;
        this.city = city;
        this.country = country;
    }

    @Override
    public String getStreet() {
        return street;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null & o instanceof Address) {
            Address address = (Address) o;
            return address.toString().equals(this.toString());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("%d, %s, %s, %s", code, street, city, country);
    }
}
