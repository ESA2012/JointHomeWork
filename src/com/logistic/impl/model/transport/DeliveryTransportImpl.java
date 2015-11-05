package com.logistic.impl.model.transport;

import com.logistic.api.model.post.PostOffice;
import com.logistic.api.model.transport.DeliveryTransport;


public class DeliveryTransportImpl implements DeliveryTransport {

    private PostOffice startPostOffice;
    private PostOffice destinationPostOffice;
    private Type type;


    public DeliveryTransportImpl(PostOffice startPostOffice, PostOffice destinationPostOffice, Type type) {
        this.startPostOffice = startPostOffice;
        this.destinationPostOffice = destinationPostOffice;
        this.type = type;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public PostOffice getStartPostOffice() {
        return startPostOffice;
    }

    @Override
    public PostOffice getDestinationPostOffice() {
        return destinationPostOffice;
    }
}
