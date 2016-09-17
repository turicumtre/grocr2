package com.example.patrick.grocr2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class shoppingBasket extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_basket);

        //toolbar with back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_shopping_basket);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView numOfItems = (TextView) findViewById(R.id.numOfItems);
        TextView totalPrice = (TextView) findViewById(R.id.totalPrice);
        int num = 0;
        double prices = 0;

        //get array of object items

       /* JSONArray productsList = (JSONArray) findViewById(R.id.productsList);

        String name = "";
        Double price = 0.0;

        ArrayList<String> listItems=new ArrayList<String>();

        for (int i = 0; i < productsList.length(); i++) {

            JSONObject row = null;
            try {
                row = productsList.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                name = row.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                price = row.getDouble("price");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listItems.add(i,name + " "+" "+ price);
            num = num + 1;
            prices = prices + price;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, listItems);
        ListView listview;
        listview = (ListView) findViewById(R.id.orderList);
        listview.setAdapter(adapter);

        numOfItems.setText(num);
        totalPrice.setText(String.valueOf(prices));*/
    }
}
