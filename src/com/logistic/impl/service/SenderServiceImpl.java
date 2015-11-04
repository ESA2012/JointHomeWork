package com.logistic.impl.service;

import com.logistic.api.model.post.*;
import com.logistic.api.model.post.Package;
import com.logistic.api.model.transport.Transit;
import com.logistic.api.service.SenderService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SnakE on 04.11.2015.
 */
public class SenderServiceImpl implements SenderService {
    private List<PostOffice> offices;

    @Override
    public List<PostOffice> getAllOffices() {
        return offices;
    }

    @Override
    public List<Transit> calculatePossibleTransits(Package parcel) {
        List<Transit> transits = new ArrayList<Transit>();
        // TODO: fill list with availabe transits
        return transits;
    }

    @Override
    public boolean sendPackage(Package parcel, Transit transit) {
        if (parcel == null) return false;
        if (transit == null) return false;

        return false;
    }

    @Override
    public PostOffice getPackageCurrentPosition(String id) {


        return null;
    }

    @Override
    public double getMilesToDestination(String id) {
        return 0;
    }
}
