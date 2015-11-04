package com.logistic.impl.model.post;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.person.FullName;
import com.logistic.api.model.post.*;
import com.logistic.api.model.person.Person;
import com.logistic.api.model.post.Package;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.CRC32;

/**
 * Created by SnakE on 02.11.2015.
 */
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
     * @param reciever    reciever of a package
     * @param type        package type
     * @param weight      package weight
     */
    public PackageImpl(Person sender, Person reciever, PackageImpl.Type type, int weight) {
        this.sender = sender;
        this.receiver = reciever;
        this.type = type;
        this.weight = weight;
        this.packageId = generateID();
        stamps = new ArrayList<Stamp>();
    }


    /**
     * Generate package ID
     * @return
     */
    private String generateID() {
        StringBuilder sbuild = new StringBuilder();
        sbuild.append(sender.getFullName());
        sbuild.append("\r\n");
        sbuild.append(sender.getAddress());
        sbuild.append("\r\n");
        sbuild.append(receiver.getFullName());
        sbuild.append("\r\n");
        sbuild.append(receiver.getAddress());
        sbuild.append("\r\n");
        sbuild.append(type.getDescription());
        sbuild.append("\r\n");
        sbuild.append(weight);
        sbuild.append("\r\n");
        sbuild.append(new Date().toString());
        CRC32 crc32 = new CRC32();
        crc32.update(sbuild.toString().getBytes());

        return Long.toString(crc32.getValue());
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
