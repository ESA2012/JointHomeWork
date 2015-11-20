package com.logistic.impl.model.post;

import com.logistic.api.model.person.*;
import com.logistic.api.model.post.PostOffice;
import com.logistic.api.model.post.Stamp;

import java.util.Date;


public class StampImpl implements Stamp {
    private Date date;
    private Address address;

    public StampImpl(PostOffice postOffice){
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

    @Override
    public String toString() {
        return date+" --> "+address;
    }
}
