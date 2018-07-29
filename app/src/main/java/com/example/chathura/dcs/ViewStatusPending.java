package com.example.chathura.dcs;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class ViewStatusPending extends AppCompatActivity {
    TextView status;
    Button okay;
    Button cancel;
    String LeaveType;
    String wantDate;
    String incomingStatus;
    String reqDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_status_pending);

        LeaveType = getIntent().getExtras().getString("XXLeave");
        wantDate = getIntent().getExtras().getString("XXDate");
        incomingStatus = getIntent().getExtras().getString("XXS");
        reqDate = getIntent().getExtras().getString("XXR");


        status = (TextView)findViewById(R.id.tvViewStatusPND);
        status.setText("Your " + LeaveType + " leave request on " + wantDate + " is pending.");

        okay = (Button)findViewById(R.id.btnOkayPND);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewStatusPending.this.finish();
            }
        });

        cancel = (Button)findViewById(R.id.btnCancelRequest);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncCheck().execute(LeaveType,wantDate,reqDate);
            }
        });
    }


    private class AsyncCheck extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ViewStatusPending.this);
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
                url = new URL("http://192.168.8.101/CancelRequest.php");

            }
            catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("LeaveType", params[0])
                        .appendQueryParameter("WantedDate", params[1])
                        .appendQueryParameter("ReqTime", params[2]);
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

            if(result.equalsIgnoreCase("Canceled")) {
                Toast.makeText(ViewStatusPending.this , "Leave Request Successfully Canceled" , Toast.LENGTH_LONG).show();
                ViewStatusPending.this.finish();

            }
            else if (result.equalsIgnoreCase("Not_Canceled")){

                Toast.makeText(ViewStatusPending.this, "Leave Request Was Not Canceled", Toast.LENGTH_LONG).show();

            }
            else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(ViewStatusPending.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }

    }
}
