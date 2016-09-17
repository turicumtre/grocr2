package com.example.patrick.grocr2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.TextView;

public class shoppingBasket extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_basket);


        TextView numOfItems = (TextView) findViewById(R.id.numOfItems);
        TextView totalPrice = (TextView) findViewById(R.id.totalPrice);
        int num = 0;
        double prices = 0;

        //get array of object items

//        JSONArray productsList = (JSONArray) findViewById(R.id.productsList);
//
//        String name = "";
//        Double price = 0.0;
//
//        ArrayList<String> listItems=new ArrayList<String>();
//
//        for (int i = 0; i < productsList.length(); i++) {
//
//            JSONObject row = null;
//            try {
//                row = productsList.getJSONObject(i);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                name = row.getString("name");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            try {
//                price = row.getDouble("price");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            listItems.add(i,name + " "+" "+ price);
//            num = num + 1;
//            prices = prices + price;
//        }
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, listItems);
//        ListView listview;
//        listview = (ListView) findViewById(R.id.orderList);
//        listview.setAdapter(adapter);
//
//        numOfItems.setText(num);
//        totalPrice.setText(String.valueOf(prices));
    }
}
