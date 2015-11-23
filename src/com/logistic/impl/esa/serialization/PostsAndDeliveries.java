package com.logistic.impl.esa.serialization;

import com.logistic.api.model.post.PostOffice;
import com.logistic.impl.model.transport.DeliveryTransportImproved;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SnakE on 22.11.2015.
 */
public class PostsAndDeliveries implements Serializable {
    private List<PostOffice> postOffices;
    private List<DeliveryTransportImproved> delivries;

    public PostsAndDeliveries(List<PostOffice> postOffices, List<DeliveryTransportImproved> deliveries) {
        this.postOffices = postOffices;
        this.delivries = deliveries;
    }

    public List<PostOffice> getPostOffices() {
        return postOffices;
    }

    public List<DeliveryTransportImproved> getDelivries() {
        return delivries;
    }
}
