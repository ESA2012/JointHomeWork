package com.logistic.impl.model.post;

import com.logistic.api.model.person.Address;

import java.util.Date;

/**
 * Created by SnakE on 02.11.2015.
 */
public class Stamp implements com.logistic.api.model.post.Stamp {
    @Override
    public Address getPostOfficeAddress() {
        return null;
    }

    @Override
    public Date getStampDate() {
        return null;
    }
}
