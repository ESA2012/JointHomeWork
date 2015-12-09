package com.logistic.impl.esa.generators;

import com.logistic.api.model.person.Address;
import com.logistic.api.model.person.FullName;
import com.logistic.api.model.person.Person;
import com.logistic.api.model.post.*;
import com.logistic.api.model.post.Package;
import com.logistic.impl.model.person.AddressImpl;
import com.logistic.impl.model.person.FullNameImpl;
import com.logistic.impl.model.person.PersonImpl;
import com.logistic.impl.model.post.PackageImpl;
import com.logistic.impl.model.post.PostOfficeImpl;
import com.logistic.impl.service.DataStorage;
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

    private static String[] lNames = {"Иванов", "Петров", "Сидоров", "Гончаров", "Дружко", "Вражко"};

    private static String[] fNames = {"Иван", "Пётр", "Андрей", "Данила", "Геннадий", "Юрий"};

    private static String[] mNames = {"Иванович", "Петрович", "Андреевич", "Данилович", "Геннадиевич", "Юриевич"};


    /**
     * Generates random person
     * @return  new person instance
     */
    public static Person generatePerson() {
        Address address = (DataStorage.getPostOffices().get(new Random().nextInt(DataStorage.getPostOffices().size())).getAddress());
        FullName fullName = generateFullName();
        return new PersonImpl(fullName, address);
    }


    /**
     * Generates full name of person
     * @return  new full name instance
     */
    public static FullName generateFullName() {
        String f = fNames[new Random().nextInt(fNames.length)];
        String l = lNames[new Random().nextInt(lNames.length)];
        String m = mNames[new Random().nextInt(mNames.length)];
        return new FullNameImpl(f,m,l);
    }


    /**
     * Generates random package
     * @return  new package instance
     */
    public static Package generatePackage() {
        return generatePackage(Package.Type.getRandomType());
    }


    /**
     * Generates random package with given package type
     * @param ptype     maximum package type
     * @return          new package instance
     */
    public static Package generatePackage(Package.Type ptype) {
        int maxweight = ptype.getMaxWeight();
        maxweight = maxweight == 0? 100 : maxweight;
        Person p1, p2;
        do {
            p1 = generatePerson();
            p2 = generatePerson();
        } while (p1.getAddress() == p2.getAddress());

        return new PackageImpl(p1, p2, ptype, new Random().nextInt(maxweight)+1);
    }



    /**
     * Generates post offices list
     * @param country country name
     * @param count the number of post offices
     * @param dimension area for post offices placement
     * @param distance  minimum allowed distance between post offices
     */
    public static ArrayList<PostOffice> generatePostOffices (String country, int count, Dimension dimension, double distance) {
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
                p = generateLocationImproved(index, dimension); // generates coordinates by index
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
    public static Set<Package.Type> generatePackageTypes() {
        Set<Package.Type> typeSet = new HashSet<>();
        int typeCount = Package.Type.values().length;
        Random rnd = new Random();
        int x = rnd.nextInt(typeCount * 10);
            if (x >= 0) typeSet.add(Package.Type.T_10);
            if (x > 5) typeSet.add(Package.Type.T_25);
            if (x > 10) typeSet.add(Package.Type.T_26);
            if (x > 20) typeSet.add(Package.Type.T_27);
            if (x > 30) typeSet.add(Package.Type.T_28);
            if (x > 50) typeSet.add(Package.Type.T_30);
            if (x > 60) typeSet.add(Package.Type.T_CP);
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



    /**
     * Generates address
     * @return  new Address instance
     */
    public static Address generateAddress() {
        return new AddressImpl(generateIndex(), generateStreet(), generateCityName(), countryName);
    }



    /**
     * Generates index from geolocation
     * @param location     geolocation point
     * @param dimension    size of graph panel
     * @return             index (zip-code analogue)
     */
    public static int generateIndex(Point location, Dimension dimension) {
        int subW = dimension.width / 3;
        int subH = dimension.height / 3;
        Rectangle[] subs = {new Rectangle(0, 0,subW, subH),         new Rectangle(subW, 0, subW, subH),         new Rectangle(2*subW, 0, subW, subH),
                            new Rectangle(0, subH, subW, subH),     new Rectangle(subW, subH, subW, subH),      new Rectangle(2*subW, subH, subW, subH),
                            new Rectangle(0, 2*subH, subW, subH),   new Rectangle(subW, 2*subH, subW, subH),    new Rectangle(2*subW, 2*subH, subW, subH)};
        int z = 0;
        for (int i = 0; i < 9; i++) {
            if (subs[i].contains(location)) {
                z = (i + 1) * 10000;
                break;
            }
        }
        Random r = new Random();
        int index = 0;
        boolean ok = false;
        do {
            index = r.nextInt(10000) + z;
            for (PostOffice p: DataStorage.getPostOffices()) {
                if (p.getCode() == index) {
                    ok = false;
                    continue;
                } else {
                    ok = true;
                    break;
                }
            }
        } while (!ok);
        return index;
    }



    private static LocationGenerationRule[] locationRules = {
            new LocationGenerationRule(10000, 20000, 0, 0, 50, 50),
            new LocationGenerationRule(20000, 30000, 1, 0, 0, 50),
            new LocationGenerationRule(30000, 40000, 2, 0, -50, 50),
            new LocationGenerationRule(40000, 50000, 0, 1, 50, 0),
            new LocationGenerationRule(50000, 60000, 1, 1, 0, 0),
            new LocationGenerationRule(60000, 70000, 2, 1, -50, 0),
            new LocationGenerationRule(70000, 80000, 0, 2, 50, -50),
            new LocationGenerationRule(80000, 90000, 1, 2, 0, -50),
            new LocationGenerationRule(90000, 100000, 2, 2, -50, -50)};


    /**
     * Generate location coordinates in specified area by index.
     * Area divides by 9 subareas. Each subarea have appropriate range of indexes
     * @param index    index (Zip code analogue)
     * @param dimension     area
     * @return
     */
    private static Point generateLocationImproved (int index, Dimension dimension) {

        int zoneWidth = dimension.width / 3;
        int zoneHeight = dimension.height / 3;

        int shiftX = 0;
        int shiftY = 0;

        for(LocationGenerationRule rule: locationRules) {
            if(rule.canApply(index)) {
                int[] coords = rule.apply(zoneWidth, zoneHeight);
                shiftX = coords[0];
                shiftY = coords[1];
                break;
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
            ok = !(((j == 2) || (j == 8)) && ((s.endsWith("я"))));
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
