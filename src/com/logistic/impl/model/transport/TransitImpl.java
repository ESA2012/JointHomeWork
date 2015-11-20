package com.logistic.impl.model.transport;

import com.logistic.api.model.post.PostOffice;
import com.logistic.api.model.transport.Transit;
import java.util.List;



public class TransitImpl implements Transit {
    private final List<PostOffice> route;
    private final double cost;
    private final double range;
    private final int time;


    public TransitImpl(List<PostOffice> postOfficesList, double cost, double range, int time) {
        route = postOfficesList;
        this.cost = cost;
        this.range = range;
        this.time = time;
    }


    @Override
    public List<PostOffice> getTransitOffices() {
        return route;
    }

    @Override
    public double getPrice() {
        return cost;
    }

    @Override
    public int getTime() {
        return time;
    }

    @Override
    public double getOverallRange() {
        return range;
    }

    @Override
    public String toString() {
        String res = "";
        for (PostOffice p : route) {
            res += "->"+p.getAddress().getCode();
        }
        return res;
    }


    @Override
    public boolean equals(Object obj) {
        if ((obj instanceof Transit)) {
            return this.toString().equals(obj.toString());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int prime = 33;
        return prime + this.toString().hashCode();
    }

}
