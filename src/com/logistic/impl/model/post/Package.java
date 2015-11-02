package com.logistic.impl.model.post;

import com.logistic.impl.model.person.Address;
import com.logistic.api.model.person.FullName;
import com.logistic.api.model.post.Stamp;
import com.logistic.impl.model.person.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muny on 02.11.2015.
 */
public class Package implements com.logistic.api.model.post.Package {
	
	private String packageId;
	private int weight;
	private Type type;
	private Person receiver;
	private Person sender;
	private ArrayList<Stamp> stamps;

	public Package(Person sender, Person reciever, Package.Type type, int weight) {
        this.sender = sender;
        this.receiver = reciever;
        this.type = type;
        this.weight = weight;
        this.packageId = generateID();
        stamps = new ArrayList<Stamp>();
   }

    private String generateID() {
        // TODO: package id generator
        return null;
    }
	
    @Override
    public String getPackageId() {
        return packageId;
    }
    
	@Override
    public int getWeight() {
        return weight;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Address getReceiverAddress() {
        return receiver.getAddress();
    }

    @Override
    public Address getSenderAddress() {
        return sender.getAddress();
    }

    @Override
    public FullName getSenderFullName() {
        return sender.getFullName();
    }

    @Override
    public FullName getReceiverFullName() {
        return receiver.getFullName();
    }

    @Override
    public List<Stamp> getStamps() {
        return stamps;
    }


	
}
