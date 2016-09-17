package com.example.patrick.grocr2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class login extends AppCompatActivity {

    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getEmail = email.getText().toString();
                String getPassword = password.getText().toString();

                Log.i("email: ", getEmail);
                Log.i("passw: ", getPassword);

                /*tryLogin(getEmail, getPassword);*/

                Intent myIntent = new Intent(login.this, MainActivity.class);
                startActivity(myIntent);
            }
        });
    }


    protected void tryLogin extends AsyncTask<Void (String email, String password)
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
            Toast.makeText(this,"Message from Server: \n"+ response, Toast.LENGTH_SHORT).show();
            isr.close();
            reader.close();

        }
        catch(IOException e)
        {
            // Error
        }
    }
}
