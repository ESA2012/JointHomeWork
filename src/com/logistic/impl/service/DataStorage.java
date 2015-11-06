package com.logistic.impl.service;

import com.logistic.api.model.post.*;
import com.logistic.api.model.post.Package;
import com.logistic.api.service.Storage;
import com.logistic.impl.Window;
import com.logistic.impl.model.person.AddressImpl;
import com.logistic.impl.model.post.PostOfficeImpl;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


/**
 * Created by SnakE on 04.11.2015.
 */
public class DataStorage {
    public static final int POST_OFFICES = 0;
    public static final int PACKAGES = 1;

    private static RouteMatrix matrix;

    public static void readPostOfficesData() throws IOException {
        ArrayList<PostOffice> offices = new ArrayList<PostOffice>();

        Scanner sc = new Scanner(new File("postoffices.txt"));
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            String[] z = s.split("\\s*\\;\\s*");
            AddressImpl address = new AddressImpl(Integer.valueOf(z[0]), z[1], z[2], z[3]);
            PostOfficeImpl po = new PostOfficeImpl(address, new Point(Integer.valueOf(z[5]), Integer.valueOf(z[6])), Package.Type.valueOf(z[4]));
            offices.add(POST_OFFICES, po);
        }
        sc.close();
        Storage.getInstance().putToStorage(POST_OFFICES, offices);

        matrix = new RouteMatrix(getPostOffices().size());

        fillRandomMatrix(matrix, DataStorage.getPostOffices());

        System.out.println(matrix);
    }


    public static RouteMatrix getRouteMatrix() {
        return matrix;
    }

    private static void fillRandomMatrix(RouteMatrix routeMatrix, List<PostOffice> postOffices) {
        Random rnd = new Random();
        for (int i = 0; i < routeMatrix.getSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                int n = rnd.nextInt(11);
                boolean b = n > 3? true : false;

                Point p1 = postOffices.get(i).getGeolocation();
                Point p2 = postOffices.get(j).getGeolocation();

                b = (p1.distance(p2) > 250d)? false:b;
                b = (i == j)? false : b;

                routeMatrix.getMatrix()[i][j] = b;
                routeMatrix.getMatrix()[j][i] = b;
            }
        }
    }


    public static List<PostOffice> getPostOffices() {
        return Storage.getInstance().getById(POST_OFFICES);
    }

    public static List<Package> getPackages() {
        return Storage.getInstance().getById(PACKAGES);
    }
}
