package com.logistic.impl.model.post;

import com.logistic.api.model.post.Package;
import com.logistic.api.model.post.PostOffice;

import java.util.List;

/**
 * Created by snake on 07.11.15.
 */
public interface PostOfficeImproved extends PostOffice{
    List<Package.Type> getAcceptableTypes2();
}
