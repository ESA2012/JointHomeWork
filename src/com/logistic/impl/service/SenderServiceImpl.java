package com.logistic.impl.service;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.post.*;
import com.logistic.api.model.post.Package;
import com.logistic.api.model.transport.DeliveryTransport;
import com.logistic.api.model.transport.Transit;
import com.logistic.impl.model.post.PackageImproved;
import com.logistic.impl.model.transport.DeliveryTransportImproved;
import com.logistic.impl.model.transport.TransitImpl;
import com.logistic.impl.service.exceptions.NullPackageException;
import com.logistic.impl.service.exceptions.NullTransitException;
import java.util.*;
import java.util.List;

import static java.lang.Thread.sleep;


/**
 * Created by SnakE on 04.11.2015.
 */
public class SenderServiceImpl implements SenderServiceImproved {


    /**
     * Use <b>getAllOfficesImpr</b>
     * @return
     */
    @Override
    public List<PostOffice> getAllOffices() {
        return DataStorage.getPostOffices();
    }



    /**
     * Search closest Post office
     * @param address    address to search closest post office
     * @return  closest post office
     */
    @Override
    public PostOffice findClosestPostOffice(Address address) {
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



    /**
     * Adds transit to set of transits
     * @param transit       Transit
     * @param transitSet    Set of Transits
     */
    private void addTransitToSet(Transit transit, Set<Transit> transitSet) {
        if (transit!=null) {
            transitSet.add(transit);
        }
    }



    /**
     * Returns all possible transits for package
     * @param parcel    package
     * @return          list of transits
     */
    @Override
    public List<Transit> calculatePossibleTransits(Package parcel) throws NullPackageException{
        if (parcel == null) throw new NullPackageException();
        Set<Transit> transitSet = new LinkedHashSet<Transit>();
        addTransitToSet(getTransit(parcel, new DeliveryTransport.Type[] {DeliveryTransport.Type.LAND}), transitSet);
        addTransitToSet(getTransit(parcel, new DeliveryTransport.Type[] {DeliveryTransport.Type.LAND, DeliveryTransport.Type.AIR}), transitSet);
        addTransitToSet(getTransit(parcel, new DeliveryTransport.Type[] {DeliveryTransport.Type.LAND, DeliveryTransport.Type.SEA}), transitSet);
        addTransitToSet(getTransit(parcel, new DeliveryTransport.Type[] {DeliveryTransport.Type.LAND, DeliveryTransport.Type.AIR, DeliveryTransport.Type.SEA}), transitSet);

        List<Transit> transits = new ArrayList<>();
        transits.addAll(transitSet);

        return transits;
    }


    private Transit getTransit (Package parcel, DeliveryTransport.Type[] allowedTypes) {
        List<DeliveryTransport.Type> aTypes = new ArrayList<>(Arrays.asList(allowedTypes));

        PostOffice send = findOfficeByAddress(((PackageImproved) parcel).getReceiverPostOfficeAddreess());
        PostOffice dest = findOfficeByAddress(((PackageImproved) parcel).getSenderPostOfficeAddress());

        List<PostOffice> closed = new ArrayList<>();
        List<DeliveryTransportImproved> edges = new ArrayList<>();
        Deque<PostOffice> opened = new ArrayDeque<>();

        closed.add(send);
        opened.add(send);
        while (opened.peek() != null) {
            PostOffice a = opened.pollFirst();
            for (DeliveryTransportImproved det: DataStorage.getDeliveryTransports()) {
                if (det.getStartPostOffice() == a) {
                    PostOffice b = det.getDestinationPostOffice();
                    if (aTypes.contains(det.getType())) {
                        if (b.getAcceptablePackageTypes().contains(parcel.getType())) {
                            if (!closed.contains(b)) {
                                closed.add(b);
                                edges.add(det);
                                opened.add(b);
                            }
                        }
                    }
                }
            }
        }

        List<PostOffice> temp = new ArrayList<>();

        if (closed.contains(dest)) {
            for (PostOffice x = dest; x != send; x = findTDSendByDest(x, edges)) {
                temp.add(0, x);
            }
            temp.add(0, send);
        }

        double range = 0;
        double price = 0;
        int time = 0;
        for (int i = 0; i < temp.size() - 1; i++) {
            DeliveryTransportImproved dt = findDeliveryTransports(temp.get(i), temp.get(i+1));
            range += dt.getRange();
            price += dt.getPrice();
            time += dt.getTime();
        }

        Collections.reverse(temp);

        if (temp.size() < 2) {
            return null;
        } else {
            return new TransitImpl(temp, price, range, time);
        }
    }

    private PostOffice findTDSendByDest(PostOffice p, List<DeliveryTransportImproved> edges) {
        PostOffice res = null;
        for(DeliveryTransportImproved t: edges) {
            if (t.getDestinationPostOffice() == p) {
                res = t.getStartPostOffice();
            }
        }
        return res;
    }


    private DeliveryTransportImproved findDeliveryTransports(PostOffice a, PostOffice b) {
        List<DeliveryTransportImproved> edges = DataStorage.getDeliveryTransports();
        DeliveryTransportImproved res = null;
        for (DeliveryTransportImproved d: edges) {
            PostOffice da = d.getStartPostOffice();
            PostOffice db = d.getDestinationPostOffice();
            if (a.equals(da) && b.equals(db)) {
                res = d;
                break;
            }
        }
        return res;
    }



    @Override
    public boolean sendPackage(Package parcel, Transit transit) throws NullPackageException, NullTransitException {
        if (parcel == null) throw new NullPackageException();
        if (transit == null) throw new NullTransitException();

        DataStorage.saveParcelTransit(parcel, transit);
        List<PostOffice> posts = transit.getTransitOffices();

        System.out.println(transit);

        Thread thread = new Thread(new Runnable() {
            boolean result = false;
            @Override
            public void run() {
                for (int i = 0; i < posts.size(); i++) {

                    boolean canSend = true;

                    PostOffice post = posts.get(i);

                    System.out.println(posts.get(i).getCode());

                    try {
                        canSend = post.sendPackage(parcel);
                    } catch (NullPackageException e) {
                        e.printStackTrace();
                    }

                    // Calculating time to go from post office 1 to post office 2
                    int time = 0;
                    if (i < posts.size() - 1) {
                        PostOffice nextPost = posts.get(i + 1);
                        DeliveryTransportImproved dti = findDeliveryTransports(post, nextPost);
                        time = dti.getTime();
                    }
                    try {
                        sleep(time);
                    } catch (InterruptedException e) {
                        result = false;
                        break;
                    }

                    boolean received = post.receivePackage(parcel);

                    result = received && !canSend;
                }
                if (result) {
                    System.out.println(parcel.getPackageId() + " is received!");
                } else {
                    System.out.println(parcel.getPackageId() + " is lost!");
                }
            }
        });
        thread.start();
        return true;
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
        return findOfficeByAddress(lastAddress);
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
                break;
            } else {
                res = null;
            }
        }
        return res;
    }

}
