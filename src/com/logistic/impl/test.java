package com.logistic.impl;

import com.logistic.api.model.post.Package;
import com.logistic.api.model.transport.Transit;
import com.logistic.impl.model.person.AddressImpl;
import com.logistic.impl.model.person.FullNameImpl;
import com.logistic.impl.model.person.PersonImpl;
import com.logistic.impl.model.post.PackageImpl;
import com.logistic.impl.service.DataStorage;
import com.logistic.impl.service.SenderServiceImpl;
import com.logistic.impl.service.exceptions.NullPackageException;
import com.logistic.impl.service.exceptions.NullTransitException;
import com.logistic.impl.visual.Window;

import java.awt.*;
import java.util.List;

import static java.lang.Thread.sleep;


public class test {
    final static Rectangle AREA = new Rectangle(800, 600);

    public static void main(String[] args) throws InterruptedException {

        DataStorage.initializeByRandomData(AREA, 30, 80);

        Window window = new Window(AREA);


        PersonImpl sender1 = new PersonImpl(new FullNameImpl("Василий", "Петрович", "Орлов"), new AddressImpl(10003, "Улица 1", "Город 1", "Страна 1"));
        PersonImpl reciever1 = new PersonImpl(new FullNameImpl("Петр", "Васильевич", "Решкин"), new AddressImpl(99000, "Улица 1", "Город 2", "Страна 1"));

        PersonImpl sender2 = new PersonImpl(new FullNameImpl("Василий", "Петрович", "Орлов"), new AddressImpl(40003, "Улица 1", "Город 1", "Страна 1"));
        PersonImpl reciever2 = new PersonImpl(new FullNameImpl("Петр", "Васильевич", "Решкин"), new AddressImpl(20000, "Улица 1", "Город 2", "Страна 1"));


        SenderServiceImpl s = new SenderServiceImpl();


        PackageImpl aPackage1 = new PackageImpl(sender1, reciever1, Package.Type.T_10, 10);
        PackageImpl aPackage2 = new PackageImpl(sender2, reciever2, Package.Type.T_10, 10);
        PackageImpl aPackage3 = new PackageImpl(sender1, reciever1, Package.Type.T_10, 10);

        List<Transit> transits1 = null;
        List<Transit> transits2 = null;
        List<Transit> transits3 = null;

        try {
            transits1 = s.calculatePossibleTransits(aPackage1);
            transits2 = s.calculatePossibleTransits(aPackage2);
            transits3 = s.calculatePossibleTransits(aPackage3);
        } catch (NullPackageException e) {
            System.out.println(e);
        }

        window.getGraph().setPostOffices(s.getAllOffices(), DataStorage.getDeliveryTransports());
        window.getGraph().setTransits(transits1);
        sleep(2000);
        window.getGraph().setTransits(transits2);
        sleep(2000);
        window.getGraph().setTransits(transits3);

        try {
            assert transits1 != null;
//            s.sendPackage(aPackage1, transits1.get(0));
            assert transits2 != null;
            s.sendPackage(aPackage1, transits1.get(0));
            assert transits3 != null;
//            s.sendPackage(aPackage3, transits1.get(2));
        } catch (NullPackageException | NullTransitException e) {
            System.out.println(e);
        }






    }

}
