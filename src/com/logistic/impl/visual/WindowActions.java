package com.logistic.impl.visual;

import com.logistic.api.model.post.Package;
import com.logistic.api.model.post.PostOffice;
import com.logistic.api.model.transport.Transit;
import com.logistic.impl.generators.PackageGenerator;
import com.logistic.impl.model.post.PackageImproved;
import com.logistic.impl.service.DataStorage;
import com.logistic.impl.service.SenderServiceImproved;
import com.logistic.impl.service.exceptions.NullPackageException;
import com.logistic.impl.service.exceptions.NullTransitException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;


/**
 * Created by SnakE on 20.11.2015.
 */
public class WindowActions implements ActionListener, ListSelectionListener {

    private Window window;
    private SenderServiceImproved postservice;

    WindowActions(Window window, SenderServiceImproved postservice) {
        this.window = window;
        this.postservice = postservice;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        // Select post office information to draw
        if (src instanceof JRadioButton) {
            if (src == window.nodesInfoIndex) {
                window.graph.setOfficeInfo(Graph.PostOfficeInfo.INDEX);
            }
            if (src == window.nodesInfoPacks) {
                window.graph.setOfficeInfo(Graph.PostOfficeInfo.PACKAGE);
            }
        }

        // Draw directions
        if (src instanceof JCheckBox) {
            if (src == window.showDirections) {
                window.graph.setDirections(((JCheckBox) src).isSelected());
            }
            if (src == window.showAllTransits) {
                window.graph.setShowAllTransits(((JCheckBox) src).isSelected());
            }
        }

        // Random generate package
        if (src instanceof JButton) {
            if (src == window.createPackage) {
                boolean ok = true;
                PackageImproved parcel;
                List<Transit> transits = null;
                do {
                    parcel = PackageGenerator.generatePackage(Package.Type.T_10);
                    try {
                        transits = postservice.calculatePossibleTransits(parcel);
                        if (transits.size() < 1) {
                            ok = false;
                            continue;
                        } else {
                            ok = true;
                        }
                        DataStorage.saveAvailableTransits(parcel, transits);
                    } catch (NullPackageException e1) {
                        e1.printStackTrace();
                    }
                } while (!ok);

                Transit transit = transits.get(new Random().nextInt(transits.size()));

                try {
                    postservice.sendPackage(parcel, transit);
                } catch (NullPackageException | NullTransitException e1) {
                    e1.printStackTrace();
                }
                window.packagesModel.addElement(parcel);
                window.packagesList.setSelectedIndex(window.packagesModel.size() - 1);
                window.graph.setPackage(parcel);
                window.graph.setTransit(transit);
                window.graph.setTransits(transits);
                window.graph.setLastKnownPostOffice(postservice.getPackageCurrentPosition(parcel.getPackageId()));
                window.graph.repaint();
            }

            if (src == window.checkPackage) {
                Package pak = window.packagesList.getSelectedValue();
                if (pak == null) return;
                PostOffice po = postservice.getPackageCurrentPosition(pak.getPackageId());
                window.graph.setLastKnownPostOffice(po);
                String message = "Отправитель: " + pak.getSenderFullName()
                        + "\r\nПолучатель: " + pak.getReceiverFullName()
                        + "\r\nМасса: " + pak.getWeight() + " кг             Тип упаковки: " + pak.getType()
                        + "\r\nПоследнее местоположение: " + po.toString()
                        + "\r\nОстаось до конечного пункта: "
                        + String.format("%10f км", postservice.getMilesToDestination(pak.getPackageId()));
                JOptionPane.showConfirmDialog(window.graph, message,
                        "Текущая информация о посылке "+ pak.getPackageId(),
                        JOptionPane.PLAIN_MESSAGE);

                if (((PackageImproved)pak).getReceiverPostOfficeAddreess().equals(postservice.getPackageCurrentPosition(pak.getPackageId()).getAddress())) {
                    int answer = JOptionPane.showConfirmDialog(window.graph,
                            "Посылка "+pak.getPackageId()+" доставлена. Удалить её из списка?",
                            "Вопрос", JOptionPane.YES_NO_OPTION);
                    if (answer == JOptionPane.YES_OPTION) {
                        window.packagesModel.removeElement(pak);
                        window.graph.setPackage(null);
                        window.graph.setTransit(null);
                        window.graph.setTransits(null);
                        window.graph.setLastKnownPostOffice(null);
                        window.graph.repaint();
                    }
                }
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
            if (src == window.packagesList) {
                PackageImproved p = window.packagesList.getSelectedValue();
                if (p == null) return;
                window.graph.setPackage(p);
                window.graph.setLastKnownPostOffice(postservice.getPackageCurrentPosition(p.getPackageId()));
                window.graph.setTransit(DataStorage.getTransit(p.getPackageId()));
                window.graph.setTransits(DataStorage.getAvailableTransits(p.getPackageId()));
                window.graph.repaint();
                window.checkPackage.setText("Проверить "+p.getPackageId());
            }
        }
    }
}
