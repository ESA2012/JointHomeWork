package com.logistic.impl.generators;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.person.FullName;
import com.logistic.api.model.person.Person;
import com.logistic.api.model.post.*;
import com.logistic.api.model.post.Package;
import com.logistic.impl.model.person.FullNameImpl;
import com.logistic.impl.model.person.PersonImpl;
import com.logistic.impl.model.post.PackageImpl;
import com.logistic.impl.model.post.PackageImproved;

import java.util.Random;

/**
 * Created by SnakE on 20.11.2015.
 */
public class PackageGenerator {

    private static String[] lNames = {"Иванов", "Петров", "Сидоров", "Гончаров", "Дружко", "Вражко"};
    private static String[] fNames = {"Иван", "Пётр", "Андрей", "Данила", "Геннадий", "Юрий"};
    private static String[] mNames = {"Иванович", "Петрович", "Андреевич", "Данилович", "Геннадиевич", "Юриевич"};

    public static Person generatePerson() {
        Address address = BigGenerator.generateAddress();

        String f = fNames[new Random().nextInt(fNames.length)];
        String l = lNames[new Random().nextInt(lNames.length)];
        String m = mNames[new Random().nextInt(mNames.length)];

        FullName fullName = new FullNameImpl(f,m,l);

        return  new PersonImpl(fullName, address);
    }

    public static PackageImproved generatePackage() {
        return generatePackage(Package.Type.getRandomType());
    }

    public static PackageImproved generatePackage(Package.Type ptype) {
        return new PackageImpl(generatePerson(), generatePerson(), ptype, new Random().nextInt(50)+1);
    }


}
