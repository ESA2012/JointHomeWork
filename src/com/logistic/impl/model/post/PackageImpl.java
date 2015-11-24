package com.logistic.impl.model.post;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.person.FullName;
import com.logistic.api.model.post.*;
import com.logistic.api.model.person.Person;
import com.logistic.api.model.post.Package;
import com.logistic.impl.service.DataStorage;

import java.util.*;
import java.util.zip.CRC32;



public class PackageImpl implements Package {
	
	private String packageId;
	private int weight;
	private Package.Type type;
	private Person receiver;
	private Person sender;
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
        this.weight = weight;
        this.type = type;
        this.packageId = generateID();
        stamps = new ArrayList<>();
        this.type = correctType(weight);
    }


    /**
     * Corrects package type by its weight
     * @param weight    weight
     * @return          correct package type
     */
    private Type correctType(int weight) {
        Type result = this.type;
        if (weight > type.getMaxWeight()) {
            int diff1 = Integer.MAX_VALUE;
            Type newType = null;
            int maxTypesWeight = 0;
            for (Type t: Type.values()) {
                int maxWeight = t.getMaxWeight();
                int diff2 = maxWeight - weight;
                if (maxWeight > maxTypesWeight) {
                    maxTypesWeight = maxWeight;
                }
                if (diff2 > 0 && diff2 < diff1) {
                    diff1 = diff2;
                    newType = t;
                }
            }
            if (weight > maxTypesWeight) {
                result = Type.T_CP;
            } else {
                result = newType;
            }
        }
        return result;
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

    public String toString() {
        return packageId + " : " + sender.getAddress().getCode() + " -> " + receiver.getAddress().getCode();
    }
}
