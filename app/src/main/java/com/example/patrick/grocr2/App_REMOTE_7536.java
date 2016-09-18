package com.example.patrick.grocr2;

import android.app.Application;

import java.util.Set;

/**
 * Created by Patrick on 18.09.2016.
 */
public class App extends Application {
    Orders currentOrder=null;
    Set<Product> currentOrderProducts = null;
    String id;
}