package com.logistic.impl.service.generators;


/**
 * Created by snake on 07.11.15.
 */
public enum RouteType {
    ROAD(11, 3, 0, 180), AIR(51, 48, 350, 600);

    int randomValue;
    int routesDestiny;
    double minRouteLength;
    double maxRouteLength;

    RouteType(int randomValue, int routesDestiny, double minRouteLength, double maxRouteLength) {
        this.randomValue = randomValue;
        this.routesDestiny = routesDestiny;
        this.minRouteLength = minRouteLength;
        this.maxRouteLength = maxRouteLength;
    }

    public int getRandomValue() {
        return randomValue;
    }

    public int getRoutesDestiny() {
        return routesDestiny;
    }

    public double getMinRouteLength() {
        return minRouteLength;
    }

    public double getMaxRouteLength() {
        return maxRouteLength;
    }
}



