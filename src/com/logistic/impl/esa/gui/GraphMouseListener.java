package com.logistic.impl.esa.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by SnakE on 23.11.2015.
 */
public class GraphMouseListener extends MouseAdapter {

    private GraphPanel graphPanel;

    public GraphMouseListener(GraphPanel graph) {
        graphPanel = graph;
    }

    @Override
    public void mousePressed(MouseEvent event) {
        graphPanel.setGraphPoint(event.getPoint());
    }
}
