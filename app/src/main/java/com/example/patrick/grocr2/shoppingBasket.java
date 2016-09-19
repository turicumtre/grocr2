package com.example.patrick.grocr2;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class shoppingBasket extends AppCompatActivity {
    App globalApp;
    String parameters=null;
    TextView oderButtonNew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_basket);

        TextView numOfItems = (TextView) findViewById(R.id.numOfItems);
        TextView totalPrice = (TextView) findViewById(R.id.totalPrice);
        oderButtonNew = (TextView) findViewById(R.id.anfrageversendet);
        oderButtonNew.setText("Order");
        oderButtonNew.setBackgroundColor(Color.rgb(52,174,148));
        oderButtonNew.setTextColor(Color.WHITE);
        TextView details = (TextView) findViewById(R.id.stuff);

        globalApp = (App) getApplicationContext();
        Integer elements = 0;
        Double sum=0.;

        EditText minutes = (EditText)findViewById(R.id.editText3) ;
        EditText hours = (EditText)findViewById(R.id.editText4) ;
        minutes.setText("30");
        hours.setText("16");

        String s="";
        if(globalApp.currentOrder!=null){
            for (Product p: globalApp.currentOrder.products){
                elements++;
                sum+=p.price;
                s+= p.name+"\n";
            }
        }
        sum = Math.round(100*sum)/100.;
        totalPrice.setText(sum.toString());
        numOfItems.setText(elements.toString());
        details.setText(s);

        oderButtonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOrderToServer();
                globalApp.currentOrder=null;
                Intent intent = new Intent(shoppingBasket.this, ThankYou.class);
                startActivity(intent);
                oderButtonNew.setClickable(false);


            }
        });
      }

    @Override
    protected void onResume() {
        super.onResume();
        if (globalApp.currentOrder==null) {
            oderButtonNew.setText("Thank you for your order");
            oderButtonNew.setBackgroundColor(Color.rgb(250,250,250));
            oderButtonNew.setTextColor(Color.rgb(52,174,148));
        }
    }

    public void sendOrderToServer() {
        double offset1 = Math.random()/100;
        double offset2 = Math.random()/100;
        if(Math.random()>0.5)
            offset1=-offset1;
        if(Math.random()>0.5)
            offset2=-offset2;
        double longi = 47.3802+offset1;
        double lati = 8.5404+offset2;
        EditText minutes = (EditText)findViewById(R.id.editText3) ;
        EditText hours = (EditText)findViewById(R.id.editText4) ;

        String min = minutes.getText().toString();
        String hour = hours.getText().toString();

        if (min.matches("")) {
            min = "30";
        }
        if (hour.matches("")) {
            hour = "16";
        }

        String deliverytime = hour+" : "+ min;
        boolean refugee = true;
        int accepted = 0;
        int id = 0;
        int account = 0;
        ArrayList<Long> pk = new ArrayList<>();
        for (Product p : globalApp.currentOrder.products) {
            try {
                if (p.EAN != null)
                    pk.add(Long.parseLong(p.EAN));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Orders order = new Orders(longi, lati, deliverytime, refugee, accepted, id, account, pk, globalApp.currentOrder.products);
        parameters = order.getPostStringForServer();
        globalApp.ordersList.add(order);
    }
}
