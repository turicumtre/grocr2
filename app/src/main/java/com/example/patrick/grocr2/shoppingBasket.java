package com.example.patrick.grocr2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class shoppingBasket extends AppCompatActivity {
    App globalApp;
    String parameters=null;
    int currentId = 0;
    Button orderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_basket);

        TextView numOfItems = (TextView) findViewById(R.id.numOfItems);
        TextView totalPrice = (TextView) findViewById(R.id.totalPrice);
        orderButton = (Button) findViewById(R.id.orderButton);

        globalApp = (App) getApplicationContext();
        Integer elements = 0;
        Double sum=0.;

        if(globalApp.currentOrderProducts!=null&&globalApp.currentOrderProducts!=null){
            for (Product p: globalApp.currentOrderProducts){
                elements++;
                sum+=p.price;
            }
        }
        totalPrice.setText(sum.toString());
        numOfItems.setText(elements.toString());

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button)view;
                switch (button.getText().toString()){
                    case "Order":
                        sendOrderToServer();
                        button.setText("Request sent!");
                        button.setClickable(false);
                        new AsyncRead().execute();

                        break;
                    case "Confirm delivery":
                        new Async3().execute();
                        button.setText("Thank you ;)");
                        button.setClickable(false);

                        new AsyncAccept().execute();
                        break;

                }
            }
        });

      }

    class AsyncAccept extends AsyncTask<Void, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            tryPushToServer();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }


    protected void tryPushToServer ()
    {

        App globalApp = (App) getApplicationContext();

        String parameters = "id="+currentId;
        Log.v("POST",parameters);
        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;

        try
        {
            url = new URL("http://46.101.175.156/api/INSERT/accepted.php");
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

    protected void tryPushToServer (String parameters)
    {
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


            if( isNumeric(response.trim())){
                 currentId = Integer.parseInt(response.trim());
            }

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


    //READ FROM SERVER
    class AsyncRead extends AsyncTask<Void, Integer, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            Orders currOrder = null;



            while (!checkIfAccountNotNull()){
               // parseOrders();
                Log.v("background","anotherone");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Toast.makeText(getApplicationContext(), "Your order has been accepted", Toast.LENGTH_LONG).show();
            Log.v("yay", "yay");
            orderButton.setText("Confirm delivery");
            orderButton.setClickable(true);
        }
    }



    public boolean checkIfAccountNotNull(){

        Boolean toReturn = false;
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
            Log.i("responsea: ", response);


            isr.close();
            reader.close();

            double clongi,clati;
            String cdeliverytime;
            int cposttime,caccepted,cid,caccount,crefugeeint;
            boolean crefugee=false;
            ArrayList<Long> cpk = new ArrayList<>();

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
                cpk.add(row.getLong("pk1"));
                cpk.add(row.getLong("pk2"));
                cpk.add(row.getLong("pk3"));
                cpk.add(row.getLong("pk4"));
                cpk.add(row.getLong("pk5"));
                cpk.add(row.getLong("pk6"));
                cpk.add(row.getLong("pk7"));
                cpk.add(row.getLong("pk8"));
                cpk.add(row.getLong("pk9"));
                cpk.add(row.getLong("pk10"));


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

                if (cid == currentId && caccount != 0){
                    Log.v("DONE","DONE");
                    toReturn = true;
                }

            }

        }
        catch(IOException e)
        {
            // Error
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return toReturn;
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
        for (Product p : globalApp.currentOrderProducts) {
            try {
                if (p.EAN != null)
                    pk.add(Long.parseLong(p.EAN));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Orders order = new Orders(longi, lati, deliverytime, refugee, accepted, id, account, pk);
        parameters = order.getPostStringForServer();
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
            tryPushToServer(parameters);
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


    class Async2 extends AsyncTask<Void, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }

    class Async3 extends AsyncTask<Void, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            confirmDelivery();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }

    protected void confirmDelivery ()
    {

        App globalApp = (App) getApplicationContext();

        String parameters = "personid="+globalApp.id+"&orderid="+globalApp.currentOrder.id;
        Log.v("POST",parameters);
        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;

        try
        {
            url = new URL("http://46.101.175.156/api/INSERT/account.php");
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

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}
