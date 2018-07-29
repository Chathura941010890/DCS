package com.example.chathura.dcs;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
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

public class PassResetAthor extends AppCompatActivity {
    EditText oldPW;
    Button Check;
    EditText newPW;
    EditText confPW;
    Button Reset;
    String opw;
    String npw;
    String cpw;
    String CurrentUN;
    String CurrentPW;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_reset_athor);

        Check = (Button)findViewById(R.id.btnCheckA);
        Reset = (Button)findViewById(R.id.btnResetA);
        oldPW = (EditText)findViewById(R.id.etOldA);
        newPW = (EditText)findViewById(R.id.etNewA);
        confPW = (EditText)findViewById(R.id.etConfA);
        CurrentUN = getIntent().getExtras().getString("CheckUN");
        CurrentPW = getIntent().getExtras().getString("CheckPW");

        Reset.setEnabled(false);
        newPW.setEnabled(false);
        confPW.setEnabled(false);

        Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opw = oldPW.getText().toString();

                new PassResetAthor.AsyncCheck().execute(opw,CurrentUN,CurrentPW);
            }
        });

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                npw = newPW.getText().toString();
                cpw = confPW.getText().toString();

                if (npw.equals(cpw)) {
                    new PassResetAthor.AsyncReset().execute(CurrentUN , npw , cpw);
                }
                else{
                    Toast.makeText(PassResetAthor.this , "Entered passwords are not matched." , Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private class AsyncCheck extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(PassResetAthor.this);
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
                url = new URL("http://10.0.2.2/PasswordCheck.php");

            }
            catch (MalformedURLException e) {
                // TODO Auto-generated catch block
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
                        .appendQueryParameter("EnteredOld", params[0])
                        .appendQueryParameter("Username", params[1])
                        .appendQueryParameter("CameOld", params[2]);
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

            if(result.equalsIgnoreCase("Passwords_Matched"))
            {
                Reset.setEnabled(true);
                newPW.setEnabled(true);
                confPW.setEnabled(true);

                Toast.makeText(PassResetAthor.this , "Passwords matched. Now you can enter and reset your password." , Toast.LENGTH_LONG).show();


            }
            else if (result.equalsIgnoreCase("Passwords_Not_Matched")){

                Toast.makeText(PassResetAthor.this, "Enter your correct old password", Toast.LENGTH_LONG).show();

            }
            else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(PassResetAthor.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }

    }


    private class AsyncReset extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(PassResetAthor.this);
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
                url = new URL("http://10.0.2.2/PasswordReset.php");

            }
            catch (MalformedURLException e) {
                // TODO Auto-generated catch block
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
                        .appendQueryParameter("Username", params[0])
                        .appendQueryParameter("NewPW", params[1])
                        .appendQueryParameter("ConfPW", params[2]);
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

            if(result.equalsIgnoreCase("Password_Reset"))
            {
                Toast.makeText(PassResetAthor.this , "Your password was reset. So you have to login again with new password." , Toast.LENGTH_LONG).show();
                PassResetAthor.this.finish();


            }
            else if (result.equalsIgnoreCase("Password_Not_Reset")){

                Toast.makeText(PassResetAthor.this, "Your password was not reset.", Toast.LENGTH_LONG).show();

            }
            else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(PassResetAthor.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }

    }
}

