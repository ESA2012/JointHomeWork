package com.logistic.impl.service;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.post.*;
import com.logistic.api.service.SenderService;


/**
 * Created by SnakE on 08.11.2015.
 */
public interface SenderServiceImproved extends SenderService {
    PostOffice findClosestPostOffice(int personIndex);
    boolean isPackageOnTheWay(String packageID);
    boolean isPackageDelivered(String packageID);
}
