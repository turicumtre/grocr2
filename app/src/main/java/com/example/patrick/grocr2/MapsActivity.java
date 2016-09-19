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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    App globalApp;
    Random randomizer=null;
    Set<Orders> realAndDummyOrderS=new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        randomizer = new Random();
        globalApp = (App) getApplicationContext();
        realAndDummyOrderS.addAll(globalApp.ordersList);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        generateOrdersFromOtherUsers();
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(47.376887,8.541694) , 14.0f) );
        mMap.setOnInfoWindowClickListener(this);

        for (Orders o:realAndDummyOrderS) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(o.longi, o.lati))
                    .title(o.products.size()+" Products, delivery time: " + o.deliverytime));

            marker.setTag(o);
        }
    }

    private void generateOrdersFromOtherUsers() {
        int amountOfOrders = computeRandomInt(1,7);
        for (int i=0;i<amountOfOrders;i++){
            Set<Product> products = new HashSet<>();
            for(int j=0;j<computeRandomInt(3,10);j++){
                int tmp = computeRandomInt(1,globalApp.initialProducts.size()-1);
                products.add(globalApp.initialProducts.get(tmp));
            }
            for(int j=0;j<computeRandomInt(1,5);j++){
                int tmp = computeRandomInt(20,globalApp.allProducts.size()-1);
                products.add(globalApp.allProducts.get(tmp));
            }
            double offset1 = Math.random()/100;
            double offset2 = Math.random()/100;
            if(Math.random()>0.5)
                offset1=-offset1;
            if(Math.random()>0.5)
                offset2=-offset2;
            double longi = 47.3802+offset1;
            double lati = 8.5404+offset2;
            String deliveryTime = String.valueOf(computeRandomInt(9,20))+" : "+String.valueOf(computeRandomInt(2,11)*5);
            Orders o = new Orders(longi,lati,deliveryTime,false,0,0,0,null,products);
            realAndDummyOrderS.add(o);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        globalApp.OrderChosenFromMap = (Orders) marker.getTag();

        Intent intent = new Intent(MapsActivity.this, ShoppingList.class);
        startActivity(intent);
    }

    private int computeRandomInt(int min, int max){
        int tmp = randomizer.nextInt();
        tmp = tmp<0?-tmp:tmp;
        tmp=tmp%(max-min);
        return min+tmp+1;
    }

}
