package com.logistic.impl.esa.serialization;

import java.io.*;

/**
 * Created by SnakE on 22.11.2015.
 */
public class SerializationUtility {

    public static void serializeToFile (Serializable object, File f) throws IOException{
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.flush();
        oos.close();
    }


    public static Object deserializeFromFile (File f) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);
        return ois.readObject();
    }

}
