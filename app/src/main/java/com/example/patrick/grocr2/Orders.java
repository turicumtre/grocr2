package com.example.patrick.grocr2;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Orders implements Serializable {
    double longi,lati;
    String deliverytime;
    boolean refugee;
    int accepted,id,account;
    double totalvalue=0;
    //int posttime;9
    ArrayList<Long> pk = new ArrayList<>();
    Set<Product> products = new HashSet<>();

    public Orders (double longi,double lati,String deliverytime, boolean refugee, int accepted, int id, int account,ArrayList<Long> pk, Set<Product> products)
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
        this.products = products;
        computeTotalValue();
    }

    private void computeTotalValue() {
        double sum=0;
        for(Product p : products)
            sum+=p.price*p.amount;
        totalvalue=sum;
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
        for (Long prod_id : pk){
            if (i>10)
                continue;
            result+=  "&pk"+ i.toString() + "=" + prod_id;
            i++;
        }
        result+= "&account=" + account;
        result+= "&length="+pk.size();
        Log.i("fudi", "PsuhRequest: "+result);

        return result;
    }
}
