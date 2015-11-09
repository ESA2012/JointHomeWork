package com.logistic.impl.model.post;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.post.Package;
import com.logistic.api.model.post.Stamp;

import java.awt.*;
import java.util.*;


public class PostOfficeImpl implements PostOfficeImproved {
	
	private Point location;
	private Address address;
    private Set<Package.Type> types;


    /**
     * Post office constructor.
     * @param address     post office address
     * @param location    post office coordinates
     * @param types       acceptable package types
     */
    public PostOfficeImpl(Address address, Point location, Set<Package.Type> types) {
		this.location = location;
		this.address = address;
		this.types = types;
	}


    /**
     * Returns new instance of stamp
     * @return new stamp
     */
    @Override
    public Stamp getStamp() {
        return new StampImpl(this);
    }


    /**
     * Returns post office address (code, street, city, country)
     * @return address
     */
    @Override
    public Address getAddress() {
        return address;
    }

    @Deprecated
    public Package.Type getAcceptableTypes() {
        // TODO придумать как реализовать этот метод - максимально допустимый тип?
        return null;
    }



    /**
     * Returns maximum allowed package weight to accept by post office
     * @return maximum weight or 0 (any weight is allowed)
     */
    @Override
    public int getMaxWeight() {
        int maxw = 0;
        for(Package.Type pt: types) {
            int ptweight = pt.getMaxWeight();
            if (ptweight == 0) {
                maxw = 0;
                break;
            } else {
                if (ptweight > maxw) {
                    maxw = ptweight;
                }
            }
        }
        return maxw;
    }

    @Override
    public boolean sendPackage(Package parcel) {
        // TODO реализовать метод
    	if(parcel.getPackageId()!=null){
        return true;
        } else {
    	return false;}
    }

    @Override
    public boolean receivePackage(Package parcel) {
        // TODO реализовать метод
        if (parcel.getReceiverFullName()!= null &&
                parcel.getReceiverAddress()!= null &&
                parcel != null) {
            parcel.getStamps().add(new StampImpl(this));
    		return true;
        } else {
        	return false;
        }
    }


    /**
     * Returns post office code (index / zip code)
     * @return index / zip code
     */
    @Override
    public int getCode() {
        return address.getCode();
    }


    /**
     * Returns post office coordinates
     * @return Point
     */
    @Override
    public Point getGeolocation() {
        return location;
    }

    @Override
    public String toString() {
        return address.toString() + " ("+location.getX()+", "+location.getY()+")  " + types;
    }


    /**
     * Returns set of allowed package type
     * @return set of package types
     */
    @Override
    public Set<Package.Type> getAcceptablePackageTypes() {
        return types;
    }
}
