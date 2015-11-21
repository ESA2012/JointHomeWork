package com.logistic.impl.model.post;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.person.FullName;
import com.logistic.api.model.post.*;
import com.logistic.api.model.person.Person;
import com.logistic.api.model.post.Package;
import com.logistic.impl.service.DataStorage;

import java.util.*;
import java.util.zip.CRC32;



public class PackageImpl implements PackageImproved {
	
	private String packageId;
	private int weight;
	private Package.Type type;
	private Person receiver;
	private Person sender;
    private final Address poSenderAddr;
    private final Address poReceiverAddr;
	private ArrayList<Stamp> stamps;

    /**
     * Creates a package
     * @param sender      sender of a package
     * @param receiver    receiver of a package
     * @param type        package type
     * @param weight      package weight
     */
    public PackageImpl(Person sender, Person receiver, PackageImpl.Type type, int weight) {
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.weight = weight;
        this.packageId = generateID();
        stamps = new ArrayList<>();
        poReceiverAddr = findClosestPostOffice(receiver.getAddress()).getAddress();
        poSenderAddr = findClosestPostOffice(sender.getAddress()).getAddress();
    }



    private PostOffice findClosestPostOffice(Address address) {
        int personIndx = address.getCode();
        int min = Integer.MAX_VALUE;
        PostOffice post = null;
        for (PostOffice p: DataStorage.getPostOffices()) {
            int postIndx = p.getCode();
            int close = Math.abs(personIndx - postIndx);
            if (close < min) {
                min = close;
                post = p;
            }
        }
        return post;
    }



    /**
     * Generate package ID
     * @return package id
     */
    private String generateID() {
        CRC32 crc32 = new CRC32();
        crc32.update((String.valueOf(sender.getFullName())
                        + "\r\n" + sender.getAddress()
                        + "\r\n" + receiver.getFullName()
                        + "\r\n" + receiver.getAddress()
                        + "\r\n" + type.getDescription()
                        + "\r\n" + weight
                        + "\r\n" + String.format("%1$td.%1$tm.%1$tY  %1$tH:%1$tM:%1$tS.%1$tL", new Date())
                        + "\r\n" + new Random().nextInt(Integer.MAX_VALUE)).getBytes());
        return String.format("%010d", crc32.getValue());
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

    @Override
    public Address getSenderPostOfficeAddress() {
        return poSenderAddr;
    }

    @Override
    public Address getReceiverPostOfficeAddreess() {
        return poReceiverAddr;
    }

    public String toString() {
        return packageId + " : " + sender.getAddress().getCode() + " -> " + receiver.getAddress().getCode();
    }
}
