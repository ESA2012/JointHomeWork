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
import com.logistic.impl.esa.serialization.PostsAndDeliveries;
import com.logistic.impl.esa.serialization.SerializationUtility;
import com.logistic.impl.exceptions.NullPackageException;
import com.logistic.impl.exceptions.NullTransitException;
import com.logistic.impl.model.person.AddressImpl;
import com.logistic.impl.model.person.PersonImpl;
import com.logistic.impl.model.post.PackageImpl;
import com.logistic.impl.model.post.PostOfficeImpl;
import com.logistic.impl.model.transport.DeliveryTransportImpl;
import com.logistic.impl.model.transport.DeliveryTransportImproved;
import com.logistic.impl.service.DataStorage;
import com.logistic.impl.service.SenderServiceImproved;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by snake on 09.02.16.
 */
public class GUIservice {

    private MainWindow frame;
    private SenderServiceImproved postservice;

    public GUIservice(MainWindow frame, SenderServiceImproved postservice) {
        this.frame = frame;
        this.postservice = postservice;
    }

    /**
     * Generates new random graph
     */
    public void generateGraph() {
        DataStorage.initializeByRandomData(Logistic.WORLD_AREA, Logistic.POST_OFFICES_NUMBER, 80);
        frame.getModelPackages().clear();
        frame.getGraphPanel().setAll(null, null, null);
        frame.getButtonCheckPackage().setText("Проверить");
        frame.getGraphPanel().setPostOffices(DataStorage.getPostOffices(), DataStorage.getDeliveryTransports());

    }


