package com.logistic.impl.model.post;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.person.FullName;
import com.logistic.api.model.post.Stamp;

import java.util.List;

/**
 * Created by SnakE on 02.11.2015.
 */
public class Package implements com.logistic.api.model.post.Package {
	
	private String PackageId;
	private int Weight;
	private Type Type;
	private Address ReceiverAddress;
	private Address SenderAddress;
	private FullName SenderFullName;
	private FullName ReceiverFullName;
	private List<Stamp> Stamps;
//______________________________________________________________________________________	
	
	
	
//______________________________________________________________________________________	
    @Override
    public String getPackageId() {
        return PackageId;
    }

    @Override
    public int getWeight() {
        return Weight;
    }

    @Override
    public Type getType() {
        return Type;
    }

    @Override
    public Address getReceiverAddress() {
        return ReceiverAddress;
    }

    @Override
    public Address getSenderAddress() {
        return SenderAddress;
    }

    @Override
    public FullName getSenderFullName() {
        return SenderFullName;
    }

    @Override
    public FullName getReceiverFullName() {
        return ReceiverFullName;
    }

    @Override
    public List<Stamp> getStamps() {
        return Stamps;
    }
}
