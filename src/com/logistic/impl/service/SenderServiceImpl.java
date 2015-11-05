package com.logistic.impl.service;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.post.*;
import com.logistic.api.model.post.Package;
import com.logistic.api.model.transport.Transit;
import com.logistic.api.service.SenderService;
import com.logistic.impl.model.transport.TransitImpl;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SnakE on 04.11.2015.
 */
public class SenderServiceImpl implements SenderService {

    @Override
    public List<PostOffice> getAllOffices() {
        return DataStorage.getPostOffices();
    }


    private static double distance(Point a, Point b) {
        return a.distance(b);
    }


    /**
     * Search closest Post office
     * @param address    address to search closest post office
     * @return  closest post office
     */
    private static PostOffice findClosestPostOffice(Address address) {
        int personIndx = address.getCode();
        int min = Integer.MAX_VALUE;
        PostOffice post = null;
        for (PostOffice p: DataStorage.getPostOffices()) {
            int postIndx = p.getCode();
            int close = Math.abs(personIndx - postIndx);
            if (close < min) {
                min = close;
                post = p;
            }
        }
        return post;
    }



    @Override
    public List<Transit> calculatePossibleTransits(Package parcel) {
        // TODO: raise exception if parcel is null
        PostOffice send = findClosestPostOffice(parcel.getSenderAddress());
        PostOffice dest = findClosestPostOffice(parcel.getReceiverAddress());

        // Selecting post offices only with allowed package type
        Package.Type packType = parcel.getType();
        List<PostOffice> allowableOffices = new ArrayList<PostOffice>();
        for (PostOffice p : DataStorage.getPostOffices()) {
            if (packType == p.getAcceptableTypes()) {
                allowableOffices.add(p);
            }
        }

        List<Transit> transits = new ArrayList<Transit>();

        Transit transit = new TransitImpl();
        transit.getTransitOffices().add(0, send);

        for (PostOffice p: allowableOffices) {
            if (!transit.getTransitOffices().contains(p)) {
                //TODO поиск путей
            }
        }

        transits.add(transit);
        transit.getTransitOffices().add(1, dest);

        return transits;
    }


    @Override
    public boolean sendPackage(Package parcel, Transit transit) {
        if (parcel == null || transit == null) return false;

        return false;
    }

    @Override
    public PostOffice getPackageCurrentPosition(String id) {
        if (id.length() != 10) return null;
        // Search package by ID
        Package pack = null;
        List<Package> packages = DataStorage.getPackages();
        for(Package p: packages) {
            if(p.getPackageId().equals(id)) {
                pack = p;
            }
        }
        // Get last pakage address
        List<Stamp> stamps = pack.getStamps();
        Address lastAddress = stamps.get(stamps.size() - 1).getPostOfficeAddress();
        // Search Post Office by Address
        PostOffice lastPO = findOfficeByAddress(lastAddress);
        return lastPO;
    }


    @Override
    public double getMilesToDestination(String id) {
        return 0;
    }

    /**
     * Search post office in storage by address
     * @param address   address of post office
     * @return post office or null
     */
    private static PostOffice findOfficeByAddress(Address address) {
        PostOffice res = null;
        for (PostOffice po: DataStorage.getPostOffices()) {
            if (po.getAddress().equals(address)) {
                res = po;
            } else {
                res = null;
            }
        }
        return res;
    }

}
