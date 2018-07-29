package com.example.chathura.dcs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CheckLeaves extends AppCompatActivity {
    EditText NIC;
    Button okay;
    String nic;
    boolean nicOkay = true;
    boolean already = false;
    boolean done = false;
    boolean invalid = false;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_leaves);

        NIC = (EditText)findViewById(R.id.etNICCheck);
        okay = (Button)findViewById(R.id.btnOkayCheck);

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nic = NIC.getText().toString();
                if (nic.equals("")) {
                    Toast.makeText(CheckLeaves.this, "Please enter your NIC number", Toast.LENGTH_SHORT).show();
                } else {
                for (int i = 0; i < nic.length(); i++) {
                    char x = nic.charAt(i);
                    if (x == '0' || x == '1' || x == '2' || x == '3' || x == '4' || x == '5' || x == '6' || x == '7' || x == '8' || x == '9' || x == 'v') {
                    } else {
                        nicOkay = false;
                    }
                }
                if (nicOkay == true) {
                    alreadyHave(nic);
                } else {
                    Toast.makeText(CheckLeaves.this, "Please enter a valid nic number", Toast.LENGTH_SHORT).show();
                    NIC.setTextColor(0xFFFF0000);
                }
            }

            }
        });

        NIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NIC.setTextColor(0xff000000 );
            }
        });
    }

    public void alreadyHave(String x){
        new Already().execute(x);
    }

    private class Already extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(CheckLeaves.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tPlease Wait...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("http://10.0.2.2/Already.php");

            }
            catch (MalformedURLException e) {
                //TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("EmployeeId", params[0]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            }
            catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                if (response_code == HttpURLConnection.HTTP_OK) {

                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return(result.toString());

                }
                else{

                    return("unsuccessful");
                }

            }
            catch (IOException e) {
                e.printStackTrace();
                return "exception";
            }
            finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {


            pdLoading.dismiss();

            if(result.equalsIgnoreCase("true")) {
                already = true;
                done = true;
                Check();

            }
            else if (result.equalsIgnoreCase("false")){
                done = true;
                invalid = true;
                Toast.makeText(CheckLeaves.this, "Invalid NIC Number", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                done = true;
                Toast.makeText(CheckLeaves.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
            else if(result.equalsIgnoreCase("new")){
                Intent intent1 = new Intent(CheckLeaves.this, SendLeaves.class);
                intent1.putExtra("GetNICCheck" , nic);
                startActivity(intent1);
                CheckLeaves.this.finish();
            }
        }

    }

    public void Check(){
        if(already == true){
            buildDialog(CheckLeaves.this).show();
        }
        else{
            if(invalid == false) {
                Intent intent = new Intent(CheckLeaves.this, SendLeaves.class);
                intent.putExtra("GetNICCheck" , nic);
                startActivity(intent);
                CheckLeaves.this.finish();
            }
        }
    }

    public AlertDialog.Builder buildDialog(Context c){
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Already available a leave request");
        builder.setMessage("You already have a pending leave request. So press ok to exit.");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CheckLeaves.this.finish();
            }
        });
        return builder;
    }
}
