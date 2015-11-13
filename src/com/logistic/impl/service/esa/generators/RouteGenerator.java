package com.logistic.impl.service.esa.generators;

import com.logistic.impl.model.post.PostOfficeImproved;
import com.logistic.impl.service.esa.routes.RouteMatrix;

import java.awt.*;
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
    public static RouteMatrix buildRandomMatrix(List<PostOfficeImproved> postOffices, RouteType rt) {

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
                b = (p1.distance(p2) < distanceMin)? false:b;
                b = (p1.distance(p2) > distanceMax)? false:b;
                b = (i == j)? false : b;

                matrix.getArray()[i][j] = b? val:0;
                matrix.getArray()[j][i] = b? val:0;
            }
        }
        return matrix;
    }

}
