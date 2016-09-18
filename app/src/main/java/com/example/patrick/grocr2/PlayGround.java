package com.example.patrick.grocr2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Patrick on 17.09.2016.
 */
public class PlayGround {

    public static void main(String[] args) {
        Orders order = new Orders(1.,1.,"s",true,0,0,0,new ArrayList<Float>());

        String serializedObject = "";

        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(order);
            so.flush();
            serializedObject = bo.toString();
        } catch (Exception e) {
            System.out.println(e);
        }

        //
        Orders order2=null;
        try {
            byte b[] = serializedObject.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            order2 = (Orders) si.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Done!");
        System.out.println(order2.refugee);

    }
}
