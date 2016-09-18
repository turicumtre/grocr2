package com.example.patrick.grocr2;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
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

import com.google.android.gms.contextmanager.internal.InterestUpdateBatchImpl;
import com.google.android.gms.location.LocationListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
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
    Orders order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        Button checkoutButton = (Button) findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ProductBrowser.this, shoppingBasket.class);
                startActivity(myIntent);
            }
        });

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
    sendOrderToServer(); //Todo löschen
    }


    public void sendOrderToServer() {

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);


        double longi = 47;
        double lati = 8.374;
        String deliverytime = "16:30";
        boolean refugee = true;
        int accepted = 0;
        int id = 0;
        int account = 0;
        ArrayList<Float> pk = new ArrayList<>();
        for (Product p : products) {
            Log.i("fudi", "parse int: " + p.EAN);
            try {
                if (p.EAN != null)
                    pk.add(Float.parseFloat(p.EAN));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        order = new Orders(longi, lati, deliverytime, refugee, accepted, id, account, pk);
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
            tryPushToServer(order.getPostStringForServer());
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("DU STINKSCH","NACH SCHEISSE");
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

//    private class MyLocationListener implements LocationListener {
//
//        @Override
//        public void onLocationChanged(Location loc) {
//            editLocation.setText("");
//            pb.setVisibility(View.INVISIBLE);
//            Toast.makeText(
//                    getBaseContext(),
//                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
//                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
//            String longitude = "Longitude: " + loc.getLongitude();
//            Log.v(TAG, longitude);
//            String latitude = "Latitude: " + loc.getLatitude();
//            Log.v(TAG, latitude);
//
//        /*------- To get city name from coordinates -------- */
//            String cityName = null;
//            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
//            List<Address> addresses;
//            try {
//                addresses = gcd.getFromLocation(loc.getLatitude(),
//                        loc.getLongitude(), 1);
//                if (addresses.size() > 0) {
//                    System.out.println(addresses.get(0).getLocality());
//                    cityName = addresses.get(0).getLocality();
//                }
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
//                    + cityName;
//            editLocation.setText(s);
//        }

//
//
//        @Override
//        public void onProviderDisabled(String provider) {}
//
//        @Override
//        public void onProviderEnabled(String provider) {}
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {}
//    }

}
