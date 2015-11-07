package com.logistic.impl;

import com.logistic.api.model.post.Package;
import com.logistic.impl.model.person.FullNameImpl;
import com.logistic.impl.model.person.PersonImpl;
import com.logistic.impl.model.post.PackageImpl;
import com.logistic.impl.service.DataStorage;
import com.logistic.impl.service.SenderServiceImpl;
import com.logistic.impl.service.generators.BigGenerator;
import com.logistic.impl.service.generators.RouteType;
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
            window.show();
            g.drawRoutes(DataStorage.getRoadRouteMatrix(RouteType.ROAD), DataStorage.getPostOffices(), Color.GRAY);
            g.drawRoutes(DataStorage.getRoadRouteMatrix(RouteType.AIR), DataStorage.getPostOffices(), Color.CYAN);
            g.drawNodes(DataStorage.getPostOffices());

        //-------------------------------------------------------

//        PersonImpl sender = new PersonImpl(new FullNameImpl("Василий", "Петрович", "Орлов"), new AddressImpl(10000, "ул. Фёдора, 11", "г. Хариков", "Страна Х"));
//        PersonImpl reciever = new PersonImpl(new FullNameImpl("Петр", "Васильевич", "Решкин"), new AddressImpl(99000, "ул. Банкирофф, 12", "г. Крапница", "Страна Х"));

        PersonImpl sender = new PersonImpl(new FullNameImpl("Василий", "Петрович", "Орлов"), BigGenerator.generateAddress());
        PersonImpl reciever = new PersonImpl(new FullNameImpl("Петр", "Васильевич", "Решкин"), BigGenerator.generateAddress());

            g.drawNode(SenderServiceImpl.findClosestPostOffice(sender.getAddress()), Color.RED);
            g.drawNode(SenderServiceImpl.findClosestPostOffice(reciever.getAddress()), Color.MAGENTA);

        window.update();

        PackageImpl p = new PackageImpl(sender, reciever, Package.Type.T_25, 10);
        SenderServiceImpl s = new SenderServiceImpl();

    }
}
