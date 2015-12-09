package com.logistic.impl.esa.generators;

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



    private static double getMaxRange(List<PostOffice> postOffices) {
        double max = 0;
        for (PostOffice p1 : postOffices) {
            for (PostOffice p2 : postOffices) {
                double range = p1.getGeolocation().distance(p2.getGeolocation());
                if (range > max) {
                    max = range;
                }
            }
        }
        return max;
    }




    /**
     * Build random adjacency matrix for post offices
     * @param postOffices   post offices list
     * @return              builded matrix
     */
    public static RouteMatrix buildRandomMatrix(List<PostOffice> postOffices) {
        int nodes = postOffices.size();

        RouteMatrix matrix = new RouteMatrix(nodes);

        Random rnd = new Random();
        double max = getMaxRange(postOffices);

        for (int i = 0; i < nodes; i++) {
            for (int j = 0; j < i + 1; j++) {

                Point p1 = postOffices.get(i).getGeolocation();
                Point p2 = postOffices.get(j).getGeolocation();
                double dist = p1.distance(p2);

                int n = rnd.nextInt(100);
                int val = 0;

                if (n > 30 && dist < max / 5) {
                    val = 1;
                }

                if (n > 97 && dist > max / 3) {
                    val = 2;
                }

                if (n > 97 && dist > max / 1.3) {
                    val = 3;
                }

                matrix.getArray()[i][j] = val;
                matrix.getArray()[j][i] = val;
            }
        }
        return matrix;
    }


    /**
     * Converts route matrix to collection of delivery transports
     * @param matrix    route matrix
     * @param posts     post offices
     * @return          a collection of delivery transport
     */
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
                    if (t == null) {
                        t = DeliveryTransport.Type.LAND;
                    }
                    dts.add(new DeliveryTransportImpl(o1, o2, t));
                }
            }
        }
        return dts;
    }



    /**
     * Builds route matrix for given post offices from given list of delivery transport
     * @param deliveryTransports    delivery transports
     * @param posts                 post offices
     * @return                      route matrix
     */
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
