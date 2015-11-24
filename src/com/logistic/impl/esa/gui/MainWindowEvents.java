package com.logistic.impl.esa.gui;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.person.FullName;
import com.logistic.api.model.person.Person;
import com.logistic.api.model.post.Package;
import com.logistic.api.model.post.PostOffice;
import com.logistic.api.model.transport.DeliveryTransport;
import com.logistic.api.model.transport.Transit;
import com.logistic.impl.Logistic;
import com.logistic.impl.esa.generators.BigGenerator;
import com.logistic.impl.model.person.AddressImpl;
import com.logistic.impl.esa.serialization.PostsAndDeliveries;
import com.logistic.impl.esa.serialization.SerializationUtility;
import com.logistic.impl.model.person.PersonImpl;
import com.logistic.impl.model.post.PackageImpl;
import com.logistic.impl.model.post.PostOfficeImpl;
import com.logistic.impl.model.transport.DeliveryTransportImpl;
import com.logistic.impl.model.transport.DeliveryTransportImproved;
import com.logistic.impl.service.DataStorage;
import com.logistic.impl.service.SenderServiceImproved;
import com.logistic.impl.exceptions.NullPackageException;
import com.logistic.impl.exceptions.NullTransitException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;


/**
 * Created by SnakE on 20.11.2015.
 */
public class MainWindowEvents extends MouseAdapter implements ActionListener, ListSelectionListener {

    private MainWindow frame;
    private SenderServiceImproved postservice;