    //////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Saves current graph to file
     */
    public void serializeGraph() {
        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int result = jfc.showSaveDialog(frame.getGraphPanel());
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = jfc.getSelectedFile();
                PostsAndDeliveries pad = new PostsAndDeliveries(DataStorage.getPostOffices(), DataStorage.getDeliveryTransports());
                SerializationUtility.serializeToFile(pad, file);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(frame.getGraphPanel(), e1.toString(), "Ошибка сериализации", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Loads graph from file
     */
    public void deserializeGraph() {
        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int result = jfc.showOpenDialog(frame.getGraphPanel());
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = jfc.getSelectedFile();
                PostsAndDeliveries pad = (PostsAndDeliveries) SerializationUtility.deserializeFromFile(file);
                DataStorage.saveOfficesAndDeliveries(pad.getPostOffices(), pad.getDelivries());
                frame.getGraphPanel().setPostOffices(DataStorage.getPostOffices(), DataStorage.getDeliveryTransports());
            } catch (IOException | ClassNotFoundException e1) {
                JOptionPane.showMessageDialog(frame.getGraphPanel(), e1.toString(), "Ошибка десериализации", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Generates new package and send it to sender
     */
    public void generatePackage(boolean setup) {
        boolean ok = true;
        Package parcel;
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
                        JOptionPane.showMessageDialog(frame.getGraphPanel(),
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
            JOptionPane.showMessageDialog(frame.getGraphPanel(), e1, "Ошибка сервиса доставки почты", JOptionPane.ERROR_MESSAGE);
        }
        frame.getModelPackages().addElement(parcel);
        frame.getListPackages().setSelectedIndex(frame.getModelPackages().size() - 1);
        frame.getGraphPanel().setAll(transit, transits, postservice.getPackageCurrentPosition(parcel.getPackageId()));
    }


    //////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Creates new package
     * @return package
     */
    private Package createPackage() {
        Integer indexA;
        String iA = JOptionPane.showInputDialog(frame.getGraphPanel(), "Введите индекс почтового отделения отправителя (10000 - 99999): ", "10000");
        if (iA == null) {
            return null;
        }
        try {
            indexA = Integer.valueOf(iA);
            if ((indexA < 10000) || (indexA > 99999)) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(frame.getGraphPanel(), e1, "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Integer indexB;
        String iB = JOptionPane.showInputDialog(frame.getGraphPanel(), "Введите индекс почтового отделения получателя (10000 - 99999): ", "90000");
        if (iB == null) {
            return null;
        }
        try {
            indexB = Integer.valueOf(iB);
            if ((indexB < 10000) || (indexB > 99999)) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(frame.getGraphPanel(), e1, "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        PostOffice postOfficeA = postservice.findClosestPostOffice(indexA);
        PostOffice postOfficeB = postservice.findClosestPostOffice(indexB);

        if (postOfficeA == postOfficeB) {
            JOptionPane.showMessageDialog(frame.getGraphPanel(), "Адреса отправителя и получателя совпадают", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        FullName nameA = BigGenerator.generateFullName();
        FullName nameB = BigGenerator.generateFullName();

        Person personA = new PersonImpl(nameA, postOfficeA.getAddress());
        Person personB = new PersonImpl(nameB, postOfficeB.getAddress());

        Integer mass = 5;
        String m = JOptionPane.showInputDialog(frame.getGraphPanel(), "Введите массу посылки (1 - 100): ", "3");
        if (m == null) {
            return null;
        }
        try {
            mass = Integer.valueOf(m);
        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(frame.getGraphPanel(), e1, "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
        }

        return new PackageImpl(personA, personB, Package.Type.T_10, mass);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Checks package current position and shows message box
     */
    public void checkPackage() {
        Package pak = frame.getListPackages().getSelectedValue();
        if (pak == null) return;
        PostOffice po = postservice.getPackageCurrentPosition(pak.getPackageId());

        if (frame.getCheckBoxShowMessages().isSelected()) {
            JOptionPane.showConfirmDialog(frame.getGraphPanel(), packageInfo(pak, po),
                    "Информация о посылке "+ pak.getPackageId(), JOptionPane.PLAIN_MESSAGE);
        }

        frame.getGraphPanel().setLastKnownPostOffice(po);
        frame.getGraphPanel().repaint();

        if (postservice.isPackageDelivered(pak.getPackageId())) {
            int answer = JOptionPane.showConfirmDialog(frame.getGraphPanel(),
                    "Посылка " + pak.getPackageId() + " доставлена. Удалить её из списка?",
                    "Вопрос", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                frame.getModelPackages().removeElement(pak);
                frame.getButtonCheckPackage().setText("Проверить");
                frame.getGraphPanel().setAll(null, null, null);
            }
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////


    public void deleteReceived() {
        for (int i = 0; i < frame.getModelPackages().size(); i++) {
            Package p = frame.getModelPackages().get(i);
            if (postservice.isPackageDelivered(p.getPackageId())) {
                frame.getModelPackages().removeElement(p);
                i = -1;
            }
        }
        frame.getButtonCheckPackage().setText("Проверить");
        frame.getGraphPanel().setAll(null, null, null);
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


    public void addEdge() {
        try {
            Integer indexA = Integer.valueOf(JOptionPane.showInputDialog(frame.getGraphPanel(), "Введите индекс почтового отделения (10000 - 99999): ", "Откуда двигаться"));
            Integer indexB = Integer.valueOf(JOptionPane.showInputDialog(frame.getGraphPanel(), "Введите индекс почтового отделения (10000 - 99999): ", "Куда двигаться"));

            PostOffice postOfficeA = DataStorage.getByPostCode(indexA);
            PostOffice postOfficeB = DataStorage.getByPostCode(indexB);

            if (postOfficeA == null || postOfficeB == null) {
                throw new NullPointerException();
            }

            double distance = postOfficeA.getGeolocation().distance(postOfficeB.getGeolocation());
            DeliveryTransport.Type type = DeliveryTransport.Type.LAND;
            double maxRange = (frame.getGraphPanel().getHeight() + frame.getGraphPanel().getWidth()) / 2;
            if (distance > maxRange / 3) {
                type = DeliveryTransport.Type.AIR;
            }
            if (distance > maxRange / 1.3) {
                type = DeliveryTransport.Type.SEA;
            }

            DeliveryTransportImproved dt = new DeliveryTransportImpl(postOfficeA, postOfficeB, type);
            DataStorage.getDeliveryTransports().add(dt);
            frame.getGraphPanel().repaint();
        } catch (NumberFormatException | NullPointerException e1) {
            JOptionPane.showMessageDialog(frame.getGraphPanel(), e1.toString(), "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////


    public void addNode() {
        Point p = frame.getGraphPanel().getGraphPoint();
        Address address = new AddressImpl(BigGenerator.generateIndex(p, frame.getGraphPanel().getSize()), BigGenerator.generateStreet(), BigGenerator.generateCityName(), BigGenerator.countryName);
        PostOffice npo = new PostOfficeImpl(address, p, BigGenerator.generatePackageTypes());
        DataStorage.getPostOffices().add(npo);
        frame.getGraphPanel().repaint();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////

}
