package com.logistic.impl;

import com.logistic.impl.model.person.AddressImpl;
import com.logistic.impl.model.person.FullNameImpl;
import com.logistic.impl.model.person.PersonImpl;
import com.logistic.impl.model.post.PackageImpl;
import com.logistic.impl.service.DataStorage;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by SnakE on 04.11.2015.
 */
public class test {
    public static void main(String[] args) throws IOException {
        PersonImpl sender = new PersonImpl(new FullNameImpl("Sergey", "Alex", "Eryomin"), new AddressImpl(68001, "Red street, 14", "Kharkov", "Novorossia"));
        PersonImpl reciever = new PersonImpl(new FullNameImpl("Peter", "Alex", "Poroshenko"), new AddressImpl(31001, "Bank street, 14", "Kiev", "Bandershadt"));

        PackageImpl p = new PackageImpl(sender, reciever, com.logistic.api.model.post.Package.Type.getRandomType(), 10);

        System.out.println(p.getPackageId());


        DataStorage.readData();
    }
}
