package com.example.patrick.grocr2;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by philipp on 17.09.16.
 */
public class Orders implements Serializable {
    double longi,lati;
    String deliverytime;
    boolean refugee;
    int accepted,id,account;
    //int posttime;
    ArrayList<Float> pk = new ArrayList<>();

    public Orders (double longi,double lati,String deliverytime, boolean refugee, int accepted, int id, int account,ArrayList<Float> pk)
    {
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

    public String getPostStringForServer(){
        String result="";
        result+= "longitude=" + longi + "&";
        result+= "latitude=" + lati + "&";
        result+= "deliverytime=" + deliverytime + "&";
        result+= "refugeeflag=" + (refugee?"1":"0");
        Integer i = 1;
        for (Float prod_id : pk){
            result+=  "&pk"+ i.toString() + "=" + prod_id;
            i+=1;
        }
        result+= "&account=" + account;
        result+= "&length="+pk.size();
        Log.i("fudi", "PsuhRequest: "+result);

        return result;
    }
}
