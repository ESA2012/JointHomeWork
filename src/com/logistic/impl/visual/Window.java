package com.logistic.impl.visual;


import com.logistic.api.model.post.Package;
import com.logistic.impl.model.post.PostOfficeImproved;
import com.logistic.impl.service.DataStorage;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListDataListener;
import java.awt.*;



/**
 * Created by SnakE on 06.11.2015.
 */
public class Window {

    private Graph graph;

    public Window(Rectangle graphArea) {
        JPanel content = new JPanel();

        content.setLayout(null);

        JButton button1 = new JButton("Button 1");
        button1.setBounds(graphArea.width+20, 10, 100, 24);
        content.add(button1);

        graph = new Graph(graphArea);
        graph.setBounds(10,10, graphArea.width, graphArea.height);
        content.add(graph);

        JFrame frame = new JFrame("Граф");
        frame.setContentPane(content);

        frame.setSize(graphArea.width + 200, graphArea.height + 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }

    public Graph getGraph() {
        return graph;
    }

}
