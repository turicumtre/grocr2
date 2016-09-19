package com.example.patrick.grocr2;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class login extends AppCompatActivity {

    EditText password;
    EditText email;
    Button login;
    ArrayList<account> profiles = new ArrayList<account>();
    Map<String, Product> nameToProduct= new HashMap<>();
    Map<String,Product> EANToProduct = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        List<String> sampleAdresses = new ArrayList<>();
        sampleAdresses.add("patrick@grocr.com");
        sampleAdresses.add("cornelia@grocr.com");
        sampleAdresses.add("philipp@grocr.com");
        Random randomizer = new Random();
        int random = randomizer.nextInt();
        if (random<0)
            random=-random;

        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        login = (Button) findViewById(R.id.loginButton);
        login.setBackgroundColor(Color.rgb(52,174,148));
        login.setTextColor(Color.WHITE);

        email.setText(sampleAdresses.get(random%2));
        password.setText("d8g7ddsf28s");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    }

