package com.logistic.impl.service;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.post.*;
import com.logistic.api.model.post.Package;
import com.logistic.api.model.transport.DeliveryTransport;
import com.logistic.api.model.transport.Transit;
import com.logistic.impl.model.post.PackageImproved;
import com.logistic.impl.model.transport.DeliveryTransportImproved;
import com.logistic.impl.model.transport.TransitImpl;
import com.logistic.impl.exceptions.NullPackageException;
import com.logistic.impl.exceptions.NullTransitException;
import java.util.*;
import java.util.List;

import static java.lang.Thread.interrupted;
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
     * Returns all possible transits for given package
     * @param parcel    package
     * @return          list of transits
     */
    @Override
    public List<Transit> calculatePossibleTransits(Package parcel) throws NullPackageException{
        if (parcel == null) throw new NullPackageException();
        Set<Transit> transitSet = new LinkedHashSet<>();
        addTransitToSet(getTransit(parcel, new DeliveryTransport.Type[] {DeliveryTransport.Type.LAND}), transitSet);
        addTransitToSet(getTransit(parcel, new DeliveryTransport.Type[] {DeliveryTransport.Type.LAND, DeliveryTransport.Type.AIR}), transitSet);
        addTransitToSet(getTransit(parcel, new DeliveryTransport.Type[] {DeliveryTransport.Type.LAND, DeliveryTransport.Type.SEA}), transitSet);
        addTransitToSet(getTransit(parcel, new DeliveryTransport.Type[] {DeliveryTransport.Type.LAND, DeliveryTransport.Type.AIR, DeliveryTransport.Type.SEA}), transitSet);

        List<Transit> transits = new ArrayList<>();
        transits.addAll(transitSet);

        return transits;
    }


    /**
     * Calculates all possible routes from post office to all others post offices.
     * Recovers one route.
     *
     * З.Ы. Поиск всех возможных путей можно перенести в генераторы и сохранить в сторедже
     * все пути для всех почтовых отделений. Здесь останется только блок Recover transit
     *
     * @param parcel        package
     * @param allowedTypes  allowed types of routes
     * @return              new transit instance
     */
    private Transit getTransit (Package parcel, DeliveryTransport.Type[] allowedTypes) {
        List<DeliveryTransport.Type> aTypes = new ArrayList<>(Arrays.asList(allowedTypes));

        PostOffice send = findOfficeByAddress(((PackageImproved) parcel).getReceiverPostOfficeAddreess());
        PostOffice dest = findOfficeByAddress(((PackageImproved) parcel).getSenderPostOfficeAddress());

        // Dynamical calculation of all available routes between sender post office and receiver post office
        List<PostOffice> closed = new ArrayList<>();                // visited nodes
        List<DeliveryTransportImproved> edges = new ArrayList<>();  // edges from current node
        Deque<PostOffice> opened = new ArrayDeque<>();              // current node queue

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

        // Recover transit
        List<PostOffice> temp = new ArrayList<>();
        if (closed.contains(dest)) {
            for (PostOffice x = dest; x != send; x = findTDSendByDest(x, edges)) {
                temp.add(0, x);
            }
            temp.add(0, send);
        }
        // Calculate overall range, price and time
        double range = 0;
        double price = 0;
        int time = 0;
        for (int i = 0; i < temp.size() - 1; i++) {
            DeliveryTransportImproved dt = findDeliveryTransports(temp.get(i), temp.get(i+1));
            range += dt.getRange();
            price += dt.getPrice();
            time += dt.getTime();
        }

        // Build result transit
        Collections.reverse(temp);
        if (temp.size() < 2) {
            return null;
        } else {
            return new TransitImpl(temp, price, range, time);
        }
    }



    /**
     * Search firts post office by second post office and delivery transports collection
     * @param p         second post office
     * @param edges     delivery transport collection
     * @return          first post office
     */
    private PostOffice findTDSendByDest(PostOffice p, List<DeliveryTransportImproved> edges) {
        PostOffice res = null;
        for(DeliveryTransportImproved t: edges) {
            if (t.getDestinationPostOffice() == p) {
                res = t.getStartPostOffice();
            }
        }
        return res;
    }



    /**
     * Search for delivery transport between two post offices
     * @param a    first post office
     * @param b    second post office
     * @return     delivery transport (or null if there are no such delivery transport)
     */
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




    private class PackageSender implements Runnable {
        private Package parcel;
        private List<PostOffice> posts;

        PackageSender(Package parcel, Transit transit) {
            this.parcel = parcel;
            this.posts = transit.getTransitOffices();
        }

        private boolean result = false;

        public boolean getResult() {
            return result;
        }

        @Override
        public void run() {
            for (int i = 0; i < posts.size(); i++) {

                PostOffice post = posts.get(i);
                boolean received = post.receivePackage(parcel);

                // Calculating time to go from post office 1 to post office 2
                int time = 0;
                if (i < posts.size() - 1) {
                    try {
                        PostOffice nextPost = posts.get(i + 1);
                        DeliveryTransportImproved dti = findDeliveryTransports(post, nextPost);
                        time = dti.getTime();
                    } catch (NullPointerException e) {
                        result = false;
                        interrupted();
                    }
                }
                try {
                    sleep(time);
                } catch (InterruptedException e) {
                    result = false;
                    break;
                }

                boolean canSend = true;

                try {
                    canSend = post.sendPackage(parcel);
                } catch (NullPackageException e) {
                    e.printStackTrace();
                }

                result = received && !canSend;
            }
        }
    }



    /**
     * Sends specified package for a given route (transit)
     * @param parcel     package
     * @param transit    route (transit)
     * @return  <b>true</b> if package has reached destination post office
     *          <b>false</b> if package has not reached destination post office
     * @throws NullPackageException     if package is null
     * @throws NullTransitException     if transit is null
     */
    @Override
    public boolean sendPackage(Package parcel, Transit transit) throws NullPackageException, NullTransitException {
        if (parcel == null) throw new NullPackageException();
        if (transit == null) throw new NullTransitException();

        DataStorage.saveParcelTransit(parcel, transit);

        Thread sender = new Thread(new PackageSender(parcel, transit));
        sender.start();
        return true;
    }



    /**
     * Returns last known post office that has package received
     * @param id    package id
     * @return      PostOffice object or null
     */
    @Override
    public PostOffice getPackageCurrentPosition(String id) {
        List<Stamp> stamps = DataStorage.getPackage(id).getStamps();
        if (stamps == null) {
                return null;
        }

        if (stamps.size() < 1) {
            return DataStorage.getTransit(id).getTransitOffices().get(0);
        }

        int lastStampIndx = stamps.size()-1;
        Address lastAddress = stamps.get(lastStampIndx).getPostOfficeAddress();
        return findOfficeByAddress(lastAddress);
    }



    /**
     * Calculates distance from last known package position to destination post office
     * @param id    package id
     * @return      distance to destination post office or Double.MAX_VALUE (if distance is not computable)
     */
    @Override
    public double getMilesToDestination(String id) {
        PostOffice lastPostOffice = getPackageCurrentPosition(id);
        Transit packageTransit = DataStorage.getTransit(id);

        if (lastPostOffice == null || packageTransit == null) {
            return Double.MAX_VALUE;
        }

        List<PostOffice> transitOfficies = packageTransit.getTransitOffices();
        double overallRange = packageTransit.getOverallRange();
        int z = transitOfficies.indexOf(lastPostOffice);
        double completedRange = 0;
        for (int i = 0; i < z; i++) {
            DeliveryTransportImproved dt = findDeliveryTransports(transitOfficies.get(i), transitOfficies.get(i+1));
            completedRange +=dt.getRange();
        }
        return overallRange - completedRange;
    }



    /**
     * Search post office in storage by address
     * @param address   address of post office
     * @return          PostOffice object or null (if search is failed)
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
