package com.example.patrick.grocr2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

public class ShoppingList extends AppCompatActivity {

    ArrayList<Orders> orders = new ArrayList<Orders>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        Bundle bundle = getIntent().getExtras();
        int userId = bundle.getInt("message");


        Toast.makeText(this, userId +" window clicked",
                Toast.LENGTH_SHORT).show();

        new AsyncShopping().execute();
    }

    class AsyncShopping extends AsyncTask<Void, Integer, String>
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

            //POPULATE TABLE HERE (?)
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
            ArrayList<Integer> cpk = new ArrayList<>();

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
                cpk.add(row.getInt("pk1"));
                cpk.add(row.getInt("pk2"));
                cpk.add(row.getInt("pk3"));
                cpk.add(row.getInt("pk4"));
                cpk.add(row.getInt("pk5"));
                cpk.add(row.getInt("pk6"));
                cpk.add(row.getInt("pk7"));
                cpk.add(row.getInt("pk8"));
                cpk.add(row.getInt("pk9"));
                cpk.add(row.getInt("pk10"));


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

                ArrayList<Integer> dummy = new ArrayList<>();
                dummy = (ArrayList<Integer>)cpk.clone();
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
