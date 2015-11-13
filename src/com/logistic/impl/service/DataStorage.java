package com.logistic.impl.service;

import com.logistic.api.model.post.*;
import com.logistic.api.model.post.Package;
import com.logistic.api.model.transport.DeliveryTransport;
import com.logistic.api.service.Storage;
import com.logistic.impl.model.person.AddressImpl;
import com.logistic.impl.model.post.PostOfficeImpl;
import com.logistic.impl.model.post.PostOfficeImproved;
import com.logistic.impl.model.transport.DeliveryTransportImpl;
import com.logistic.impl.model.transport.DeliveryTransportImproved;
import com.logistic.impl.service.esa.generators.BigGenerator;
import com.logistic.impl.service.esa.generators.RouteGenerator;
import com.logistic.impl.service.esa.generators.RouteType;
import com.logistic.impl.service.esa.routes.RouteMatrix;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


/**
 * Created by SnakE on 04.11.2015.
 */
public class DataStorage {
    public static final int POST_OFFICES = 0;
    public static final int DELIVERY_TRANPORTS = 1;
    public static final int PACKAGES = 2;

    private static RouteMatrix matrixRoad;
    private static RouteMatrix matrixAir;
    private static RouteMatrix matrixSea;
    private static RouteMatrix matrixComplete;
    private static RouteMatrix matrix;

    // ---------- Initialise Generators constants ----------
    public static final Rectangle AREA              = new Rectangle(800, 600);
    public static final int POST_OFFICES_COUNT      = 30;   // N
    public static final int DISTANCE_BTWN_OFFICES   = 80;   // 0 - Rectangle AREA side

    // ------------------------------------------------------


    static {
        Storage.getInstance().putToStorage(POST_OFFICES,
                BigGenerator.generatePostOffices(BigGenerator.countryName, POST_OFFICES_COUNT, AREA, DISTANCE_BTWN_OFFICES));

        matrixRoad = RouteGenerator.buildRandomMatrix(DataStorage.getPostOffices(), RouteType.LAND);
        matrixAir = RouteGenerator.buildRandomMatrix(DataStorage.getPostOffices(), RouteType.AIR);
        matrixSea = RouteGenerator.buildRandomMatrix(DataStorage.getPostOffices(), RouteType.SEA);
        matrixComplete = RouteMatrix.join(matrixRoad, matrixAir, matrixSea);

        Storage.getInstance().putToStorage(DELIVERY_TRANPORTS, buildDeliveryTransports(matrixComplete));
        matrix = buildMatrix(getDeliveryTransports());
    }


    private static List<DeliveryTransportImproved> buildDeliveryTransports(RouteMatrix matrix) {
        List<DeliveryTransportImproved> dts = new ArrayList<DeliveryTransportImproved>();

        int[][] m = matrix.getArray();

        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < i+1; j++) {
                if (m[i][j]!=0) {
                    PostOffice o1 = getPostOffices().get(i);
                    PostOffice o2 = getPostOffices().get(j);
                    DeliveryTransport.Type t = null;
                    switch (m[i][j]) {
                        case 1: t = DeliveryTransport.Type.LAND;
                            break;
                        case 2: t = DeliveryTransport.Type.AIR;
                            break;
                        case 3: t = DeliveryTransport.Type.SEA;
                    }
                    dts.add(new DeliveryTransportImpl(o1, o2, t));
                }
            }
        }
        return dts;
    }


    private static RouteMatrix buildMatrix(List<DeliveryTransportImproved> deliveryTransports) {
        RouteMatrix matrix = new RouteMatrix(getPostOffices().size());

        for (DeliveryTransportImproved d: deliveryTransports) {
            int i = getPostOffices().indexOf(d.getStartPostOffice());
            int k = getPostOffices().indexOf(d.getDestinationPostOffice());
            DeliveryTransport.Type type = d.getType();
            int t = 0;
            switch (type) {
                case LAND: t = 1;
                    break;
                case AIR: t = 2;
                    break;
                case SEA: t = 3;
                    break;
            }
            matrix.getArray()[i][k] = t;
            matrix.getArray()[k][i] = t;
        }
        return matrix;
    }



    public static void loadFromFile(String fileName) throws IOException {
        ArrayList<PostOffice> offices = new ArrayList<PostOffice>();

        Scanner sc = new Scanner(new File(fileName));
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            String[] z = s.split("\\s*\\;\\s*");
            AddressImpl address = new AddressImpl(Integer.valueOf(z[0]), z[1], z[2], z[3]);
            HashSet<Package.Type> pkg = new HashSet<>();
            pkg.add(Package.Type.T_25);
            PostOfficeImpl po = new PostOfficeImpl(address, new Point(Integer.valueOf(z[5]), Integer.valueOf(z[6])), pkg);
            offices.add(po);
        }
        sc.close();
        Storage.getInstance().putToStorage(POST_OFFICES, offices);
    }



    public static RouteMatrix getRouteMatrix() {
        return matrix;
    }


    public static List<DeliveryTransportImproved> getDeliveryTransports() {
        return Storage.getInstance().getById(DELIVERY_TRANPORTS);
    }

    public static List<PostOfficeImproved> getPostOffices() {
        return Storage.getInstance().getById(POST_OFFICES);
    }

    public static List<Package> getPackages() {
        return Storage.getInstance().getById(PACKAGES);
    }
}
