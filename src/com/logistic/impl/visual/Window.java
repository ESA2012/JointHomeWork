package com.logistic.impl.visual;

import com.logistic.impl.model.post.PackageImproved;
import com.logistic.impl.service.SenderServiceImproved;

import javax.swing.*;
import java.awt.*;



/**
 * Created by SnakE on 06.11.2015.
 */
public class Window {

    Graph graph;
    JRadioButton nodesInfoIndex;
    JRadioButton nodesInfoPacks;
    JCheckBox showDirections;
    JCheckBox showAllTransits;
    DefaultListModel<PackageImproved> packagesModel;
    JList<PackageImproved> packagesList;
    JButton createPackage;
    JButton checkPackage;


    public Window(SenderServiceImproved service, Dimension graphArea) {
        JPanel content = new JPanel();

        WindowActions actions = new WindowActions(this, service);

        content.setLayout(null);

        // Radio buttons
        nodesInfoIndex = new JRadioButton("Отображать индексы");
        nodesInfoIndex.setBounds(720, 10, 220, 24);
        nodesInfoIndex.addActionListener(actions);
        nodesInfoIndex.setSelected(true);

        nodesInfoPacks = new JRadioButton("Отображать принимаемые грузы");
        nodesInfoPacks.setBounds(720, 40, 220, 24);
        nodesInfoPacks.addActionListener(actions);

        ButtonGroup group = new ButtonGroup();
        group.add(nodesInfoIndex);
        group.add(nodesInfoPacks);

        content.add(nodesInfoIndex);
        content.add(nodesInfoPacks);

        // CheckBox Show Directions
        showDirections = new JCheckBox("Отображать направления");
        showDirections.setBounds(720, 70, 220, 24);
        showDirections.addActionListener(actions);
        content.add(showDirections);

        // Package list
        packagesModel = new DefaultListModel<PackageImproved>();
        packagesList = new JList<>(packagesModel);
        ScrollPane scroll = new ScrollPane();
        scroll.setBounds(720, 100, 220, 200);
        packagesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        packagesList.addListSelectionListener(actions);
        scroll.add(packagesList);
        content.add(scroll);

        // CheckBox Show Directions
        showAllTransits = new JCheckBox("Отображать все доступные пути");
        showAllTransits.setBounds(720, 310, 220, 24);
        showAllTransits.setSelected(true);
        showAllTransits.addActionListener(actions);
        content.add(showAllTransits);

        // Generate package button
        createPackage = new JButton("Генерировать посылку");
        createPackage.setBounds(720, 340, 220, 24);
        createPackage.addActionListener(actions);
        content.add(createPackage);

        // Check package button
        checkPackage = new JButton("Проверить ");
        checkPackage.setBounds(720, 370, 220, 24);
        checkPackage.addActionListener(actions);
        content.add(checkPackage);


        // Graph
        graph = new Graph(graphArea);
        JScrollPane graphscroll = new JScrollPane(graph);
        graphscroll.setBounds(10, 10, 700, 600);
        content.add(graphscroll);

        // Window
        JFrame frame = new JFrame("Граф");
        frame.setContentPane(content);

        frame.setSize (970, 670);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }

    public Graph getGraph() {
        return graph;
    }

}
