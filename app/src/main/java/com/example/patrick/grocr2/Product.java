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

    Product(String EAN) {
        final AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("HackZurich","mKw%VY<7.Yb8D!G-");

        final String storeID = "23303";
        client.get("https://backend.scango.ch/api/v01/items/find-by-ean/?ean="+EAN+"&format=json&retail_store_id="+storeID, new AsyncHttpResponseHandler() {

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
                    String price = jsonobject.getJSONObject("pageInfo").getString("pageName");

                } catch (JSONException e) {
                    e.printStackTrace();
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
}
