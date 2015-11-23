package com.logistic.impl;

import com.logistic.impl.esa.gui.MainWindow;
import com.logistic.impl.service.DataStorage;
import com.logistic.impl.service.SenderServiceImpl;

import java.awt.*;



public class Logistic {

    public static final Dimension   WORLD_AREA          = new Dimension(800,800);
    public static final int         POST_OFFICES_NUMBER = 40;

    public static void main(String[] args) throws InterruptedException, ClassNotFoundException {

        DataStorage.initializeByRandomData(WORLD_AREA, POST_OFFICES_NUMBER, 80);

        new MainWindow(new SenderServiceImpl()).getGraphPanel().setPostOffices(DataStorage.getPostOffices(), DataStorage.getDeliveryTransports());

    }

}
