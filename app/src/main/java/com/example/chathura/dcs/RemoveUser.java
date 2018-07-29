package com.example.chathura.dcs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RemoveUser extends AppCompatActivity {
    EditText NIC;
    Button Next;
    String nic;
    boolean nicOkay = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_user);

        NIC = (EditText)findViewById(R.id.etRemoveNIC);
        Next = (Button)findViewById(R.id.btnCheckNIC);

        NIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NIC.setTextColor(0xff000000 );
            }
        });


        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nic = NIC.getText().toString();

                for(int i=0; i<nic.length(); i++){
                    char x = nic.charAt(i);
                    if(x == '0' || x== '1' || x == '2' || x == '3' || x == '4' || x == '5' || x == '6'|| x =='7' || x == '8' || x =='9' || x == 'v'){
                    }
                    else{
                        nicOkay = false;
                    }
                }

                if(nic.equals("")){
                    Toast.makeText(RemoveUser.this, "Please enter a NIC Number", Toast.LENGTH_LONG).show();
                }
                else if(nicOkay == false){
                    Toast.makeText(RemoveUser.this, "Please enter a valid nic number" , Toast.LENGTH_SHORT).show();
                    NIC.setTextColor(0xFFFF0000 );
                }
                else{
                    new RemoveUser.backgroundWorkerRemoveCheck().execute(nic);
                }
            }
        });
    }

    public class backgroundWorkerRemoveCheck extends AsyncTask<String, Void, String> {
        String urlSet = "http://10.0.2.2/RemoveNIC.php";
        Context ctx;
        HttpURLConnection connSet;
        URL url = null;
        ProgressDialog pdLoading = new ProgressDialog(RemoveUser.this);



        public void onPreExecute() {
            super.onPreExecute();


            pdLoading.setMessage("\tChecking...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        public String doInBackground(String... params) {

            try {
                url = new URL(urlSet);
                connSet = (HttpURLConnection) url.openConnection();
                connSet.setRequestMethod("POST");
                connSet.setDoOutput(true);
                connSet.setDoInput(true);
            } catch (Exception e) {
                e.printStackTrace();
                return "exception";
            }
            try {

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("passNic", params[ 0 ]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = connSet.getOutputStream();
                BufferedWriter bwRegister = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                bwRegister.write(query);
                bwRegister.flush();
                bwRegister.close();
                os.close();
                connSet.connect();

            }
            catch (Exception e) {
                e.printStackTrace();
                return "exception";
            }

            try {

                int response_code = connSet.getResponseCode();

                if (response_code == HttpURLConnection.HTTP_OK) {

                    InputStream input = connSet.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            }
            catch (IOException e) {
                e.printStackTrace();
                return "exception";
            }
            finally {
                connSet.disconnect();
            }


        }

        public void onPostExecute(String result) {
            pdLoading.dismiss();
            if (result.equalsIgnoreCase("NIC_ok")) {
                Toast.makeText(RemoveUser.this, "NIC Matched", Toast.LENGTH_LONG).show();
                buildDialog(RemoveUser.this).show();


            }
            else if (result.equalsIgnoreCase("NIC_not_ok")) {

                Toast.makeText(RemoveUser.this, "There is no user with this NIC.", Toast.LENGTH_LONG).show();
                finish();

            }
            else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(RemoveUser.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }
    }


    public AlertDialog.Builder buildDialog(Context c){
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Remove User");
        builder.setMessage("Are you sure about this. Press Yes to Continue. ");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new RemoveUser.RemoveU().execute(nic);
            }
        });
        return builder;
    }

    public class RemoveU extends AsyncTask<String, Void, String> {
        String urlSet = "http://10.0.2.2/RemoveU.php";
        Context ctx;
        HttpURLConnection connSet;
        URL url = null;
        ProgressDialog pdLoading = new ProgressDialog(RemoveUser.this);



        public void onPreExecute() {
            super.onPreExecute();


            pdLoading.setMessage("\tRemoving...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        public String doInBackground(String... params) {

            try {
                url = new URL(urlSet);
                connSet = (HttpURLConnection) url.openConnection();
                connSet.setRequestMethod("POST");
                connSet.setDoOutput(true);
                connSet.setDoInput(true);
            }
            catch (Exception e) {
                e.printStackTrace();
                return "exception";
            }
            try {

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("passU", params[ 0 ]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = connSet.getOutputStream();
                BufferedWriter bwRegister = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                bwRegister.write(query);
                bwRegister.flush();
                bwRegister.close();
                os.close();
                connSet.connect();

            }
            catch (Exception e) {
                e.printStackTrace();
                return "exception";
            }

            try {

                int response_code = connSet.getResponseCode();

                if (response_code == HttpURLConnection.HTTP_OK) {

                    InputStream input = connSet.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            }
            catch (IOException e) {
                e.printStackTrace();
                return "exception";
            }
            finally {
                connSet.disconnect();
            }


        }

        public void onPostExecute(String result) {
            pdLoading.dismiss();
            if (result.equalsIgnoreCase("Removed")) {
                Toast.makeText(RemoveUser.this, "User Removed Successfully.", Toast.LENGTH_LONG).show();
                finish();


            }
            else if (result.equalsIgnoreCase("Not_Removed")) {

                Toast.makeText(RemoveUser.this, "User cannot be removed", Toast.LENGTH_LONG).show();
                finish();

            }
            else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(RemoveUser.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }
    }

}
