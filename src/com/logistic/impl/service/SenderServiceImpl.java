package com.logistic.impl.service;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.post.*;
import com.logistic.api.model.post.Package;
import com.logistic.api.model.transport.Transit;
import com.logistic.api.service.SenderService;
import com.logistic.impl.model.post.PostOfficeImproved;
import com.logistic.impl.model.transport.TransitImpl;
import com.logistic.impl.service.generators.RouteType;

import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * Created by SnakE on 04.11.2015.
 */
public class SenderServiceImpl implements SenderServiceImproved {

    @Deprecated
    public List<PostOffice> getAllOffices() {
        return null;
    }

    @Override
    public List<PostOfficeImproved> getAllOfficesImr() {
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

    @Override
    public List<Transit> calculatePossibleTransits(Package parcel) {
        // TODO: raise exception if parcel is null
        PostOffice send = findClosestPostOffice(parcel.getSenderAddress());
        PostOffice dest = findClosestPostOffice(parcel.getReceiverAddress());

        List<Transit> transits = new ArrayList<Transit>();

        // Start index for route matrix
        int nodeS = DataStorage.getPostOffices().indexOf(send);
        int nodeF = DataStorage.getPostOffices().indexOf(dest);

        // опредение количества всех вершин
        int nodesCount = DataStorage.getPostOffices().size();

        // TODO : поиск пути вынести в отдельный класс PathFinder
        // TODO : добавить поиск пути с учетом воздушных и морских путей сообщений

        // Получение ссылки на матрицу смежности
        RouteMatrix matrix = DataStorage.getRoadRouteMatrix(RouteType.ROAD);
        boolean[] marked = new boolean[nodesCount];
        Deque<Integer> queue = new ArrayDeque<Integer>();

        int[] nodeLast = new int[nodesCount];

        marked[nodeS] = true;
        queue.add(nodeS);

        while (queue.peek()!=null) {
            int node1 = queue.pollFirst();
            for(int node2 = 0; node2 < nodesCount; node2++) {
                if (matrix.isConnected(node1, node2)) {
                    if (DataStorage.getPostOffices().get(node2).getAcceptablePackageTypes().contains(parcel.getType())) {
                        if (!marked[node2]) {
                            marked[node2] = true;
                            nodeLast[node2] = node1;
                            queue.add(node2);
                        }
                    }
                }
            }
        }

        Transit transit = new TransitImpl();

        for (int x = nodeF; x != nodeS; x = nodeLast[x]) {
            transit.getTransitOffices().add(0, getPO(x));
        }

        if (!marked[nodeF]) {
            System.out.println("Нет дороги!");
        } else {
            transit.getTransitOffices().add(0, getPO(nodeS));
        }

        transits.add(transit);
        return transits;
    }

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
