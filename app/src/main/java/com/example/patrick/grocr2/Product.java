package com.example.patrick.grocr2;

import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Patrick on 17.09.2016.
 */
public class Product {

    public String name;

    public String EAN;
    public double price;
    public String itemGroup;
    int amount;

    public Product(String name, String EAN, double price, String itemGroup, int amount) {
        this.name = name;
        this.EAN = EAN;
        this.price = price;
        this.itemGroup = itemGroup;
        this.amount = amount;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }


    public void setName(String name) {
        this.name = name;
    }
}
