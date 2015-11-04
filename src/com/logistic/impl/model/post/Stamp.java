package src.com.logistic.impl.model.post;

import com.logistic.api.model.person.*;

import java.util.Date;

/**
 * Created by SnakE on 02.11.2015.
 */
public class Stamp implements com.logistic.api.model.post.Stamp {
    private Date date;
    private Address address;

    public Stamp(PostOffice postOffice){
        this.address = postOffice.getAddress();
        this.date = new Date();
    }

    @Override
    public Address getPostOfficeAddress() {
        return null;
    }

    @Override
    public Date getStampDate() {
        return null;
    }
}
