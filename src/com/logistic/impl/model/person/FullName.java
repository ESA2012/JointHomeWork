package com.logistic.impl.model.person;

/**
 * Created by SnakE on 02.11.2015.
 */
public class FullName implements com.logistic.api.model.person.FullName {
    private String firstName;
    private String middleName;
    private String lastName;

    public FullName(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getMiddleName() {
        return middleName;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", lastName, firstName, middleName);
    }
}
