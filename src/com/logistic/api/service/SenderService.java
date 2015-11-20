package com.logistic.api.service;

import com.logistic.api.model.post.Package;
import com.logistic.api.model.post.PostOffice;
import com.logistic.api.model.transport.Transit;
import com.logistic.impl.service.exceptions.NullPackageException;
import com.logistic.impl.service.exceptions.NullTransitException;

import java.util.List;

/**
 * Created by Denis on 5/25/2015.
 */
public interface SenderService {
    List<PostOffice> getAllOffices();
    List<Transit> calculatePossibleTransits(Package parcel) throws NullPackageException;
    boolean sendPackage(Package parcel, Transit transit) throws NullPackageException, NullTransitException;
    PostOffice getPackageCurrentPosition(String id);
    double getMilesToDestination(String id);
}
