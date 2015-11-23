package com.logistic.impl.model.post;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.post.Package;
import com.logistic.api.model.post.PostOffice;
import com.logistic.api.model.post.Stamp;
import com.logistic.impl.exceptions.NullPackageException;

import java.awt.*;
import java.io.Serializable;
import java.util.*;


public class PostOfficeImpl implements PostOffice, Serializable {
	
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
        return types.iterator().next();
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


    /**
     * Returns true if package can be send to next post office,
     * and returns false when package receiver address and a post office address is equals
     * @param parcel a Package object
     * @return
     * @throws NullPackageException when Package is null
     */
    @Override
    public boolean sendPackage(Package parcel) throws NullPackageException{
        if (parcel == null) throw new NullPackageException();
        return !((PackageImproved) parcel).getReceiverPostOfficeAddreess().equals(address);
    }


    /**
     *
     * @param parcel
     * @return
     */
    @Override
    public boolean receivePackage(Package parcel) {
        if (parcel != null) {
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
