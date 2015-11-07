package com.logistic.impl.service;

import com.logistic.api.model.post.*;
import com.logistic.api.model.post.Package;
import com.logistic.api.service.Storage;
import com.logistic.impl.service.generators.BigGenerator;
import com.logistic.impl.service.generators.RouteGenerator;

import java.awt.*;
import java.util.List;


/**
 * Created by SnakE on 04.11.2015.
 */
public class DataStorage {
    public static final int POST_OFFICES = 0;
    public static final int PACKAGES = 1;

    private static RouteMatrix matrix;

    public static final Rectangle AREA              = new Rectangle(800, 600);
    public static final String COUNTRY_NAME         = "Старорумбия";
    public static final int POST_OFFICES_COUNT      = 30;
    public static final int DISTANCE_BTWN_OFFICES   = 80;

    public static final int MIN_ROUTE_LENGTH        = 0;
    public static final int MAX_ROUTE_LENGTH        = 180;



    static {
        Storage.getInstance().putToStorage(POST_OFFICES, BigGenerator.generatePostOffices(
                COUNTRY_NAME, POST_OFFICES_COUNT, AREA, DISTANCE_BTWN_OFFICES));
        matrix = RouteGenerator.buildRandomMatrix(DataStorage.getPostOffices(), MIN_ROUTE_LENGTH, MAX_ROUTE_LENGTH);
    }



//    public static void readPostOfficesData() throws IOException {
//        ArrayList<PostOffice> offices = new ArrayList<PostOffice>();
//
//        Scanner sc = new Scanner(new File("postoffices.txt"));
//        while (sc.hasNextLine()) {
//            String s = sc.nextLine();
//            String[] z = s.split("\\s*\\;\\s*");
//            AddressImpl address = new AddressImpl(Integer.valueOf(z[0]), z[1], z[2], z[3]);
//            PostOfficeImpl po = new PostOfficeImpl(address, new Point(Integer.valueOf(z[5]), Integer.valueOf(z[6])), Package.Type.valueOf(z[4]));
//            offices.add(POST_OFFICES, po);
//        }
//        sc.close();
//        Storage.getInstance().putToStorage(POST_OFFICES, offices);
//    }

    public static RouteMatrix getRouteMatrix() {
        return matrix;
    }

    public static List<PostOffice> getPostOffices() {
        return Storage.getInstance().getById(POST_OFFICES);
    }

    public static List<Package> getPackages() {
        return Storage.getInstance().getById(PACKAGES);
    }
}
