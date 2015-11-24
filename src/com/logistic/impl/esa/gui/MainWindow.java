package com.logistic.impl.esa.gui;

import com.logistic.impl.service.SenderServiceImproved;
import com.logistic.api.model.post.Package;

import javax.swing.*;
import java.awt.*;



/**
 * Created by SnakE on 06.11.2015.
 */
public class MainWindow {

    GraphPanel graphPanel;
    JRadioButton radioButtonNodesInfoIndex;
    JRadioButton radioButtonNodesInfoPacks;
    JCheckBox checkBoxShowDirections;
    JCheckBox checkBoxShowAllTransits;
    JCheckBox checkBoxShowMessages;
    DefaultListModel<Package> modelPackages;
    JList<Package> listPackages;
    JButton buttonGeneratePackage;
    JButton buttonCreatePackage;
    JButton buttonCheckPackage;
    JButton buttonSerialize;
    JButton buttonDeserialize;
    JButton buttonGenerateGraph;
    JButton buttonDeletePackages;
    JMenuItem menuItemAddNode;
    JMenuItem menuItemAddEdge;


    public MainWindow(SenderServiceImproved service) {
        JPanel content = new JPanel();

        MainWindowEvents actions = new MainWindowEvents(this, service);

        content.setLayout(null);

        // Radio buttons
        radioButtonNodesInfoIndex = new JRadioButton("Отображать индексы");
        radioButtonNodesInfoIndex.setBounds(720, 10, 230, 24);
        radioButtonNodesInfoIndex.addActionListener(actions);
        radioButtonNodesInfoIndex.setSelected(true);

        radioButtonNodesInfoPacks = new JRadioButton("Отображать принимаемые грузы");
        radioButtonNodesInfoPacks.setBounds(720, 40, 230, 24);
        radioButtonNodesInfoPacks.addActionListener(actions);

        ButtonGroup group = new ButtonGroup();
        group.add(radioButtonNodesInfoIndex);
        group.add(radioButtonNodesInfoPacks);

        content.add(radioButtonNodesInfoIndex);
        content.add(radioButtonNodesInfoPacks);

        // CheckBox Show Directions
        checkBoxShowDirections = new JCheckBox("Отображать направления");
        checkBoxShowDirections.setBounds(720, 70, 230, 24);
        checkBoxShowDirections.addActionListener(actions);
        content.add(checkBoxShowDirections);

        // Package list
        modelPackages = new DefaultListModel<Package>();
        listPackages = new JList<>(modelPackages);
        ScrollPane scroll = new ScrollPane();
        scroll.setBounds(720, 100, 230, 200);
        listPackages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listPackages.addListSelectionListener(actions);
        scroll.add(listPackages);
        content.add(scroll);

        // CheckBox Show Directions
        checkBoxShowAllTransits = new JCheckBox("Отображать все доступные пути");
        checkBoxShowAllTransits.setBounds(720, 310, 230, 24);
        checkBoxShowAllTransits.setSelected(true);
        checkBoxShowAllTransits.addActionListener(actions);
        content.add(checkBoxShowAllTransits);

        // Generate package button
        buttonGeneratePackage = new JButton("Генерировать посылку");
        buttonGeneratePackage.setBounds(720, 340, 230, 24);
        buttonGeneratePackage.addActionListener(actions);
        content.add(buttonGeneratePackage);

        // Create package button
        buttonCreatePackage = new JButton("Создать посылку...");
        buttonCreatePackage.setBounds(720, 370, 230, 24);
        buttonCreatePackage.addActionListener(actions);
        content.add(buttonCreatePackage);

        // Check package button
        buttonCheckPackage = new JButton("Проверить ");
        buttonCheckPackage.setBounds(720, 400, 230, 24);
        buttonCheckPackage.addActionListener(actions);
        content.add(buttonCheckPackage);

        // CheckBox Show Messages
        checkBoxShowMessages = new JCheckBox("Показывать сообщение");
        checkBoxShowMessages.setBounds(720, 430, 230, 24);
        checkBoxShowMessages.setSelected(true);
        checkBoxShowMessages.addActionListener(actions);
        content.add(checkBoxShowMessages);

        // Delete received packages button
        buttonDeletePackages = new JButton("Удалить доставленные");
        buttonDeletePackages.setBounds(720, 460, 230, 24);
        buttonDeletePackages.addActionListener(actions);
        content.add(buttonDeletePackages);

        // Generate graph
        buttonGenerateGraph = new JButton("Генерировать новый граф");
        buttonGenerateGraph.setBounds(720, 540, 230, 24);
        buttonGenerateGraph.addActionListener(actions);
        content.add(buttonGenerateGraph);

        // Serialize button
        buttonSerialize = new JButton("Сохранить...");
        buttonSerialize.setBounds(720, 570, 110, 24);
        buttonSerialize.addActionListener(actions);
        content.add(buttonSerialize);

        // Deserialize button
        buttonDeserialize = new JButton("Загрузить...");
        buttonDeserialize.setBounds(840, 570, 110, 24);
        buttonDeserialize.addActionListener(actions);
        content.add(buttonDeserialize);

        // PopUpMenu
        JPopupMenu popupMenuGraph = new JPopupMenu();
        menuItemAddNode = new JMenuItem("Добавить почтовое отделение...");
        menuItemAddEdge = new JMenuItem("Добавить связь...");
        menuItemAddEdge.addActionListener(actions);
        menuItemAddNode.addActionListener(actions);
        popupMenuGraph.add(menuItemAddNode);
        popupMenuGraph.add(menuItemAddEdge);

        // GraphPanel
        graphPanel = new GraphPanel();
        graphPanel.setComponentPopupMenu(popupMenuGraph);
//        graphPanel.addMouseListener(new GraphMouseListener(graphPanel));      // does not work in Linux
        graphPanel.addMouseMotionListener(new GraphMouseListener(graphPanel));  // works everywhere but is not optimal
        JScrollPane graphscroll = new JScrollPane(graphPanel);
        graphscroll.setBounds(10, 10, 700, 600);
        content.add(graphscroll);

        // MainWindow
        JFrame frame = new JFrame("Моделирование работы почтовых отделений");
        frame.setContentPane(content);
        frame.setSize (970, 670);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public GraphPanel getGraphPanel() {
        return graphPanel;
    }

}
