package com.logistic.impl.model.post;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.post.Package;

/**
 * Created by SnakE on 20.11.2015.
 */
public interface PackageImproved extends Package {
    Address getSenderPostOfficeAddress();
    Address getReceiverPostOfficeAddreess();
}
