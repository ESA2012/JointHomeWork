package com.logistic.impl.model.post;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.person.FullName;
import com.logistic.api.model.post.Stamp;

import java.util.List;

/**
 * Created by SnakE on 02.11.2015.
 */
public class Package implements com.logistic.api.model.post.Package {
    @Override
    public String getPackageId() {
        return null;
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public Address getReceiverAddress() {
        return null;
    }

    @Override
    public Address getSenderAddress() {
        return null;
    }

    @Override
    public FullName getSenderFullName() {
        return null;
    }

    @Override
    public FullName getReceiverFullName() {
        return null;
    }

    @Override
    public List<Stamp> getStamps() {
        return null;
    }
}
