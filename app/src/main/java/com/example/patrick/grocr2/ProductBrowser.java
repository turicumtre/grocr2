package com.example.patrick.grocr2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

public class ProductBrowser extends AppCompatActivity {
    String[] example_EAN;
    TableLayout tableLayout;
    Set<Product> currentSearchResult = new HashSet<>();
    Set<Product> chosenProducts = new HashSet<>();
    Map<View, Product> viewToProduct = new HashMap<>();
    Map<String, Product> nameToProduct= new HashMap<>();
    int remaining=0;
    App globalApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final EditText searchbar = (EditText) findViewById(R.id.searchbar);

        globalApp = (App) getApplicationContext();
        nameToProduct = globalApp.nameToProduct;
        setSupportActionBar(toolbar);
        tableLayout = (TableLayout) findViewById(R.id.productTableLayout);

        searchbar.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                currentSearchResult = new HashSet<Product>();
                for (String productName : nameToProduct.keySet()){
                    if (productName.toLowerCase().contains(searchbar.getText().toString().toLowerCase()))
                        currentSearchResult.add(nameToProduct.get(productName));
                    refreshTable();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        Button checkoutButton = (Button) findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrder();
                globalApp.nameToProduct = nameToProduct;
                Intent myIntent = new Intent(ProductBrowser.this, shoppingBasket.class);
                startActivity(myIntent);
                }
        });

    }

    public void refreshTable() {
        tableLayout.removeAllViews();
        for (Product p:currentSearchResult){
            TableRow row = new TableRow(this);
            Button name = new Button(this);
            TextView price = new TextView(this);
            name.setText(p.name);
            price.setText("  Fr. " + Double.toString(p.price));
            row.addView(name);
            row.addView(price);
            tableLayout.addView(row);
            viewToProduct.put(name, p);
            if(chosenProducts.contains(viewToProduct.get(name)))
                name.setBackgroundColor(Color.LTGRAY);
            else
                name.setBackgroundColor(Color.WHITE);
            name.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    if(!chosenProducts.contains(viewToProduct.get(v))){
                        v.setBackgroundColor(Color.LTGRAY);
                        chosenProducts.add(viewToProduct.get(v));
                    }
                    else {
                    v.setBackgroundColor(Color.WHITE);
                    chosenProducts.remove(viewToProduct.get(v));
                }
                }
            });
        }
    }

    public void createOrder() {
        double offset1 = Math.random()/100;
        double offset2 = Math.random()/100;
        if(Math.random()>0.5)
            offset1=-offset1;
        if(Math.random()>0.5)
            offset2=-offset2;
        double longi = 47.3802+offset1;
        double lati = 8.5404+offset2;
        String deliverytime = "16:30";
        boolean refugee = true;
        int accepted = 0;
        int id = 0;
        int account = 0;
        ArrayList<Long> pk = new ArrayList<>();
        for (Product p : chosenProducts) {
            try {
                if (p.EAN != null)
                    pk.add(Long.parseLong(p.EAN));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        globalApp.currentOrderProducts = chosenProducts;
        globalApp.currentOrder = new Orders(longi, lati, deliverytime, refugee, accepted, id, account, pk);

    }
}