    MainWindowEvents(MainWindow mainWindow, SenderServiceImproved postservice) {
        this.frame = mainWindow;
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

        //  Radio buttons events handlers
        if (src instanceof JRadioButton) {
            if (src == frame.radioButtonNodesInfoIndex) {
                frame.graphPanel.setOfficeInfo(GraphPanel.PostOfficeInfo.INDEX);
            }
            if (src == frame.radioButtonNodesInfoPacks) {
                frame.graphPanel.setOfficeInfo(GraphPanel.PostOfficeInfo.PACKAGE);
            }
        }


        // Checkboxes events handlers
        if (src instanceof JCheckBox) {
            if (src == frame.checkBoxShowDirections) {
                frame.graphPanel.setDirections(((JCheckBox) src).isSelected());
            }
            if (src == frame.checkBoxShowAllTransits) {
                frame.graphPanel.setShowAllTransits(((JCheckBox) src).isSelected());
            }
        }


        // Buttons events handlers
        if (src instanceof JButton) {

            if (src == frame.buttonGeneratePackage) {
                generatePackage(false);
            }

            if (src == frame.buttonCreatePackage) {
                generatePackage(true);
            }

            if (src == frame.buttonSerialize) {
                serializeGraph();
            }

            if (src == frame.buttonDeserialize) {
                deserializeGraph();
            }

            if (src == frame.buttonGenerateGraph) {
                generateGraph();
            }

            if (src == frame.buttonCheckPackage) {
                checkPackage();
            }

            if (src == frame.buttonDeletePackages) {
                deleteReceived();
            }
        }


        // Popup menus
        if (src instanceof JMenuItem) {
            JMenuItem menu = (JMenuItem) e.getSource();
            if (menu == frame.menuItemAddEdge) {
                addEdge();
            }
            if (menu == frame.menuItemAddNode) {
                addNode();
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
            if (src == frame.listPackages) {
                Package p = frame.listPackages.getSelectedValue();
                if (p == null) return;
                frame.graphPanel.setAll(DataStorage.getTransit(p.getPackageId()),
                        DataStorage.getAvailableTransits(p.getPackageId()),
                        postservice.getPackageCurrentPosition(p.getPackageId()));
                frame.buttonCheckPackage.setText("Проверить "+p.getPackageId());
            }
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////
    //                                   Private methods                                        //
    //////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Generates new random graph
     */
    private void generateGraph() {
        DataStorage.initializeByRandomData(Logistic.WORLD_AREA, Logistic.POST_OFFICES_NUMBER, 80);
        frame.modelPackages.clear();
        frame.graphPanel.setAll(null, null, null);
        frame.buttonCheckPackage.setText("Проверить");
        frame.graphPanel.setPostOffices(DataStorage.getPostOffices(), DataStorage.getDeliveryTransports());

    }


    //////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Saves current graph to file
     */
    private void serializeGraph() {
        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int result = jfc.showSaveDialog(frame.graphPanel);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = jfc.getSelectedFile();
                PostsAndDeliveries pad = new PostsAndDeliveries(DataStorage.getPostOffices(), DataStorage.getDeliveryTransports());
                SerializationUtility.serializeToFile(pad, file);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(frame.graphPanel, e1.toString(), "Ошибка сериализации", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Loads graph from file
     */
    private void deserializeGraph() {
        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int result = jfc.showOpenDialog(frame.graphPanel);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = jfc.getSelectedFile();
                PostsAndDeliveries pad = (PostsAndDeliveries) SerializationUtility.deserializeFromFile(file);
                DataStorage.saveOfficesAndDeliveries(pad.getPostOffices(), pad.getDelivries());
                frame.graphPanel.setPostOffices(DataStorage.getPostOffices(), DataStorage.getDeliveryTransports());
            } catch (IOException | ClassNotFoundException e1) {
                JOptionPane.showMessageDialog(frame.graphPanel, e1.toString(), "Ошибка десериализации", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Generates new package and send it to sender
     */
    private void generatePackage(boolean setup) {
        boolean ok = true;
        Package parcel = null;
        List<Transit> transits = null;
        do {
            if (!setup) {
                parcel = BigGenerator.generatePackage();
            } else {
                parcel = createPackage();
                if (parcel == null) {
                    return;
                }
            }
            try {
                transits = postservice.calculatePossibleTransits(parcel);
                if (transits.size() < 1) {
                    ok = false;
                    if (setup) {
                        JOptionPane.showMessageDialog(frame.graphPanel,
                                "Невозможно подобрать маршрут для посылки."
                                        +"\r\nУкажите другую массу или выберите другие отделения.",
                                "Информация", JOptionPane.INFORMATION_MESSAGE);
                    }
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
            JOptionPane.showMessageDialog(frame.graphPanel, e1, "Ошибка сервиса доставки почты", JOptionPane.ERROR_MESSAGE);
        }
        frame.modelPackages.addElement(parcel);
        frame.listPackages.setSelectedIndex(frame.modelPackages.size() - 1);
        frame.graphPanel.setAll(transit, transits, postservice.getPackageCurrentPosition(parcel.getPackageId()));
    }


    //////////////////////////////////////////////////////////////////////////////////////////////


    private Package createPackage() {
        Integer indexA;
        String iA = JOptionPane.showInputDialog(frame.graphPanel, "Введите индекс почтового отделения отправителя (10000 - 99999): ", "10000");
        if (iA == null) {
            return null;
        }
        try {
            indexA = Integer.valueOf(iA);
            if ((indexA < 10000) || (indexA > 99999)) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(frame.graphPanel, e1, "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Integer indexB;
        String iB = JOptionPane.showInputDialog(frame.graphPanel, "Введите индекс почтового отделения получателя (10000 - 99999): ", "90000");
        if (iB == null) {
            return null;
        }
        try {
            indexB = Integer.valueOf(iB);
            if ((indexB < 10000) || (indexB > 99999)) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(frame.graphPanel, e1, "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        PostOffice postOfficeA = postservice.findClosestPostOffice(indexA);
        PostOffice postOfficeB = postservice.findClosestPostOffice(indexB);

        if (postOfficeA == postOfficeB) {
            JOptionPane.showMessageDialog(frame.graphPanel, "Адреса отправителя и получателя совпадают", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        FullName nameA = BigGenerator.generateFullName();
        FullName nameB = BigGenerator.generateFullName();

        Person personA = new PersonImpl(nameA, postOfficeA.getAddress());
        Person personB = new PersonImpl(nameB, postOfficeB.getAddress());

        Integer mass = 5;
        String m = JOptionPane.showInputDialog(frame.graphPanel, "Введите массу посылки (1 - 100): ", "3");
        if (m == null) {
            return null;
        }
        try {
            mass = Integer.valueOf(m);
        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(frame.graphPanel, e1, "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
        }

        return new PackageImpl(personA, personB, Package.Type.T_10, mass);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Checks package current position and shows message box
     */
    private void checkPackage() {
        Package pak = frame.listPackages.getSelectedValue();
        if (pak == null) return;
        PostOffice po = postservice.getPackageCurrentPosition(pak.getPackageId());

        if (frame.checkBoxShowMessages.isSelected()) {
            JOptionPane.showConfirmDialog(frame.graphPanel, packageInfo(pak, po),
                    "Информация о посылке "+ pak.getPackageId(), JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE);
        }

        frame.graphPanel.setLastKnownPostOffice(po);
        frame.graphPanel.repaint();

        if (postservice.isPackageDelivered(pak.getPackageId())) {
            int answer = JOptionPane.showConfirmDialog(frame.graphPanel,
                    "Посылка " + pak.getPackageId() + " доставлена. Удалить её из списка?",
                    "Вопрос", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                frame.modelPackages.removeElement(pak);
                frame.buttonCheckPackage.setText("Проверить");
                frame.graphPanel.setAll(null, null, null);
            }
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////


    private void deleteReceived() {
        for (int i = 0; i < frame.modelPackages.size(); i++) {
            Package p = frame.modelPackages.get(i);
            if (postservice.isPackageDelivered(p.getPackageId())) {
                frame.modelPackages.removeElement(p);
                i = -1;
            }
        }
        frame.buttonCheckPackage.setText("Проверить");
        frame.graphPanel.setAll(null, null, null);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Text for package information message
     * @param pak   package
     * @param po    last known post office
     * @return      message text
     */
    private String packageInfo (Package pak, PostOffice po) {
        return  "Отправитель: " + pak.getSenderFullName()
                + "\r\nПолучатель: " + pak.getReceiverFullName()
                + "\r\nМасса: " + pak.getWeight() + " кг"
                +"\r\nТип упаковки: " + pak.getType()
                + String.format("\r\nСтоимость отправки: $%.2f", DataStorage.getTransit(pak.getPackageId()).getPrice())
                + "\r\nВыбранный маршрут: " + DataStorage.getTransit(pak.getPackageId())
                + String.format("\r\nОбщее расстояние: %.3f км\r\nОстаось до конечного пункта: %.3f км",
                    DataStorage.getTransit(pak.getPackageId()).getOverallRange(), postservice.getMilesToDestination(pak.getPackageId()))
                + "\r\nПоследнее местоположение: " + po.getAddress()
                + "\r\nТипы посылок, принимаемые данным почтовым отделением: "+po.getAcceptablePackageTypes();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////


    private void addEdge() {
        try {
            Integer indexA = Integer.valueOf(JOptionPane.showInputDialog(frame.graphPanel, "Введите индекс почтового отделения (10000 - 99999): ", "Откуда двигаться"));
            Integer indexB = Integer.valueOf(JOptionPane.showInputDialog(frame.graphPanel, "Введите индекс почтового отделения (10000 - 99999): ", "Куда двигаться"));

            PostOffice postOfficeA = DataStorage.getByPostCode(indexA);
            PostOffice postOfficeB = DataStorage.getByPostCode(indexB);

            if (postOfficeA == null || postOfficeB == null) {
                throw new NullPointerException();
            }

            double distance = postOfficeA.getGeolocation().distance(postOfficeB.getGeolocation());
            DeliveryTransport.Type type = DeliveryTransport.Type.LAND;
            double maxRange = (frame.graphPanel.getHeight() + frame.graphPanel.getWidth()) / 2;
            if (distance > maxRange / 3) {
                type = DeliveryTransport.Type.AIR;
            }
            if (distance > maxRange / 1.3) {
                type = DeliveryTransport.Type.SEA;
            }

            DeliveryTransportImproved dt = new DeliveryTransportImpl(postOfficeA, postOfficeB, type);
            DataStorage.getDeliveryTransports().add(dt);
            frame.graphPanel.repaint();
        } catch (NumberFormatException | NullPointerException e1) {
            JOptionPane.showMessageDialog(frame.graphPanel, e1.toString(), "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////


    private void addNode() {
        Point p = frame.graphPanel.getGraphPoint();
        Address address = new AddressImpl(BigGenerator.generateIndex(p, frame.graphPanel.getSize()), BigGenerator.generateStreet(), BigGenerator.generateCityName(), BigGenerator.countryName);
        PostOffice npo = new PostOfficeImpl(address, p, BigGenerator.generatePackageTypes());
        DataStorage.getPostOffices().add(npo);
        frame.graphPanel.repaint();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////
}
