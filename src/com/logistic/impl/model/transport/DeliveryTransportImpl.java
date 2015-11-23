package com.logistic.impl.model.transport;

import com.logistic.api.model.post.PostOffice;

import java.io.Serializable;


public class DeliveryTransportImpl implements DeliveryTransportImproved, Serializable {
    private PostOffice officeA;
    private PostOffice officeB;
    private Type type;
    private double price;
    private double range;
    private int time;


    public DeliveryTransportImpl (PostOffice officeA, PostOffice officeB, Type type) {
        // TODO: Вопрос: объекст создастся в любом случае? как недопустить создание объекта?
        if (officeA == null || officeB == null) return;
        this.officeA = officeA;
        this.officeB = officeB;
        this.type = type;
        range = officeA.getGeolocation().distance(officeB.getGeolocation());
        price = getRange()*type.getCostPerMile();
        time = (int)(getRange() / type.getSpeed() * 300);
    }


    @Override
    public Type getType() {
        return type;
    }


    @Override
    public PostOffice getStartPostOffice() {
        return officeA;
    }


    @Override
    public PostOffice getDestinationPostOffice() {
        return officeB;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public int getTime() {
        return time;
    }

    @Override
    public double getRange() {
        return range;
    }


    public String toString() {
        return officeA.getCode()
                + " -> " + officeB.getCode()
                + " : Range=" + getRange()
                + "; Time="+ getTime()
                + "; Type=" + getType()
                + "; Cost=" + getPrice();
    }
}
