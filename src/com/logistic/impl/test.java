package com.logistic.impl;

import com.logistic.api.model.post.Package;
import com.logistic.api.model.transport.DeliveryTransport;
import com.logistic.impl.model.person.AddressImpl;
import com.logistic.impl.model.person.FullNameImpl;
import com.logistic.impl.model.person.PersonImpl;
import com.logistic.impl.model.post.PackageImpl;
import com.logistic.impl.model.transport.TransitImproved;
import com.logistic.impl.service.DataStorage;
import com.logistic.impl.service.SenderServiceImpl;
import com.logistic.impl.visual.Graph;
import com.logistic.impl.visual.Window;

import java.awt.*;


public class test {
    public static void main(String[] args) {

        //-----------------------------------------------------
            Graph g = new Graph(DataStorage.AREA);
            g.drawNodes(DataStorage.getPostOffices());
            Window window = new Window();
            window.setImg(g.getImage());
        //-------------------------------------------------------

        PersonImpl sender = new PersonImpl(new FullNameImpl("Василий", "Петрович", "Орлов"), new AddressImpl(10003, "Улица 1", "Город 1", "Страна 1"));
        PersonImpl reciever = new PersonImpl(new FullNameImpl("Петр", "Васильевич", "Решкин"), new AddressImpl(99000, "Улица 1", "Город 2", "Страна 1"));

        PackageImpl aPackage = new PackageImpl(sender, reciever, Package.Type.T_10, 10);

        SenderServiceImpl s = new SenderServiceImpl();

        g.drawTransits(s.calculatePossibleTransitsImproved(aPackage));

        g.drawRoutes(DataStorage.getDeliveryTransports());

        g.drawNodes(DataStorage.getPostOffices());

        g.drawNode(s.findClosestPostOffice(sender.getAddress()), Color.RED);
        g.drawNode(s.findClosestPostOffice(reciever.getAddress()), Color.MAGENTA);
        window.update();
    }

}
