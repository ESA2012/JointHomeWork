package com.logistic.impl.model.transport;

import com.logistic.api.model.post.PostOffice;
import com.logistic.api.model.transport.Transit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SnakE on 04.11.2015.
 */
public class TransitImpl implements Transit {
    private List<PostOffice> route;
    private double price;

    public TransitImpl() {
        route = new ArrayList<PostOffice>();
        price = 0d;
    }

    @Override
    public List<PostOffice> getTransitOffices() {
        return route;
    }

    @Override
    public double getPrice() {
        return price;
    }
}
