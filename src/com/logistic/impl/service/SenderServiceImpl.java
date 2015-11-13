package com.logistic.impl.service;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.post.*;
import com.logistic.api.model.post.Package;
import com.logistic.api.model.transport.DeliveryTransport;
import com.logistic.api.model.transport.Transit;
import com.logistic.impl.model.post.PostOfficeImproved;
import com.logistic.impl.model.transport.DeliveryTransportImproved;
import com.logistic.impl.model.transport.TransitImpl;
import com.logistic.impl.model.transport.TransitImproved;
import com.logistic.impl.service.esa.routes.PathFinder;
import com.logistic.impl.service.esa.routes.RouteMatrix;
import sun.awt.image.ImageWatched;

import java.util.*;
import java.util.List;


/**
 * Created by SnakE on 04.11.2015.
 */
public class SenderServiceImpl implements SenderServiceImproved {


    /**
     * Use <b>getAllOfficesImpr</b>
     * @return
     */
    @Deprecated
    public List<PostOffice> getAllOffices() {
        return null;
    }

    @Deprecated
    @Override
    public List<TransitImproved> calculatePossibleTransits(Package parcel) {
        return calculatePossibleTransitsImproved(parcel);
    }


    /**
     * Returns post offices list
     * @return list of post offices
     */
    @Override
    public List<PostOfficeImproved> getAllOfficesImpr() {
        return DataStorage.getPostOffices();
    }



    /**
     * Search closest Post office
     * @param address    address to search closest post office
     * @return  closest post office
     */
    @Override
    public PostOfficeImproved findClosestPostOffice(Address address) {
        int personIndx = address.getCode();
        int min = Integer.MAX_VALUE;
        PostOfficeImproved post = null;
        for (PostOfficeImproved p: DataStorage.getPostOffices()) {
            int postIndx = p.getCode();
            int close = Math.abs(personIndx - postIndx);
            if (close < min) {
                min = close;
                post = p;
            }
        }
        return post;
    }



    /**
     * Returns all possible transits for package
     * @param parcel    package
     * @return          list of transits
     */
    @Override
    public List<TransitImproved> calculatePossibleTransitsImproved(Package parcel) {
        // TODO: raise exception if parcel is null
        Set<TransitImproved> transitSet = new LinkedHashSet<TransitImproved>();
        addTransitToSet(getTransit(parcel, new DeliveryTransport.Type[] {DeliveryTransport.Type.LAND}), transitSet);
        addTransitToSet(getTransit(parcel, new DeliveryTransport.Type[] {DeliveryTransport.Type.LAND, DeliveryTransport.Type.AIR}), transitSet);
        addTransitToSet(getTransit(parcel, new DeliveryTransport.Type[] {DeliveryTransport.Type.LAND, DeliveryTransport.Type.SEA}), transitSet);
        addTransitToSet(getTransit(parcel, new DeliveryTransport.Type[] {DeliveryTransport.Type.LAND, DeliveryTransport.Type.AIR, DeliveryTransport.Type.SEA}), transitSet);

        List<TransitImproved> transits = new ArrayList<TransitImproved>();
        transits.addAll(transitSet);

//        path(parcel, new DeliveryTransport.Type[] {DeliveryTransport.Type.LAND});


        return transits;
    }


//
//    private Transit path(Package parcel, DeliveryTransport.Type[] allowedTypes) {
//        List<PostOfficeImproved> posts = new ArrayList<>();
//
//        PostOfficeImproved send = findClosestPostOffice(parcel.getSenderAddress());
//        PostOfficeImproved dest = findClosestPostOffice(parcel.getReceiverAddress());
//
//
//        List<DeliveryTransportImproved> edges = DataStorage.getDeliveryTransports();
//        List<DeliveryTransportImproved> path = new ArrayList<DeliveryTransportImproved>();
//
//        DeliveryTransportImproved last = findDeliveryTransports(send);
//        int l = DataStorage.getDeliveryTransports().indexOf(last);
//        int f = DataStorage.getDeliveryTransports().indexOf(findDeliveryTransports(dest));
//
//        TransitImproved transit = new TransitImpl(posts);
//        return transit;
//    }


    private DeliveryTransportImproved findDeliveryTransports(PostOffice a, PostOffice b) {
        List<DeliveryTransportImproved> edges = DataStorage.getDeliveryTransports();
        DeliveryTransportImproved res = null;
        for (DeliveryTransportImproved d: edges) {
            PostOffice da = d.getStartPostOffice();
            PostOffice db = d.getDestinationPostOffice();
            if (((a == da) && (b == db)) || ((a == db) && (b == da))) {
                res = d;
                break;
            }
        }
        return res;
    }

    private DeliveryTransportImproved findDeliveryTransports(PostOffice a) {
        List<DeliveryTransportImproved> edges = DataStorage.getDeliveryTransports();
        DeliveryTransportImproved res = null;
        for (DeliveryTransportImproved d: edges) {
            PostOffice da = d.getStartPostOffice();
            PostOffice db = d.getDestinationPostOffice();
            if ((a == da) || (a == db)) {
                res = d;
                break;
            }
        }
        return res;
    }


    /**
     * Calculates transit for package with given route matrix
     * @param parcel        package
     * @return              calculated transit
     */
    private TransitImproved getTransit(Package parcel, DeliveryTransport.Type[] allowedTypes) {
        int[] types = new int[allowedTypes.length];
        for (int i = 0; i < types.length; i++) {
            int t = 0;
            switch (allowedTypes[i]) {
                case LAND: t = 1;
                    break;
                case AIR: t = 2;
                    break;
                case SEA: t = 3;
                    break;
            }
            types[i] = t;
        }

        PostOffice send = findClosestPostOffice(parcel.getSenderAddress());
        PostOffice dest = findClosestPostOffice(parcel.getReceiverAddress());
        // Indexes of start and destination nodes
        int startNode = DataStorage.getPostOffices().indexOf(send);
        int finishNode = DataStorage.getPostOffices().indexOf(dest);

        PathFinder pfAll = new PathFinder(startNode, parcel, types);
        TransitImproved transit = new TransitImpl(new ArrayList<PostOfficeImproved>());
        if (pfAll.hasWayTo(finishNode)) {
            for (int x : pfAll.findPath(finishNode)) {
                transit.getTransitOffices().add(getPO(x));
            }
        }

        return transit;
    }



    /**
     * Adds transit to set of transits
     * @param transit       Transit
     * @param transitSet    Set of Transits
     */
    private void addTransitToSet(TransitImproved transit, Set<TransitImproved> transitSet) {
        if (transit!=null) {
            transitSet.add(transit);
        }
    }


    /**
     * Returns Post office by index
     * @param i     index
     * @return      Post office
     */
    private PostOffice getPO(int i) {
        return DataStorage.getPostOffices().get(i);
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
        //TODO : realise method
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
