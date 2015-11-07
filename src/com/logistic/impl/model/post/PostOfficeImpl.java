package com.logistic.impl.model.post;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.post.Package;
import com.logistic.api.model.post.PostOffice;
import com.logistic.api.model.post.Stamp;

import java.awt.*;



public class PostOfficeImpl implements PostOffice, PostOfficeImproved {
	
	private Point location;
	private Address address;
    private Package.Type type;
    
	public PostOfficeImpl(Address address, Point location, Package.Type type) {
		this.location = location;
		this.address = address;
		this.type = type;
	}


	public void setType(Package.Type type) {
		this.type = type;
	}

	@Override
    public Stamp getStamp() {
        return new StampImpl(this);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public Package.Type getAcceptableTypes() {
        return type;
    }

    @Override
    public int getMaxWeight() {
        return type.getMaxWeight();
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

    @Override
    public int getCode() {
        return address.getCode();
    }

    @Override
    public Point getGeolocation() {
        return location;
    }

    @Override
    public String toString() {
        return address.toString() + " ("+location.getX()+", "+location.getY()+")";
    }

}
