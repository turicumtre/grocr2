package com.example.patrick.grocr2;

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
    public final static String[] example_EAN = new String[] {"9002975301268", "7610469295645", "2050000719073","5937","2050000771606"};

    public String name;
    public String EAN;
    public double price;
    public String itemGroup;

    Product(String EAN) {
        this.EAN = EAN;
        final AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("HackZurich","mKw%VY<7.Yb8D!G-");

        final String storeID = "18406";
        //client.get("https://backend.scango.ch/api/v01/items/find-by-ean/?ean="+EAN+"&format=json&retail_store_id="+storeID, new AsyncHttpResponseHandler() {
        client.get("https://backend.scango.ch/api/v01/items/find-by-ean/?ean=9002975301268&format=json&retail_store_id=18406", new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {}

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("Fudi","Sucess " + responseBody.toString());
                String res = new String(responseBody);
                Log.i("fudi",res);
                JSONObject jsonobject = null;
                try {
                    jsonobject = new JSONObject(res);
                    String priceString = jsonobject.getJSONObject("current_price").getString("price");
                    Double price = Double.parseDouble(priceString);
                    if(price!=null && price>0 && price<100)
                    setPrice(price);
                    String itemGroup = jsonobject.getString("item_group");
                    setItemGroup(itemGroup);
                    Log.i("fudi", "Price:" + price + "ItemGroup" + itemGroup);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("fudi", "JSON Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("Fudi","Failure " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {}
        });
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }
}
