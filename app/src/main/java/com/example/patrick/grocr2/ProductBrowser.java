package com.example.patrick.grocr2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProductBrowser extends AppCompatActivity {
    TableLayout tableLayout;
    Set<Product> currentSearchResult = new HashSet<>();
    Set<Product> chosenProducts = new HashSet<>();
    Map<View, Product> viewToProduct = new HashMap<>();
    EditText searchbar;
    App globalApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_browser);
        searchbar = (EditText) findViewById(R.id.searchbar);

        globalApp = (App) getApplicationContext();
        tableLayout = (TableLayout) findViewById(R.id.productTableLayout);
        searchbar.setHint("Looking for something in particular?");


        Button checkoutButton = (Button) findViewById(R.id.checkoutButton);
        checkoutButton.setBackgroundColor(Color.rgb(52,174,148));
        checkoutButton.setTextColor(Color.WHITE);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrder();
                Intent myIntent = new Intent(ProductBrowser.this, shoppingBasket.class);
                startActivity(myIntent);
            }
        });

        searchbar.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                String searchString = searchbar.getText().toString().toLowerCase();
                if(searchString.length()>=2){
                    currentSearchResult = new HashSet<>();
                    for (Product p : globalApp.allProducts){
                        if (p.name.toLowerCase().contains(searchString))
                            currentSearchResult.add(p);
                    }
                    refreshTable();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });


    }

    @Override
    protected void onResume(){
        super.onResume();
        currentSearchResult = new HashSet<>();
        currentSearchResult.addAll(globalApp.initialProducts);
        chosenProducts = new HashSet<>();
        refreshTable();
    }


    public void refreshTable() {
        tableLayout.removeAllViews();
        for (Product p:currentSearchResult){
            TableRow row = new TableRow(this);
            TextView name = new TextView(this);
            //TextView price = new TextView(this);
            String priceString = " (Fr. " + Double.toString(p.price)+")";
            name.setText(p.name+priceString);
            name.setMinimumHeight(90);
            name.setMinWidth(1000);
            //price.setText();
            row.addView(name);
            // row.addView(price);
            tableLayout.addView(row);
            viewToProduct.put(name, p);
            if(chosenProducts.contains(viewToProduct.get(name)))
                row.setBackgroundColor(Color.LTGRAY);
            else
                row.setBackgroundColor(Color.WHITE);
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
        globalApp.currentOrder = new Orders(longi, lati, deliverytime, refugee, accepted, id, account, pk, chosenProducts);
    }
}
