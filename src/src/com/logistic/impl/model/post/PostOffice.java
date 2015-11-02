package com.logistic.impl.model.post;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.post.Package;

import java.awt.*;

/**
 * Created by SnakE on 02.11.2015.
 */
public class PostOffice implements com.logistic.api.model.post.PostOffice {
    @Override
    public Stamp getStamp() {
        return null;
    }

    @Override
    public Address getAddress() {
        return null;
    }

    @Override
    public Package.Type getAcceptableTypes() {
        return null;
    }

    @Override
    public int getMaxWeight() {
        return 0;
    }

    @Override
    public boolean sendPackage(Package parcel) {
        return false;
    }

    @Override
    public boolean receivePackage(Package parcel) {
        return false;
    }

    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public Point getGeolocation() {
        return null;
    }
}
