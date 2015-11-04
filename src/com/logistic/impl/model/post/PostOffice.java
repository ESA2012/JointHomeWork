package src.com.logistic.impl.model.post;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.post.Package;

import java.awt.*;

/**
 * Created by SnakE on 02.11.2015.
 */
public class PostOffice implements com.logistic.api.model.post.PostOffice {
	
	private int code;
	private Point location;
	private Address address;
    private Stamp stamp;
    private Package.Type type;
    
    
    

	public PostOffice(int code, Point location, Address address, Stamp stamp) {
		
		this.code = code;
		this.location = location;
		this.address = address;
		this.stamp = stamp;
		type = type.T_30;
	}


	public void setType(Package.Type type) {
		type = type;
	}

	@Override
    public Stamp getStamp() {
        return stamp;
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
    	if(parcel.getPackageId()!=null){
    		//тут надо поставить штамп почтового отделения
        return true;
        }else{
    	return false;}
    }

    @Override
    public boolean receivePackage(Package parcel) {
    	if(parcel.getReceiverFullName()!=null&&parcel.getReceiverAddress()!=null
    			&&parcel!=null){
    		//тут надо поставить штамп почтового отделения
        return true;
        }else{
        	return false;
        }
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public Point getGeolocation() {
        return location;
    }
}
