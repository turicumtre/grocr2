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
import java.util.Iterator;

public class login extends AppCompatActivity {

    EditText password;
    EditText email;
    Button login;
    ArrayList<account> profiles = new ArrayList<account>();

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

        App globalApp = (App) getApplicationContext();
        globalApp.test++;

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


                    Intent intent = new Intent(login.this, MainActivity.class);
                    intent.putExtra("message", message);
                    startActivity(intent);
                }

            }
        }
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

