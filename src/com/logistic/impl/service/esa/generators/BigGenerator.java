package com.logistic.impl.service.esa.generators;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.post.*;
import com.logistic.api.model.post.Package;
import com.logistic.impl.model.person.AddressImpl;
import com.logistic.impl.model.post.PostOfficeImpl;
import javafx.scene.shape.Circle;

import java.awt.*;
import java.util.*;

/**
 * Created by SnakE on 07.11.2015.
 */
public class BigGenerator {

    public static String countryName = "Утопия";

    private static String[] cities = { "Ангбанд", "Куёфф", "Линстаград", "Мегасити", "Сноубург", "Бронивики",
                                "Богомолы", "Култаун", "Малиновка", "Султаново", "Тыковка", "Киберсити",
                                "Джавабург", "Барад-дур", "Гондолин", "Дол Гулдур", "Минас Итиль", "Изенгард",
                                "Минас Тирит", "Нарготронд", "Ветроград", "Осгилиат", "Тирион", "Эребор",
                                "Калэмбель", "Дол Амрот", "Мория", "Форлонд", "Дуртанг", "Нью-Васюки", "Калимпорт",
                                "Уотердип", "Торпедосити", "Санкт-Панда", "Петропетровск"};

    private static String[] cityType = {"г. ", "пгт ", "г. ", "г. ", "пос. ", "г. ", "г. ", "c. ", "г. ", "г. "};

    private static String[] streets = { "Пушистых", "Космонавтов", "Мурзилки", "Красивых", "Норманская",
                                        "Петрова", "Варвары", "Пушкина", "Клубнички", "Победы демократии",
                                        "Робототехников", "Чекистов", "Программистов", "Французская",
                                        "Победы диктатуры", "Пролитариата", "Союза", "Дружбы народов", "Зелёной гвардии",
                                        "Кладоискателей", "Чебурашки", "Кинематографистов", "Аптекарей",
                                        "Красной капеллы", "Стали", "Деревянная", "Сиюминутная", "Зелёная",
                                        "Синяя", "Красная", "Чёрная", "Жёлтая", "Оранжевая", "Ванильная",
                                        "Вишнёвая", "Банановая", "Яблочная", "Сливовая", "Вареньичная", "Грушевая",
                                        "Рублёвая", "Китайская", "Дождя", "Небесная"};

    private static String[] streetsType = {"ул. ", "ул. ", "переулок ", "ул. ", "ул. ", "площадь ", "ул. ", "ул. ", "проспект ", "ул. ", "ул. "};



    /**
     * Generates post offices list
     * @param country country name
     * @param count the number of post offices
     * @param rectangle area for post offices placement
     * @param distance  minimum allowed distance between post offices
     */
    public static ArrayList<PostOffice> generatePostOffices (String country, int count, Rectangle rectangle, double distance) {
        ArrayList<PostOffice> posts = new ArrayList<>();
        // Points array for checking distance between post offices
        Point[] points = new Point[count];
        for (int m = 0; m < points.length; m++) {
            points[m] = new Point(0,0);
        }

        // Using TreeMap for sorting by index
        Map<Integer, PostOffice> tempStorage = new TreeMap<>();

        for (int i = 0; i < count; i++) {
            int index = generateIndex();
            String c = generateCityName();
            String s = generateStreet();

            Point p;
            int iterations = 100000; // number of attempts to satisfy the condition of distance between post offices
            do {
                p = generateLocation(index, rectangle); // generates coordinates by index
                if (isGoodPoint(p, points, distance)) { // check condition
                    points[i] = p;
                    break;
                }
                iterations--;
            } while (iterations > 0);
            tempStorage.put(index, new PostOfficeImpl(new AddressImpl(index, s, c, country), p, generatePackageTypes()));
        }

        // Convert Map to Set for further return as array list
        Set<Map.Entry<Integer, PostOffice>> set = tempStorage.entrySet();
        for (Map.Entry<Integer, PostOffice> me: set) {
            posts.add(me.getValue());
        }

        return posts;
    }


