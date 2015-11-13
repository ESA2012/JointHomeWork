package com.logistic.impl.model.transport;

import com.logistic.api.model.transport.DeliveryTransport;

/**
 * Created by SnakE on 12.11.2015.
 */
public interface DeliveryTransportImproved extends DeliveryTransport {
    double getPrice();
    int getTime();
    double getRange();
}
