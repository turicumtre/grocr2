package com.example.patrick.grocr2;

import android.content.Intent;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        String userId = bundle.getString("message");

        if (userId == null) {
            userId = "extra not set";
        }

        Log.v("id",userId);

        Button order = (Button) findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, ProductBrowser.class);
                startActivity(myIntent);
            }
        });

        Button collect = (Button) findViewById(R.id.collect);
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(myIntent);
            }
        });

        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, login.class);
                startActivity(myIntent);
            }
        });

        Button shoppingBasketButton = (Button) findViewById(R.id.shoppingBasketButton);
        shoppingBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, shoppingBasket.class);
                startActivity(myIntent);
            }
        });
    }
}
