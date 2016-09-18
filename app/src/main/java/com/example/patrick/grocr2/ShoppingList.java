package com.example.patrick.grocr2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.scandit.barcodepicker.BarcodePicker;
import com.scandit.barcodepicker.OnScanListener;
import com.scandit.barcodepicker.ScanSession;
import com.scandit.barcodepicker.ScanSettings;
import com.scandit.barcodepicker.ScanditLicense;
import com.scandit.recognition.Barcode;

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

import android.Manifest;

import android.content.pm.PackageManager;
import android.os.Build;

import android.view.WindowManager;

import com.scandit.recognition.SymbologySettings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class ShoppingList extends Activity implements OnScanListener {

    ArrayList<Orders> orders = new ArrayList<Orders>();
    ArrayList<String> rowtoGrey = new ArrayList<>();
    ArrayList<String> scannedEan = new ArrayList<>();
    TableLayout tableLayout ;

    int totalworth = 0;
    int percentage = 0;

    HashMap<Integer, Long> meMap;
    HashMap<Long, Integer> meMapSwitch;

    // SCANDIT
    // The main object for recognizing a displaying barcodes.
    private BarcodePicker mBarcodePicker;
    private final int CAMERA_PERMISSION_REQUEST = 0;
    private boolean mDeniedCameraAccess = false;
    private RelativeLayout scanner;
    private Button scan;
    // Enter your Scandit SDK App key here.
    // Your Scandit SDK App key is available via your Scandit SDK web account.
    public static final String sScanditSdkAppKey = "UW7JbTjpkUqEPJlAj9Fiv4TMAgFI+nyQQa5jNIaaNps";
    private boolean mPaused = true;
    Toast mToast = null;
    int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        meMap = new HashMap<Integer, Long>();
        meMapSwitch = new HashMap<Long, Integer>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        scanner = (RelativeLayout) findViewById(R.id.scannview);
        scanner.setVisibility(View.GONE);
        scanner.setTranslationZ(10);

        tableLayout = (TableLayout) findViewById(R.id.glutz);

        Bundle bundle = getIntent().getExtras();
        orderId = bundle.getInt("message");
       
        new AsyncShopping().execute();

        ScanditLicense.setAppKey(sScanditSdkAppKey);

        Button scan =(Button) findViewById(R.id.button);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize and start the bar code recognition.
                initializeAndStartBarcodeScanning();
                mBarcodePicker.startScanning();
            }
        });

        Button acceptbtn = (Button) findViewById(R.id.button2);
        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Async().execute();
                new AsyncRead().execute();
            }
        });

    }

    //Scandit Stuff
    /**
     * Initializes and starts the bar code scanning.
     */
    public void initializeAndStartBarcodeScanning() {
        // Switch to full screen.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);


        // The scanning behavior of the barcode picker is configured through scan
        // settings. We start with empty scan settings and enable a very generous
        // set of symbologies. In your own apps, only enable the symbologies you
        // actually need.
        ScanSettings settings = ScanSettings.create();
        int[] symbologiesToEnable = new int[] {
                Barcode.SYMBOLOGY_EAN13,
                Barcode.SYMBOLOGY_EAN8,
                Barcode.SYMBOLOGY_UPCA,
                Barcode.SYMBOLOGY_DATA_MATRIX,
                Barcode.SYMBOLOGY_QR,
                Barcode.SYMBOLOGY_CODE39,
                Barcode.SYMBOLOGY_CODE128,
                Barcode.SYMBOLOGY_INTERLEAVED_2_OF_5,
                Barcode.SYMBOLOGY_UPCE
        };
        for (int sym : symbologiesToEnable) {
            settings.setSymbologyEnabled(sym, true);
        }


        // Some 1d barcode symbologies allow you to encode variable-length data. By default, the
        // Scandit BarcodeScanner SDK only scans barcodes in a certain length range. If your
        // application requires scanning of one of these symbologies, and the length is falling
        // outside the default range, you may need to adjust the "active symbol counts" for this
        // symbology. This is shown in the following few lines of code.

        SymbologySettings symSettings = settings.getSymbologySettings(Barcode.SYMBOLOGY_CODE39);
        short[] activeSymbolCounts = new short[] {
                7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20
        };
        symSettings.setActiveSymbolCounts(activeSymbolCounts);
        // For details on defaults and how to calculate the symbol counts for each symbology, take
        // a look at http://docs.scandit.com/stable/c_api/symbologies.html.



        // prefer the back-facing camera, is there is any.
        settings.setCameraFacingPreference(ScanSettings.CAMERA_FACING_BACK);


        // Some Android 2.3+ devices do not support rotated camera feeds. On these devices, the
        // barcode picker emulates portrait mode by rotating the scan UI.
        boolean emulatePortraitMode = !BarcodePicker.canRunPortraitPicker();
        if (emulatePortraitMode) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        BarcodePicker picker = new BarcodePicker(this, settings);

        scanner.addView(picker);
        scanner.setVisibility(View.VISIBLE);

        //setContentView(picker);
        mBarcodePicker = picker;

        // Register listener, in order to be notified about relevant events
        // (e.g. a successfully scanned bar code).
        mBarcodePicker.setOnScanListener(this);

    }

    public void didScan(ScanSession session) {
        String message = "";
        for (Barcode code : session.getNewlyRecognizedCodes()) {
            String data = code.getData();
            // truncate code to certain length
            String cleanData = data;
            if (data.length() > 30) {
                cleanData = data.substring(0, 25) + "[...]";
            }
            if (message.length() > 0) {
                message += "\n\n\n";
            }
            message += cleanData;
            message += "\n\n(" + code.getSymbologyName().toUpperCase(Locale.US) + ")";
            scannedEan.add(data);

            Set<String> hs = new HashSet<>();
            hs.addAll(scannedEan);
            scannedEan.clear();
            scannedEan.addAll(hs);




            for(int i = 0, j = tableLayout.getChildCount(); i < j; i++) {
                final View view = tableLayout.getChildAt(i);
                if (view instanceof TableRow) {
                    // then, you can remove the the row you want...
                    // for instance...
                    TableRow row = (TableRow) view;

                    //get pk from tag of row
                    Integer rowtag = (Integer) row.getTag();
                    Long pkgfromRow = meMap.get(rowtag);

                    //Log.i("memapvals", String.valueOf(meMap.values()));

                  /*  Log.w("Potato",String.valueOf(scannedEan));
                    Log.w("Potato","length "+String.valueOf(scannedEan.size()));

                    for (int a = 0; a < 1; a++){
                        Log.w("Potato","yeshh");
                        if (scannedEan.get(a).equals("7640146941626")){
                            Log.w("yep","done");
                        }

                    }*/


                    /*
                    if( scannedEan.contains("7640146941626") ) {
                        /*Log.i("Success","yess");

                        int tag = (int) row.getTag();
                        int togrey = meMapSwitch.get(pkgfromRow);

                        Log.v("tag"," row "+tag+" togrey "+ togrey);*/
                        //if (tag == togrey) {
                        /*Log.v("scannerd", String.valueOf(pkgfromRow));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    view.setBackgroundColor(Color.LTGRAY);
                                }
                            });
                        }
                    }
*/
                    /*Integer rowtag = (int) row.getTag();
                    Float pkgfromRow = meMap.get(rowtag);
                    // float pkfromRow = (Float) meMap.get(row.getTag());

                    Log.i("pkfromRow", String.valueOf(pkgfromRow));
                    Log.i("memapvals", String.valueOf(meMap.values()));

                    if( meMap.values().contains(pkgfromRow) ) {
                        view.setBackgroundColor(Color.LTGRAY);
                    }*/
                }
            }

        }
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        mToast.show();


    }

    @Override
    protected void onPause() {
        // When the activity is in the background immediately stop the
        // scanning to save resources and free the camera.
        if (mBarcodePicker != null) {
            mBarcodePicker.stopScanning();
        }

        mPaused = true;
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mPaused = false;

        if (mBarcodePicker != null) {
            // handle permissions for Marshmallow and onwards...
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                grantCameraPermissionsThenStartScanning();
            } else {
                // Once the activity is in the foreground again, restart scanning.
                mBarcodePicker.startScanning();
            }
        }
    }
    @Override
    public void onBackPressed() {

        if (mBarcodePicker != null) {
            mBarcodePicker.stopScanning();
        }
        if (scanner.getVisibility() == View.VISIBLE){
            scanner.setVisibility(View.GONE);
        }else {
            finish();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void grantCameraPermissionsThenStartScanning() {
        if (this.checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (mDeniedCameraAccess == false) {
                // it's pretty clear for why the camera is required. We don't need to give a
                // detailed reason.
                this.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_REQUEST);
            }

        } else {
            // we already have the permission
            mBarcodePicker.startScanning();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mDeniedCameraAccess = false;
                if (!mPaused) {
                    mBarcodePicker.startScanning();
                }
            } else {
                mDeniedCameraAccess = true;
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    //AsyncTask
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

            Log.i("done","post");
            refreshTable();

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

    public void refreshTable() {
        tableLayout.removeAllViews();

        //Get all packages
        Orders correctOrder = null;

        for (int i = 0; i < orders.size(); i++){
            if (orders.get(i).id == orderId){
                correctOrder = orders.get(i);
            }
        }

        for (int x = 0; x < correctOrder.pk.size(); x++){

            meMap.put( x,correctOrder.pk.get(x));
            meMapSwitch.put(correctOrder.pk.get(x),x);

            String  ean = String.valueOf(correctOrder.pk.get(x));
            App globalApp = (App) getApplicationContext();

            Product currentProduct = globalApp.EANToProduct.get(ean);
            String nameString = currentProduct.name;
            String priceString = String.valueOf(currentProduct.price)+" CHF";

            totalworth += currentProduct.price;

            Log.v("worth",String.valueOf(totalworth));
            TableRow row = new TableRow(this);
            row.setTag(x);

            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    view.setBackgroundColor(Color.LTGRAY);
                }
            });

            TextView name = new TextView(this);
            TextView price = new TextView(this);


            price.setTextSize(TypedValue.COMPLEX_UNIT_SP , 24);
            price.setTextAppearance(android.R.style.TextAppearance_Medium);
            name.setTextSize(TypedValue.COMPLEX_UNIT_SP , 24);
            name.setTextAppearance(android.R.style.TextAppearance_Medium);

            name.setText(nameString);
            price.setText(priceString);
            row.addView(name);
            row.addView(price);
            tableLayout.addView(row);
        }

        TextView deadline = (TextView) findViewById(R.id.deadline);
        TextView earnings = (TextView) findViewById(R.id.earnings);

        int cashInt = totalworth / 10;

        if (cashInt == 0){
            cashInt = 1;
        }

        percentage = cashInt;
        String cash = String.valueOf(cashInt);
        earnings.setText(cash+" CHF");
        deadline.setText(correctOrder.deliverytime);

        Log.v("Hashmap", String.valueOf(meMap));
        Log.v("HashmapSwitch", String.valueOf(meMapSwitch));
    }


 //
//    }

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



            while (!checkIfAccepted()){
                parseOrders();
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

            Toast.makeText(getApplicationContext(), "Congratulations you have just earned "+percentage + " CHF", Toast.LENGTH_LONG).show();
            Log.v("yay", "yay");

        }
    }


    public boolean checkIfAccepted(){

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

                if (cid == orderId && caccepted == 1){
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

    //WRITE TO SERVER
    class Async extends AsyncTask<Void, Integer, String>
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

        String parameters = "personid="+globalApp.id+"&orderid="+ orderId;
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


}
