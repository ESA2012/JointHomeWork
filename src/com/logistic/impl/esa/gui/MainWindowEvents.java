package com.logistic.impl.esa.gui;

import com.logistic.api.model.post.Package;
import com.logistic.impl.service.DataStorage;
import com.logistic.impl.service.SenderServiceImproved;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;


/**
 * Created by SnakE on 20.11.2015.
 */
public class MainWindowEvents extends MouseAdapter implements ActionListener, ListSelectionListener {

    private MainWindow frame;
    private SenderServiceImproved postservice;
    private GUIservice controls;

    MainWindowEvents(MainWindow mainWindow, SenderServiceImproved postservice) {
        this.frame = mainWindow;
        this.postservice = postservice;
        controls = new GUIservice(mainWindow, postservice);
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        //  Radio buttons events handlers
        if (src instanceof JRadioButton) {
            if (src == frame.getRadioButtonNodesInfoIndex()) {
                frame.getGraphPanel().setOfficeInfo(GraphPanel.PostOfficeInfo.INDEX);
            }
            if (src == frame.getRadioButtonNodesInfoPacks()) {
                frame.getGraphPanel().setOfficeInfo(GraphPanel.PostOfficeInfo.PACKAGE);
            }
        }


        // Checkboxes events handlers
        if (src instanceof JCheckBox) {
            if (src == frame.getCheckBoxShowDirections()) {
                frame.getGraphPanel().setDirections(((JCheckBox) src).isSelected());
            }
            if (src == frame.getCheckBoxShowAllTransits()) {
                frame.getGraphPanel().setShowAllTransits(((JCheckBox) src).isSelected());
            }
        }


        // Buttons events handlers
        if (src instanceof JButton) {

            if (src == frame.getButtonGeneratePackage()) {
                controls.generatePackage(false);
            }

            if (src == frame.getButtonCreatePackage()) {
                controls.generatePackage(true);
            }

            if (src == frame.getButtonSerialize()) {
                controls.serializeGraph();
            }

            if (src == frame.getButtonDeserialize()) {
                controls.deserializeGraph();
            }

            if (src == frame.getButtonGenerateGraph()) {
                controls.generateGraph();
            }

            if (src == frame.getButtonCheckPackage()) {
                controls.checkPackage();
            }

            if (src == frame.getButtonDeletePackages()) {
                controls.deleteReceived();
            }
        }


        // Popup menus
        if (src instanceof JMenuItem) {
            JMenuItem menu = (JMenuItem) e.getSource();
            if (menu == frame.getMenuItemAddEdge()) {
                controls.addEdge();
            }
            if (menu == frame.getMenuItemAddNode()) {
                controls.addNode();
            }
        }
    }



    /**
     * Called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        Object src = e.getSource();
        if (src instanceof JList) {
            if (src == frame.getListPackages()) {
                Package p = frame.getListPackages().getSelectedValue();
                if (p == null) return;
                frame.getGraphPanel().setAll(DataStorage.getTransit(p.getPackageId()),
                        DataStorage.getAvailableTransits(p.getPackageId()),
                        postservice.getPackageCurrentPosition(p.getPackageId()));
                frame.getButtonCheckPackage().setText("Проверить "+p.getPackageId());
            }
        }
    }

}
