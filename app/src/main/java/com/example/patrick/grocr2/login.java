package com.example.patrick.grocr2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class login extends AppCompatActivity {

    EditText password;
    EditText email;
    Button login;
    ArrayList<account> profiles = new ArrayList<account>();
    Map<String, Product> nameToProduct= new HashMap<>();
    Map<String,Product> EANToProduct = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        login = (Button) findViewById(R.id.loginButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Async().execute();
            }
        });

        String[] example_EAN = new String[] {"9002975301268", "7610469295645", "2050000719073","5937","2050000771606","7640146943200","7614400005829","7640146944993", "7640146945020","5000159459228","7640113614829","7640146940315","7640146940414","7640146943200","7640146943231","7640146944887","7640146944993","7640146945020","7640146947185","7640146947192","9002975301558"};
        for (String EAN : Arrays.asList(example_EAN)){
            createProduct(EAN);
        }
    }


    class Async extends AsyncTask<Void, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            tryLogin();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.v("started","IT HAS BEGUN");
            for (int i = 0; i < profiles.size(); i++){




                Log.v("started","1:"+email.getText().toString()+" 2."+profiles.get(i).email);
                Log.v("started","1:"+password.getText().toString()+" 2."+profiles.get(i).password);

                String currentEmail =  email.getText().toString();
                String iteratorEmail = profiles.get(i).email.toString();
                String currentPwd = password.getText().toString();
                String iteratorPwd = profiles.get(i).password.toString();
                if(currentEmail.equals(iteratorEmail) && currentPwd.equals(iteratorPwd)  ){


                    String message = Integer.toString(profiles.get(i).id);


                    App globalApp = (App) getApplicationContext();
                    globalApp.id = Integer.toString(profiles.get(i).id);

                    globalApp.nameToProduct = nameToProduct;
                    globalApp.EANToProduct = EANToProduct;
                    Intent intent = new Intent(login.this, MainActivity.class);
                    intent.putExtra("message", message);
                    startActivity(intent);
                }

            }
        }
    }
    public void createProduct(final String EAN)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("HackZurich","mKw%VY<7.Yb8D!G-");
        final String storeID = "18406";
        //final String requestLink = "https://backend.scango.ch/api/v01/items/find-by-ean/?ean=9002975301268&format=json&retail_store_id=18406";
        final String requestLink = "https://backend.scango.ch/api/v01/items/find-by-ean/?ean="+EAN+"&format=json&retail_store_id="+storeID;
        Log.i("fudi", "Request link: "+requestLink);
        System.out.println(requestLink);
        client.get(requestLink, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {}
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("Fudi","Sucess " + responseBody.toString());
                String res = new String(responseBody);
                Log.i("fudi",res);
                JSONObject jsonobject = null;
                try {
                    jsonobject = new JSONObject(res);
                    String priceString = jsonobject.getJSONObject("current_price").getString("price");
                    Double price = Double.parseDouble(priceString);
                    String name = jsonobject.getString("name");
                    String itemGroup = jsonobject.getString("item_group");
                    Product product = new Product(name,EAN, price,itemGroup, 1);
                    nameToProduct.put(name, product);
                    EANToProduct.put(EAN,product);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("fudi", "JSON Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("Fudi","Failure " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                System.out.println("Retry");
            }
        });

    }


    protected void tryLogin ()
    {
        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
        String parameters = "email="+email+"&password="+password;

        Log.i("parameters: ", parameters);

        try
        {
            url = new URL("http://46.101.175.156/api/GET/accounts.php");
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

            int cId,cScore;
            String cFirst,cLast,cEmail,cPassword;
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
                cFirst = row.getString("firstname");
                cLast = row.getString("lastname");
                cEmail = row.getString("email");
                cScore = row.getInt("score");
                cPassword = row.getString("password");
                cId = row.getInt("id");

                account currentAccount = new account(cFirst,cLast,cEmail,cPassword,cScore,cId);
                profiles.add(currentAccount);
            }

            int asdf = Log.v("asdf", String.valueOf(profiles.size()));
        }
        catch(IOException e)
        {
            // Error
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

