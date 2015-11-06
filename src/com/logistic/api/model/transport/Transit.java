package com.logistic.api.model.transport;

import com.logistic.api.model.post.PostOffice;

import java.util.List;

/**
 * Created by Denis on 5/25/2015.
 */
public interface Transit {
    List<PostOffice> getTransitOffices();
    double getPrice();
}
