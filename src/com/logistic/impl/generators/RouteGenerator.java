package com.logistic.impl.generators;

import com.logistic.api.model.post.PostOffice;
import com.logistic.api.model.transport.DeliveryTransport;
import com.logistic.impl.model.transport.DeliveryTransportImpl;
import com.logistic.impl.model.transport.DeliveryTransportImproved;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by SnakE on 07.11.2015.
 */
public class RouteGenerator {


    /**
     * Build random adjacency matrix for post offices
     * @param postOffices   post offices list
     * @param rt            route type
     * @return              builded matrix
     */
    public static RouteMatrix buildRandomMatrix(List<PostOffice> postOffices, RouteType rt) {

        int density = rt.getRoutesDestiny();
        double distanceMin = rt.getMinRouteLength();
        double distanceMax = rt.getMaxRouteLength();
        int nodes = postOffices.size();

        RouteMatrix matrix = new RouteMatrix(nodes);

        int val = rt.ordinal() + 1;

        Random rnd = new Random();

        for (int i = 0; i < nodes; i++) {
            for (int j = 0; j < i + 1; j++) {
                int n = rnd.nextInt(rt.getRandomValue());
                boolean b = n > density;

                Point p1 = postOffices.get(i).getGeolocation();
                Point p2 = postOffices.get(j).getGeolocation();
                b = (p1.distance(p2) >= distanceMin) && b;
                b = (p1.distance(p2) <= distanceMax) && b;
                b = (i != j) && b;

                matrix.getArray()[i][j] = b? val:0;
                matrix.getArray()[j][i] = b? val:0;
            }
        }
        return matrix;
    }


    public static List<DeliveryTransportImproved> buildDeliveryTransports(RouteMatrix matrix, List<PostOffice> posts) {
        List<DeliveryTransportImproved> dts = new ArrayList<>();

        int[][] m = matrix.getArray();

        for (int i = 0; i < m.length; i++) {
//            for (int j = 0; j < i+1; j++) {
            for (int j = 0; j < m.length; j++) {
                if (m[i][j]!=0) {
                    PostOffice o1 = posts.get(i);
                    PostOffice o2 = posts.get(j);
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


    public static RouteMatrix buildMatrix(List<DeliveryTransportImproved> deliveryTransports, List<PostOffice> posts) {
        RouteMatrix matrix = new RouteMatrix(posts.size());

        for (DeliveryTransportImproved d: deliveryTransports) {
            int i = posts.indexOf(d.getStartPostOffice());
            int k = posts.indexOf(d.getDestinationPostOffice());
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

}
