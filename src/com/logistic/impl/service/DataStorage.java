package com.logistic.impl.service;

import com.logistic.api.model.post.*;
import com.logistic.api.model.post.Package;
import com.logistic.api.service.Storage;
import com.logistic.impl.model.person.AddressImpl;
import com.logistic.impl.model.post.PostOfficeImpl;
import com.logistic.impl.model.post.PostOfficeImproved;
import com.logistic.impl.service.generators.BigGenerator;
import com.logistic.impl.service.generators.RouteGenerator;
import com.logistic.impl.service.generators.RouteType;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


/**
 * Created by SnakE on 04.11.2015.
 */
public class DataStorage {
    public static final int POST_OFFICES = 0;
    public static final int PACKAGES = 1;

    private static RouteMatrix matrixRoad;
    private static RouteMatrix matrixAir;

    // ---------- Initialise Generators constants ----------
    public static final Rectangle AREA              = new Rectangle(800, 600);
    public static final int POST_OFFICES_COUNT      = 30;   // N
    public static final int DISTANCE_BTWN_OFFICES   = 80;   // 0 - Rectangle AREA side

    // ------------------------------------------------------


    static {
        Storage.getInstance().putToStorage(POST_OFFICES,
                BigGenerator.generatePostOffices(BigGenerator.countryName, POST_OFFICES_COUNT, AREA, DISTANCE_BTWN_OFFICES));

        matrixRoad = RouteGenerator.buildRandomMatrix(DataStorage.getPostOffices(), RouteType.ROAD);

        matrixAir = RouteGenerator.buildRandomMatrix(DataStorage.getPostOffices(), RouteType.AIR);

//        try {
//            loadFromFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        matrixRoad = new RouteMatrix(7);
//        matrixRoad.setMatrix(new boolean[][]{
//                {false, false, true, false, false, false, false},
//                {false, false, true, false, true, false, false},
//                {true, true, false, false, false, true, false},
//                {false, false, false, false, true, false, false},
//                {false, true, false, true, false, true, true},
//                {false, false, true, false, true, false, false},
//                {false, false, false, false, true, false, false}});
    }


    public static void loadFromFile(String fileName) throws IOException {
        ArrayList<PostOffice> offices = new ArrayList<PostOffice>();

        Scanner sc = new Scanner(new File(fileName));
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            String[] z = s.split("\\s*\\;\\s*");
            AddressImpl address = new AddressImpl(Integer.valueOf(z[0]), z[1], z[2], z[3]);
            HashSet<Package.Type> pkg = new HashSet<>();
            pkg.add(Package.Type.T_25);
            PostOfficeImpl po = new PostOfficeImpl(address, new Point(Integer.valueOf(z[5]), Integer.valueOf(z[6])), pkg);
            offices.add(po);
        }
        sc.close();
        Storage.getInstance().putToStorage(POST_OFFICES, offices);
    }




    public static RouteMatrix getRoadRouteMatrix(RouteType routeType) {
        switch (routeType) {
            case ROAD: return matrixRoad;
            case AIR: return matrixAir;
            default: return null;
        }
    }


    public static List<PostOfficeImproved> getPostOffices() {
        return Storage.getInstance().getById(POST_OFFICES);
    }

    public static List<Package> getPackages() {
        return Storage.getInstance().getById(PACKAGES);
    }
}
