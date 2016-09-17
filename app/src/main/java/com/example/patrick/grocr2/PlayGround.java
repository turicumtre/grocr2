package com.example.patrick.grocr2;

import java.util.ArrayList;

/**
 * Created by Patrick on 17.09.2016.
 */
public class PlayGround {
    Orders order = new Orders(1.,1.,"s",true,0,0,0,new ArrayList<Float>());
    try {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream so = new ObjectOutputStream(bo);
        so.writeObject(myObject);
        so.flush();
        serializedObject = bo.toString();
    } catch (Exception e) {
        System.out.println(e);
    }

}
