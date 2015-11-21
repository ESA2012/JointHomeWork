package com.logistic.impl;

import com.logistic.impl.service.DataStorage;
import com.logistic.impl.service.SenderServiceImpl;
import com.logistic.impl.visual.Window;

import java.awt.*;



public class Logistic {
    final static Dimension GRAPH_SIZE = new Dimension(800,800);

    public static void main(String[] args) throws InterruptedException {

        DataStorage.initializeByRandomData(GRAPH_SIZE, 40, 70);

        SenderServiceImpl s = new SenderServiceImpl();
        Window window = new Window(s, GRAPH_SIZE);

        window.getGraph().setPostOffices(s.getAllOffices(), DataStorage.getDeliveryTransports());

    }

}
