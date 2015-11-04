package com.logistic.impl.service;

import com.logistic.api.model.post.PostOffice;
import com.logistic.impl.model.post.PostOfficeImpl;
import com.logistic.api.service.Storage;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by SnakE on 04.11.2015.
 */
public class DataStorage {
    public static final int POST_OFFICES = 0;

    public static void readData() throws IOException {
        ArrayList<PostOffice> offices = new ArrayList<PostOffice>();

        Scanner sc = new Scanner(new File("postoffices.txt"));

        PostOfficeImpl po = new PostOfficeImpl();



        Storage.getInstance().putToStorage(POST_OFFICES, offices);


    }

    public static List<PostOffice> getPostOffices() {
        return Storage.getInstance().getById(POST_OFFICES);
    }
}
