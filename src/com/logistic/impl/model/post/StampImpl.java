package com.logistic.impl.model.post;

import com.logistic.api.model.person.*;
import com.logistic.api.model.post.Stamp;

import java.util.Date;

/**
 * Created by SnakE on 02.11.2015.
 */
public class StampImpl implements Stamp {
    private Date date;
    private Address address;

    public StampImpl(PostOfficeImpl postOffice){
        this.address = postOffice.getAddress();
        this.date = new Date();
    }

    @Override
    public Address getPostOfficeAddress() {
        return address;
    }

    @Override
    public Date getStampDate() {
        return date;
    }
}
