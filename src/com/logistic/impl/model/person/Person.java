package com.logistic.impl.model.person;



/**
 * Created by SnakE on 02.11.2015.
 */
public class Person implements com.logistic.api.model.person.Person {

    private Address address;
    private FullName fullName;

    public Person(FullName fullName) {
        this.fullName = fullName;
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
