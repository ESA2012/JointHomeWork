package com.logistic.api.model.transport;

import com.logistic.api.model.post.PostOffice;

/**
 * Created by Denis on 5/25/2015.
 */
public interface DeliveryTransport {
    Type getType();
    PostOffice getStartPostOffice();
    PostOffice getDestinationPostOffice();

    enum Type {
//        SEA(10, 2.5), AIR(50, 25.2), LAND(18, 1.26);
        SEA(4, 0.02), AIR(60, 0.10), LAND(8, 0.05);

        private int speed;
        private double costPerMile;

        Type(int speed, double costPerMile) {
            this.speed = speed;
            this.costPerMile = costPerMile;
        }

        public int getSpeed() {
            return speed;
        }

        public double getCostPerMile() {
            return costPerMile;
        }
    }
}
