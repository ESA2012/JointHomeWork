package com.logistic.impl.model.person;


import com.logistic.api.model.person.Person;

/**
 * Created by SnakE on 02.11.2015.
 */
public class PersonImpl implements Person {

    private AddressImpl address;
    private FullNameImpl fullName;

    public PersonImpl(FullNameImpl fullName, AddressImpl address) {
        this.fullName = fullName;
        this.address = address;
    }

    @Override
    public AddressImpl getAddress() {
        return address;
    }

    @Override
    public FullNameImpl getFullName() {
        return fullName;
    }
}
