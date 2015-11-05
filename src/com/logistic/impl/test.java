package com.logistic.impl;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.post.*;
import com.logistic.api.model.post.Package;
import com.logistic.api.model.transport.Transit;
import com.logistic.impl.model.person.AddressImpl;
import com.logistic.impl.model.person.FullNameImpl;
import com.logistic.impl.model.person.PersonImpl;
import com.logistic.impl.model.post.PackageImpl;
import com.logistic.impl.model.transport.TransitImpl;
import com.logistic.impl.service.DataStorage;
import com.logistic.impl.service.SenderServiceImpl;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by SnakE on 04.11.2015.
 */
public class test {
    public static void main(String[] args) throws IOException {
        DataStorage.readData();

        PersonImpl sender = new PersonImpl(new FullNameImpl("Василий", "Петрович", "Орлов"), new AddressImpl(1000, "ул. Фёдора, 11", "г. Хариков", "Страна Х"));
        PersonImpl reciever = new PersonImpl(new FullNameImpl("Петр", "Васильевич", "Решкин"), new AddressImpl(9900, "ул. Банкирофф, 12", "г. Крапница", "Страна Х"));

        PackageImpl p = new PackageImpl(sender, reciever, Package.Type.T_25, 10);
        SenderServiceImpl s = new SenderServiceImpl();


        ArrayList<Transit> transits = (ArrayList<Transit>) s.calculatePossibleTransits(p);
        for (PostOffice p1: transits.get(0).getTransitOffices()) {
            System.out.println(p1);
        }

    }
}
