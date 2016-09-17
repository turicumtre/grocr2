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

        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, login.class);
                startActivity(myIntent);
            }
        });

        getRequest();
    }

    private void getRequest() {
        final AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("HackZurich","mKw%VY<7.Yb8D!G-");
//        client.get("https://backend.scango.ch/api/v01/items/find-by-ean/?ean=9002975301268&format=json&retail_store_id=18406", new AsyncHttpResponseHandler() {
        client.get("https://backend.scango.ch/api/v01/items/", new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("Fudi","Sucess " + responseBody.toString());
                String res = new String(responseBody);
                Log.i("fudi",res);
                try {
                    JSONObject jsonobject = new JSONObject(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("Fudi","Failure " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }
}
