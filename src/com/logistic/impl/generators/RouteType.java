package com.logistic.impl.generators;


/**
 * Created by snake on 07.11.15.
 */
public enum RouteType {
    LAND(11, 3, 0, 180), AIR(51, 49, 350, 600), SEA(51, 49, 500, 600);

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



