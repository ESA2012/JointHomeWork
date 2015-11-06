package com.logistic.api.service;

import com.logistic.api.model.post.Package;
import com.logistic.api.model.post.PostOffice;
import com.logistic.api.model.transport.Transit;

import java.util.List;

/**
 * Created by Denis on 5/25/2015.
 */
public interface SenderService {
    List<PostOffice> getAllOffices();
    List<Transit> calculatePossibleTransits(Package parcel);
    boolean sendPackage(Package parcel, Transit transit);
    PostOffice getPackageCurrentPosition(String id);
    double getMilesToDestination(String id);

}
