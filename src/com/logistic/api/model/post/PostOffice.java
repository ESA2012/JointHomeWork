package com.logistic.api.model.post;

import com.logistic.api.model.person.Address;
import com.logistic.impl.exceptions.NullPackageException;

import java.awt.*;
import java.util.Set;

/**
 * Created by Denis on 5/25/2015.
 */
public interface PostOffice {
    Stamp getStamp();
    Address getAddress();
    Set<Package.Type> getAcceptablePackageTypes();  // Changed by ESA
    int getMaxWeight();
    boolean sendPackage(Package parcel) throws NullPackageException;    // Changed by ESA
    boolean receivePackage(Package parcel);
    int getCode();
    Point getGeolocation();
}
