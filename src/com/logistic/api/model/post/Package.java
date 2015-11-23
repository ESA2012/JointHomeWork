package com.logistic.api.model.post;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.person.FullName;

import java.util.List;
import java.util.Random;

/**
 * Created by Denis on 5/25/2015.
 *
 * Updated by ESA
 */
public interface Package {
    String getPackageId();
    int getWeight();
    Type getType();
    Address getReceiverAddress();
    Address getSenderAddress();
    FullName getSenderFullName();
    FullName getReceiverFullName();
    List<Stamp> getStamps();

    /**
     * http://www.ups.com/worldshiphe
     * lp/WS15/RUS/AppHelp/Codes/Package_Type_Codes.htm
     */
    enum Type {
        T_10("Коробка UPS 10 кг", 10),
        T_25("Коробка UPS 25 кг", 25),
        T_26("Маленькая коробка UPS Express", 4),   // для отправлений меньшего размера, таких, как книги и кассеты
        T_27("Средняя коробка UPS Express", 5),     // для отправлений меньшего размера, таких, как книги и кассеты
        T_28("Большая коробка UPS Express", 13),    // перевозки весом до 30 фунтов
        T_30("Палета", 50),
        T_CP("Место груза");

        private final String description;
        private final int maxWeight;

        Type(String description) {
            this(description, 0);
        }

        Type(String description, int maxWeight) {
            this.description = description;
            this.maxWeight = maxWeight;
        }

        public String getDescription() {
            return description;
        }

        public int getMaxWeight() {
            return maxWeight;
        }

        public static Type getRandomType() {
            Random r = new Random();
            return values()[r.nextInt(values().length)];
        }
    }
}
