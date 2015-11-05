package com.logistic.impl.service;

import com.logistic.api.model.post.*;
import com.logistic.api.model.post.Package;
import com.logistic.api.service.Storage;
import com.logistic.impl.model.person.AddressImpl;
import com.logistic.impl.model.post.PostOfficeImpl;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * Created by SnakE on 04.11.2015.
 */
public class DataStorage {
    public static final int POST_OFFICES = 0;
    public static final int PACKAGES = 1;

    public static void readData() throws IOException {
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
    }

    public static List<PostOffice> getPostOffices() {
        return Storage.getInstance().getById(POST_OFFICES);
    }

    public static List<Package> getPackages() {
        return Storage.getInstance().getById(PACKAGES);
    }
}
