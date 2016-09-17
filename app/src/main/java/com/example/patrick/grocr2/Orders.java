package com.example.patrick.grocr2;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by philipp on 17.09.16.
 */
public class Orders {
    double longi,lati;
    String deliverytime;
    boolean refugee;
    int accepted,id,account;
    //int posttime;
    ArrayList<Integer> pk = new ArrayList<>();

    public Orders (double longi,double lati,String deliverytime, boolean refugee, int accepted, int id, int account,ArrayList<Integer> pk){
        this.longi = longi;
        this.lati = lati;
        this.deliverytime = deliverytime;
        this.refugee = refugee;
        //this.posttime = posttime;
        this.accepted = accepted;
        this.id = id;
        this.account = account;
        this.pk = pk;

    }
    public String  getData(){
        String asdf ="longitude "+longi+" latitide "+ lati+" deliverytime "+deliverytime;
        return asdf;
    }

}
