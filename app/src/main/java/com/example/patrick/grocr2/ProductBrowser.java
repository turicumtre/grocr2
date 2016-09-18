package com.example.patrick.grocr2;

import android.content.Context;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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
    Button button;
    Map<String, Product> nameToProduct= new HashMap<>();
    int remaining=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final EditText searchbar = (EditText) findViewById(R.id.searchbar);

        App globalApp = (App) getApplicationContext();

        setSupportActionBar(toolbar);

        button = (Button) findViewById(R.id.searchb);

        example_EAN = new String[] {"9002975301268", "7610469295645", "2050000719073","5937","2050000771606","7640146943200","7614400005829","7640146944993", "7640146945020","5000159459228","7640113614829","7640146940315","7640146940414","7640146943200","7640146943231","7640146944887","7640146944993","7640146945020","7640146947185","7640146947192","9002975301558"};
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    refreshTable();
            }
        });
        remaining = example_EAN.length;

        tableLayout = (TableLayout) findViewById(R.id.productTableLayout);

        for (String EAN : Arrays.asList(example_EAN)){
            createProduct(EAN);
        }

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

    public void sendOrderToServer() {

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

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
        for (Product p : nameToProduct.values()) {
            Log.i("fudi", "parse int: " + p.EAN);
            try {
                if (p.EAN != null)
                    pk.add(Long.parseLong(p.EAN));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Orders order = new Orders(longi, lati, deliverytime, refugee, accepted, id, account, pk);
    new Async().execute();
    }
    class Async extends AsyncTask<Void, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            ;
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("de päddy stinkt","NACH SCHEISSE");
        }
    }

    protected void tryPushToServer (String parameters)
    {
        Log.v("POST",parameters);
        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;

        try
        {
            url = new URL("http://46.101.175.156/api/INSERT/orders.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(10000);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            // Response from server after login process will be stored in response variable.
            response = sb.toString();
            // You can perform UI operations here
            Log.i("response: ", response);


            isr.close();
            reader.close();
    } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void createProduct(final String EAN)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("HackZurich","mKw%VY<7.Yb8D!G-");
        final String storeID = "18406";
        //final String requestLink = "https://backend.scango.ch/api/v01/items/find-by-ean/?ean=9002975301268&format=json&retail_store_id=18406";
        final String requestLink = "https://backend.scango.ch/api/v01/items/find-by-ean/?ean="+EAN+"&format=json&retail_store_id="+storeID;
        Log.i("fudi", "Request link: "+requestLink);
        System.out.println(requestLink);
        client.get(requestLink, new AsyncHttpResponseHandler() {
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
                        String name = jsonobject.getString("name");
                        String itemGroup = jsonobject.getString("item_group");
                        nameToProduct.put(name, new Product(name,EAN, price,itemGroup, 1));
                        currentSearchResult.add(nameToProduct.get(name));
                        refreshTable();
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
                public void onRetry(int retryNo) {
                    System.out.println("Retry");
                }
            });

    }

}
