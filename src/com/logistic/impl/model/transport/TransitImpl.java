package com.logistic.impl.model.transport;

import com.logistic.api.model.post.PostOffice;
import com.logistic.api.model.transport.Transit;
import com.logistic.impl.model.post.PostOfficeImproved;

import java.util.ArrayList;
import java.util.List;



public class TransitImpl implements TransitImproved {
    private final List<PostOffice> route;

    public TransitImpl(List<PostOfficeImproved> postOfficesList) {
        route = new ArrayList<>();
    }


    @Override
    public List<PostOffice> getTransitOffices() {
        return route;
    }

    @Override
    public double getPrice() {
        return 0;
    }

    @Override
    public int getTime() {
        return 0;
    }

    @Override
    public double getOverallRange() {
        return 0;
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
