package com.example.patrick.grocr2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;

    ArrayList<Orders> orders = new ArrayList<Orders>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(47.376887,8.541694) , 14.0f) );

        new AsyncMap().execute();
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
       String title = marker.getTitle();
        int tag = (int) marker.getTag();

        Intent intent = new Intent(MapsActivity.this, ShoppingList.class);
        intent.putExtra("message", tag);
        startActivity(intent);
    }

    class AsyncMap extends AsyncTask<Void, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            parseOrders();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            for (int i = 0; i < orders.size(); i++){

                Log.v("pk",String.valueOf(orders.get(i).pk));

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(orders.get(i).longi,  orders.get(i).lati))
                        .title(orders.get(i).pk.size() + " Products, delivery time: "+orders.get(i).deliverytime));

                marker.setTag(orders.get(i).id);
            }

        }
    }

    public void parseOrders(){
        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;

        try
        {
            url = new URL("http://46.101.175.156/api/GET/orders.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(10000);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
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

            double clongi,clati;
            String cdeliverytime;
            int cposttime,caccepted,cid,caccount,crefugeeint;
            boolean crefugee=false;
            ArrayList<Float> cpk = new ArrayList<>();

            JSONArray array = new JSONArray(response);

            for (int i = 0; i < array.length(); i++) {
                JSONObject row = array.getJSONObject(i);

                clongi = row.getDouble("longitude");
                clati = row.getDouble("latitude");


                cdeliverytime = row.getString("deliverytime");

                crefugeeint = row.getInt("refugeeflag");
                Log.i("longi", String.valueOf(clati));
                //cposttime = row.getInt("posttime");

                caccepted = row.getInt("accepted");
                cid = row.getInt("id");
                caccount = row.getInt("account");


               //add all products
                cpk.add((float)row.getInt("pk1"));
                cpk.add((float)row.getInt("pk2"));
                cpk.add((float)row.getInt("pk3"));
                cpk.add((float)row.getInt("pk4"));
                cpk.add((float)row.getInt("pk5"));
                cpk.add((float)row.getInt("pk6"));
                cpk.add((float)row.getInt("pk7"));
                cpk.add((float)row.getInt("pk8"));
                cpk.add((float)row.getInt("pk9"));
                cpk.add((float)row.getInt("pk10"));


                Log.v("Entire",String.valueOf(cpk));
                //check if all products actually exist. If not delete from arraylist
                for (int x = 9; x >= 0;x--){
                    if (cpk.get(x)==0){
                        cpk.remove(x);
                    }
                }

                if (crefugeeint >0){
                    crefugee = true;
                }

                ArrayList<Long> dummy = new ArrayList<>();
                dummy = (ArrayList<Long>)cpk.clone();
                Orders currentOrder = new Orders(clongi,clati,cdeliverytime,crefugee,caccepted,cid,caccount,dummy);
                orders.add(currentOrder);
                cpk.clear();
            }

        }
        catch(IOException e)
        {
            // Error
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
