package com.example.patrick.grocr2;

import android.app.Application;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Patrick on 18.09.2016.
 */
public class App extends Application {
    Orders currentOrder=null;
    Set<Product> currentOrderProducts = new HashSet<>();
    String id;
    Map<String, Product> nameToProduct= new HashMap<>();
    Map<String,Product> EANToProduct = new HashMap<>();

}