package com.logistic.impl.model.transport;

import com.logistic.api.model.transport.Transit;

/**
 * Created by SnakE on 12.11.2015.
 */
public interface TransitImproved extends Transit {
    int getTime();
    double getOverallRange();
}