    /**
     * Generate random package type set.
     * @return package type set
     */
    private static Set<Package.Type> generatePackageTypes() {
        Set<Package.Type> typeSet = new HashSet<>();
        int typeCount = Package.Type.values().length;
        Random rnd = new Random();
        int x = rnd.nextInt(typeCount * 10);
            if (x >= 0) typeSet.add(Package.Type.T_10);
            if (x > 5) typeSet.add(Package.Type.T_25);
            if (x > 10) typeSet.add(Package.Type.T_27);
            if (x > 35) typeSet.add(Package.Type.T_30);
            if (x > 45) typeSet.add(Package.Type.T_CP);
        return typeSet;
    }


    /**
     * Checks is point away from each point in points array in distance
     * @param point     point to check
     * @param points    array of points
     * @param radius    radius (minmus allowed distance)
     * @return is good point? true / false
     */
    private static boolean isGoodPoint(Point point, Point[] points, double radius) {
        boolean res = true;
        Circle czz = new Circle(point.x, point.y, radius);
        for (Point p: points) {
            if (czz.contains(p.x, p.y)) {
                res = false;
                break;
            }
        }
        return res;
    }



    public static Address generateAddress() {
        return new AddressImpl(generateIndex(), generateStreet(), generateCityName(), countryName);
    }


    /**
     * Generate location coordinates in specified area by index.
     * Area divides by 9 subareas. Each subarea have appropriate range of indexes
     * @param index    index (Zip code analogue)
     * @param rect     area
     * @return
     */
    private static Point generateLocation(int index, Rectangle rect) {
        int overallWidth = rect.width;
        int overallHeight = rect.height;

        int zoneWidth = overallWidth / 3;
        int zoneHeight = overallHeight / 3;

        int shiftX = 0;
        int shiftY = 0;

        if (index >= 10000 && index < 20000) {
            shiftX = 30;
            shiftY = 30;
        } else {
            if (index >= 20000 && index < 30000) {
                shiftX = zoneWidth;
                shiftY = 30;
            } else {
                if (index >= 30000 && index < 40000) {
                    shiftX = zoneWidth * 2 - 50;
                    shiftY = 30;
                } else {
                    if (index >= 40000 && index < 50000) {
                        shiftX = 30;
                        shiftY = zoneHeight;
                    } else {
                        if (index >= 50000 && index < 60000) {
                            shiftX = zoneWidth;
                            shiftY = zoneHeight;
                        } else {
                            if (index >= 60000 && index < 70000) {
                                shiftX = zoneWidth * 2 - 50;
                                shiftY = zoneHeight;
                            } else {
                                if (index >= 70000 && index < 80000) {
                                    shiftX = 0;
                                    shiftY = zoneHeight * 2 - 50;
                                } else {
                                    if (index >= 80000 && index < 90000) {
                                        shiftX = zoneWidth;
                                        shiftY = zoneHeight * 2 - 50;
                                    } else {
                                        if (index >= 90000 && index < 100000) {
                                            shiftX = zoneWidth * 2 - 30;
                                            shiftY = zoneHeight * 2 - 50;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Random r = new Random();

        int x = r.nextInt(zoneWidth) + shiftX;
        int y = r.nextInt(zoneHeight) + shiftY;

        return new Point(x,y);
    }





    /**
     * Generate street name and building number
     * @return street
     */
    public static String generateStreet() {
        Random r = new Random();
        boolean ok;
        String st;
        String s;
        do {
            int i = r.nextInt(streets.length);
            int j = r.nextInt(streetsType.length);
            st = streetsType[j];
            s = streets[i];
            if (((j == 2) || (j == 8)) && ((s.endsWith("я")))) {
                ok = false;
            } else {
                ok = true;
            }
        } while (!ok);
        return st+s+", "+r.nextInt(100);
    }



    /**
     * Generate locality name and its type
     * @return locality
     */
    public static String generateCityName() {
        Random rt = new Random();
        int i = rt.nextInt(cityType.length);
        String cType = cityType[i];
        i = rt.nextInt(cities.length);
        String c = cities[i];
        return cType+c;
    }



    /**
     * Generate index (zip-code analogue)
     * @return index
     */
    public static int generateIndex() {
        Random r = new Random();
        int i1 = (r.nextInt(9)+1) * 10000;
        int i2 = (r.nextInt(9)+1) * 1000;
        int i3 = (r.nextInt(9)+1) * 100;
        int i4 = (r.nextInt(9)+1) * 10;
        int i5 = (r.nextInt(9)+1);
        return i1+i2+i3+i4+i5;
    }





}
