package com.example.patrick.grocr2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.TextView;

public class shoppingBasket extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_basket);


        TextView numOfItems = (TextView) findViewById(R.id.numOfItems);
        TextView totalPrice = (TextView) findViewById(R.id.totalPrice);

        App globalApp = (App) getApplicationContext();
        int elements = 0;
        double sum=0;
        for (Product p: globalApp.currentOrderProducts){
            elements++;
            sum+=p.price;
        }
        totalPrice.setText("Fr. "+sum);

      }
}
