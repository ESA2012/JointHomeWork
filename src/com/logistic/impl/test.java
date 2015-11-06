package com.logistic.impl;

import com.logistic.api.model.post.Package;
import com.logistic.impl.model.person.AddressImpl;
import com.logistic.impl.model.person.FullNameImpl;
import com.logistic.impl.model.person.PersonImpl;
import com.logistic.impl.model.post.PackageImpl;
import com.logistic.impl.service.DataStorage;
import com.logistic.impl.service.SenderServiceImpl;
import com.logistic.impl.visual.Graph;

import java.io.IOException;

import static java.lang.Thread.sleep;


public class test {
    public static void main(String[] args) throws IOException, InterruptedException {
        DataStorage.readPostOfficesData();

        //-----------------------------------------------------
        Graph g = new Graph(640, 480);



        g.drawNodes(DataStorage.getPostOffices());

        Window window = new Window();
        window.setImg(g.getImage());
        window.show();

        //sleep(2000);
        g.drawRoutes(DataStorage.getRouteMatrix(), DataStorage.getPostOffices());
        g.drawNodes(DataStorage.getPostOffices());
        window.update();

        //-------------------------------------------------------

        PersonImpl sender = new PersonImpl(new FullNameImpl("Василий", "Петрович", "Орлов"), new AddressImpl(10000, "ул. Фёдора, 11", "г. Хариков", "Страна Х"));
        PersonImpl reciever = new PersonImpl(new FullNameImpl("Петр", "Васильевич", "Решкин"), new AddressImpl(99000, "ул. Банкирофф, 12", "г. Крапница", "Страна Х"));

        PackageImpl p = new PackageImpl(sender, reciever, Package.Type.T_25, 10);

        SenderServiceImpl s = new SenderServiceImpl();
    }
}
