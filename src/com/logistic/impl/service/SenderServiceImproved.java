package com.logistic.impl.service;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.post.PostOffice;
import com.logistic.api.service.SenderService;
import com.logistic.impl.model.post.PostOfficeImproved;

import java.util.List;

/**
 * Created by SnakE on 08.11.2015.
 */
public interface SenderServiceImproved extends SenderService {
    PostOfficeImproved findClosestPostOffice(Address address);
    List<PostOfficeImproved> getAllOfficesImr();
}
