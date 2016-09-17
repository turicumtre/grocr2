package com.example.patrick.grocr2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

public class ProductBrowser extends AppCompatActivity {
    Set<Product> products = new HashSet<>();
    TableLayout tableLayout;
    Set<Product> currentlyDisplayedProducts = new HashSet<>();
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        button = (Button) findViewById(R.id.searchb);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    refreshTable();
            }
        });

        for (String EAN : Arrays.asList(Product.example_EAN)){
            Product product = new Product(EAN);
            product.contactServer();
            products.add(product);
        }

        tableLayout = (TableLayout) findViewById(R.id.productTableLayout);
    }
    public void refreshTable() {
        tableLayout.removeAllViews();
        for (Product p:products){
            TableRow row = new TableRow(this);
            TextView name = new TextView(this);
            TextView price = new TextView(this);
            name.setText(p.name);
            price.setText(Double.toString(p.price));
            row.addView(name);
            row.addView(price);
            tableLayout.addView(row);
        }
    }


}
