package com.example.patrick.grocr2;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ThankYou extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);
        getWindow().getDecorView().setBackgroundColor(Color.rgb(52,174,148));
        TextView thanx = (TextView) findViewById(R.id.thankyou);
        thanx.setTextColor(Color.WHITE);
        thanx.setTextSize(45);
    }
}
