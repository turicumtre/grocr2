package com.example.patrick.grocr2;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Patrick on 18.09.2016.
 */
public class App extends Application {
    Orders currentOrder=null;
    String id;
    List<Product> allProducts= new ArrayList<>();
    List<Product> initialProducts= new ArrayList<>();
    Map<String,Product> EANToProduct = new HashMap<>();
    List<Orders> ordersList = new ArrayList<>();
    Orders OrderChosenFromMap;
}