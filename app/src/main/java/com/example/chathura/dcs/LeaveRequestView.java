package com.example.chathura.dcs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

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
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class LeaveRequestView extends AppCompatActivity {

    TextView FName;
    TextView role;
    TextView RequestedTime;
    TextView LeaveType;
    TextView WantedDate;
    TextView NumOfDays;
    MultiAutoCompleteTextView Description;
    ImageView imdView;
    StringBuilder result;
    String pName;
    String pRole;
    String pImage;
    Bitmap bitmap;
    Button Accept;
    Button Reject;
    Button Forward;
    String datetime;
    String datetime1;
    String datetime2;
    String Action;
    String Forwarded;
    int time = 0;
    int time1 = 0;

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private static final String TAG = "LeaveRequestView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_request_view);


        FName = (TextView) findViewById(R.id.tvFirstNameL);
        role = (TextView) findViewById(R.id.tvRoleL);
        RequestedTime = (TextView) findViewById(R.id.tvRequestedTime);
        LeaveType = (TextView) findViewById(R.id.tvLeaveType);
        WantedDate = (TextView) findViewById(R.id.tvWantedDate);
        NumOfDays = (TextView) findViewById(R.id.tvNumOfDays);
        Description = (MultiAutoCompleteTextView) findViewById(R.id.tvDescription);
        imdView = (ImageView)findViewById(R.id.profilePictureL);
        Accept = (Button)findViewById(R.id.btnAccept);
        Reject = (Button)findViewById(R.id.btnReject);
        Forward = (Button)findViewById(R.id.btnForward);



        final String athoDesi = getIntent().getExtras().getString("DesignationSend");
        pName = getIntent().getExtras().getString("GetNameL");
        pRole = getIntent().getExtras().getString("GetRoleL");
        pImage = getIntent().getExtras().getString("GetImageL");
        new LeaveRequestView.AsyncSetL().execute(pName,pRole);
        new LeaveRequestView.getImageFromURL(imdView).execute(pImage);
        if(athoDesi.equals("HOD")){
            Forwarded = "FH";
        }
        if(athoDesi.equals("DEAN")){
            Forwarded = "FD";
        }
        if(athoDesi.equals("VC")){
            Forward.setEnabled(false);
            Forward.setVisibility(View.INVISIBLE);
        }

        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentTime = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                datetime = dateFormat.format(currentTime);
                final String xx = datetime;
                new acceptLeave().execute(xx , athoDesi, "Acc",pName, pRole);
                Action = "accepted";
            }
        });

        Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentTime1 = Calendar.getInstance().getTime();
                DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                datetime1 = dateFormat1.format(currentTime1);
                final String xy = datetime1;
                new acceptLeave().execute(xy , athoDesi, "Rej",pName, pRole);
                Action = "rejected";
            }
        });

        Forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date currentTime2 = Calendar.getInstance().getTime();
                DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                datetime2 = dateFormat2.format(currentTime2);
                final String xz = datetime2;
                new acceptLeave().execute(xz , athoDesi, Forwarded,pName, pRole);
                Action = "forwarded";
            }
        });

    }

    private class AsyncSetL extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(LeaveRequestView.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("http://localhost/LeaveViewDetails.php");

            }
            catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("FirstName", params[ 0 ])
                        .appendQueryParameter("Role", params[ 1 ]);
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
                    result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return (result.toString());

                }
                else {

                    return ("unsuccessful");
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

            if (result.equalsIgnoreCase("false")) {
                Toast.makeText(LeaveRequestView.this, "Details cannot be loaded.", Toast.LENGTH_LONG).show();

            }
            else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(LeaveRequestView.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
            }
            else {
                FName.setText(pName);
                role.setText(pRole);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = null;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String ReqTime = jsonObject.getString("RequestedTime");
                        String LType = jsonObject.getString("Name");
                        String Wanted = jsonObject.getString("WantedDate");
                        String Num = jsonObject.getString("NoofDays");
                        String Des = jsonObject.getString("Description");

                        RequestedTime.setText(ReqTime);
                        LeaveType.setText(LType);
                        WantedDate.setText(Wanted);
                        NumOfDays.setText(Num);
                        Description.setText(Des);


                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public class getImageFromURL extends AsyncTask<String, Void, Bitmap>{

        public getImageFromURL(ImageView imgv){
            imdView=imgv;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String urlDisplay = url[0];
            bitmap = null;

            try{
                InputStream ist = new java.net.URL(urlDisplay).openStream();
                bitmap = BitmapFactory.decodeStream(ist);

            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            imdView.setImageBitmap(bitmap);
        }
    }

    private class acceptLeave extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(LeaveRequestView.this);
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
                url = new URL("http://10.0.2.2/AcceptLeave.php");

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
                        .appendQueryParameter("ApprovedTime", params[0])
                        .appendQueryParameter("ApprovedId", params[1])
                        .appendQueryParameter("Accepted", params[2])
                        .appendQueryParameter("Name", params[3])
                        .appendQueryParameter("Role", params[4]);
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

            if(result.equalsIgnoreCase("accepted"))
            {
                Toast.makeText(LeaveRequestView.this, "Leave request was " + Action + " successfully", Toast.LENGTH_LONG).show();
                LeaveRequestView.this.finish();
            }
            else if (result.equalsIgnoreCase("not_accepted")){

                Toast.makeText(LeaveRequestView.this, "Leave request was not " + Action, Toast.LENGTH_LONG).show();

            }
            else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(LeaveRequestView.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }

        }

    }

}
