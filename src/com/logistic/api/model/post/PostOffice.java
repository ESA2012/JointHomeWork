package com.logistic.api.model.post;

import com.logistic.api.model.person.Address;

import java.awt.*;

/**
 * Created by Denis on 5/25/2015.
 */
public interface PostOffice {
    Stamp getStamp();
    Address getAddress();
    Package.Type getAcceptableTypes();
    int getMaxWeight();
    boolean sendPackage(Package parcel);
    boolean receivePackage(Package parcel);
    int getCode();
    Point getGeolocation();
}
