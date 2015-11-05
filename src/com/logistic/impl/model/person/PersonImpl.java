package com.logistic.impl.model.person;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.person.FullName;
import com.logistic.api.model.person.Person;


/**
 * Created by SnakE on 02.11.2015.
 */
public class PersonImpl implements Person {

    private Address address;
    private FullName fullName;

    public PersonImpl(FullNameImpl fullName, Address address) {
        this.fullName = fullName;
        this.address = address;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public FullName getFullName() {
        return fullName;
    }
}
