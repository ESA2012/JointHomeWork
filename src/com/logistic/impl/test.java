package com.logistic.impl;

import com.logistic.api.model.post.Package;
import com.logistic.impl.model.person.AddressImpl;
import com.logistic.impl.model.person.FullNameImpl;
import com.logistic.impl.model.person.PersonImpl;
import com.logistic.impl.model.post.PackageImpl;
import com.logistic.impl.service.DataStorage;
import com.logistic.impl.service.SenderServiceImpl;
import com.logistic.impl.visual.Graph;
import com.logistic.impl.visual.Window;

import java.io.IOException;



public class test {
    public static void main(String[] args) throws IOException {

        //-----------------------------------------------------
            Graph g = new Graph(DataStorage.AREA);
            g.drawNodes(DataStorage.getPostOffices());
            Window window = new Window();
            window.setImg(g.getImage());
            window.show();
            g.drawRoutes(DataStorage.getRouteMatrix(), DataStorage.getPostOffices());
            g.drawNodes(DataStorage.getPostOffices());
//            window.update();
        //-------------------------------------------------------


        PersonImpl sender = new PersonImpl(new FullNameImpl("Василий", "Петрович", "Орлов"), new AddressImpl(10000, "ул. Фёдора, 11", "г. Хариков", "Страна Х"));
        PersonImpl reciever = new PersonImpl(new FullNameImpl("Петр", "Васильевич", "Решкин"), new AddressImpl(99000, "ул. Банкирофф, 12", "г. Крапница", "Страна Х"));

            g.selectNode(SenderServiceImpl.findClosestPostOffice(sender.getAddress()));
            g.selectNode(SenderServiceImpl.findClosestPostOffice(reciever.getAddress()));

        PackageImpl p = new PackageImpl(sender, reciever, Package.Type.T_25, 10);
        SenderServiceImpl s = new SenderServiceImpl();


    }
}
