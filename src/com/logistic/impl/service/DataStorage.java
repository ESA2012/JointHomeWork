package com.logistic.impl.service;

import com.logistic.api.model.post.Package;
import com.logistic.api.model.post.PostOffice;
import com.logistic.api.model.transport.Transit;
import com.logistic.api.service.Storage;
import com.logistic.impl.model.transport.DeliveryTransportImproved;
import com.logistic.impl.generators.BigGenerator;
import com.logistic.impl.generators.RouteGenerator;
import com.logistic.impl.generators.RouteType;
import com.logistic.impl.generators.RouteMatrix;

import java.awt.*;
import java.util.List;

import static com.logistic.impl.generators.RouteGenerator.buildDeliveryTransports;


/**
 * Created by SnakE on 04.11.2015.
 */
public class DataStorage {
    public static final String POST_OFFICES_KEY = "postOffices";
    public static final String DELIVERY_TRANSPORTS_KEY = "deliveryTransports";
    public static final String PACKAGES_KEY = "packages";
    public static final String TRANSIT_PREFIX = "_transit";
    public static final String AVAILABLE_TRANSITS_PREFIX = "_transits";


    public static void initializeByRandomData(Dimension area, int postOfficesCount, int minDistance) {

        Storage.getInstance().putToStorage(POST_OFFICES_KEY, BigGenerator.generatePostOffices(BigGenerator.countryName, postOfficesCount, area, minDistance));

        RouteMatrix matrixRoad = RouteGenerator.buildRandomMatrix(DataStorage.getPostOffices(), RouteType.LAND);
        RouteMatrix matrixAir = RouteGenerator.buildRandomMatrix(DataStorage.getPostOffices(), RouteType.AIR);
        RouteMatrix matrixSea = RouteGenerator.buildRandomMatrix(DataStorage.getPostOffices(), RouteType.SEA);
        RouteMatrix matrixComplete = RouteMatrix.join(matrixRoad, matrixAir, matrixSea);

        Storage.getInstance().putToStorage(DELIVERY_TRANSPORTS_KEY, buildDeliveryTransports(matrixComplete, DataStorage.getPostOffices()));
    }


    public static void saveParcelTransit(Package parcel, Transit transit) {
        Storage.getInstance().putToStorage(parcel.getPackageId(), parcel);
        Storage.getInstance().putToStorage(PACKAGES_KEY, parcel);
        Storage.getInstance().putToStorage(parcel.getPackageId() + TRANSIT_PREFIX, transit);
    }


    public static Transit getTransit(String id) {
        return Storage.getInstance().<Transit>getById(id + TRANSIT_PREFIX);
    }


    public static Package getPackage(String id) {
        return Storage.getInstance().<Package>getById(id);
    }


    public static void saveAvailableTransits(Package p, List<Transit> list) {
        Storage.getInstance().putToStorage(p.getPackageId()+AVAILABLE_TRANSITS_PREFIX, list);
    }

    public static List<Transit> getAvailableTransits(String id) {
        return Storage.getInstance().<List>getById(id + AVAILABLE_TRANSITS_PREFIX);
    }

    public static List<DeliveryTransportImproved> getDeliveryTransports() {
        return Storage.getInstance().getById(DELIVERY_TRANSPORTS_KEY);
    }

    public static List<PostOffice> getPostOffices() {
        return Storage.getInstance().getById(POST_OFFICES_KEY);
    }

    public static List<Package> getPackages() {
        return Storage.getInstance().getById(PACKAGES_KEY);
    }
}
