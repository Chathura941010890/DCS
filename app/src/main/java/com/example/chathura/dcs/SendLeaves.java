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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SendLeaves extends AppCompatActivity {
    public static final String TAG = "SendLeaves";
    EditText DescriptionL;
    EditText CurrentState;
    TextView pickDate;
    TextView NICN;
    EditText NoDays;
    Spinner ChooseLeaves;
    TextView Remaining;
    Button apply;
    Button pick;
    Button cancel;
    String nic;
    String descriptionl;
    String currentstate;
    String LeaveType;
    String datetime;
    String wantedDate;
    String wantedNo;
    String Approved = "No";
    String x;
    boolean picked = false;
                BufferedInputStream isSpinner;
                String resultSpinner;
    HttpURLConnection connSpinner;
    int time = 0;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_leaves);

        DescriptionL = (EditText) findViewById(R.id.etDescription);
        CurrentState = (EditText) findViewById(R.id.etCurrentState);
        ChooseLeaves = (Spinner) findViewById(R.id.spnChooseLeave);
        pickDate = (TextView) findViewById(R.id.etPickDate);
        pick = (Button) findViewById(R.id.btnPickDate);
        NoDays = (EditText) findViewById(R.id.etNoDays);
        Remaining = (TextView) findViewById(R.id.tvRemainingLeaves);
        apply = (Button) findViewById(R.id.btnSendLeave);
        cancel = (Button)findViewById(R.id.btnCancel);
        NICN = (TextView)findViewById(R.id.asdnic) ;

        Intent incomingIntent = getIntent();
        String want = incomingIntent.getStringExtra("GetDate");

        pickDate.setText(want);



        //Spinner
        listAdapter = new ArrayAdapter<String>(this, R.layout.spinner_resource, R.id.text, listItems);
        ChooseLeaves.setAdapter(listAdapter);
        ChooseLeaves.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> listAdapter, View view, int position, long id) {
                LeaveType = ChooseLeaves.getSelectedItem().toString();
                if (LeaveType.equals("Half Day") || LeaveType.equals("Short Leave")) {
                    NoDays.setText("0");
                    NoDays.setEnabled(false);
                } else {
                    NoDays.setEnabled(true);
                }
                Toast.makeText(listAdapter.getContext(), "Selected : " + LeaveType, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x = getIntent().getExtras().getString("GetNICCheck");
                Intent intent = new Intent(SendLeaves.this, ChooseDate.class);
                intent.putExtra("SendNIC" , x);
                startActivity(intent);
                picked = true;

            }
        });


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nic = getIntent().getExtras().getString("NIC");
                NICN.setText(nic);
                descriptionl = DescriptionL.getText().toString();
                currentstate = CurrentState.getText().toString();
                Date currentTime = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                datetime = dateFormat.format(currentTime);
                wantedDate = pickDate.getText().toString();
                wantedNo = NoDays.getText().toString();

                    new applyLeave().execute(nic, LeaveType, descriptionl, currentstate, datetime, wantedDate, wantedNo, Approved);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendLeaves.this.finish();
            }
        });

    }


    protected void onStart(){
        super.onStart();
        BackTask bt = new BackTask();
        bt.execute();
    }


    public class BackTask extends AsyncTask<Void,Void , Void> {
        ArrayList<String> list;
        protected void onPreExecute(){
            super.onPreExecute();
            list = new ArrayList<>();
        }

        protected Void doInBackground(Void... params){
            isSpinner = null;
            resultSpinner = "";

            try{
                URL url = new URL("http://10.0.2.2/SpinnerLeaves.php");
                connSpinner = (HttpURLConnection)url.openConnection();
                connSpinner.setRequestMethod("POST");
                isSpinner = new BufferedInputStream(connSpinner.getInputStream());
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

            try{
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(isSpinner , "utf-8"));
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    resultSpinner += line;
                }
                isSpinner.close();

            }
            catch(IOException e){
                e.printStackTrace();
            }

            try {
                JSONArray jsonArray = new JSONArray(resultSpinner);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(jsonObject.getString("Name"));
                }

            }
            catch(JSONException e){
                e.printStackTrace();
            }
            connSpinner.disconnect();
            return null;
        }

        protected void onPostExecute(Void resultSpinner){
            listItems.addAll(list);
            listAdapter.notifyDataSetChanged();
        }
    }


    private class applyLeave extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(SendLeaves.this);
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
                url = new URL("http://10.0.2.2/ApplyLeave.php");

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
                        .appendQueryParameter("nic", params[0])
                        .appendQueryParameter("leaveType", params[1])
                        .appendQueryParameter("description", params[2])
                        .appendQueryParameter("state", params[3])
                        .appendQueryParameter("time", params[4])
                        .appendQueryParameter("wantedDate", params[5])
                        .appendQueryParameter("wantedNo", params[6])
                        .appendQueryParameter("Approved", params[7]);
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

            if(result.equalsIgnoreCase("sent"))
            {
                Toast.makeText(SendLeaves.this, "Leave request was sent successfully", Toast.LENGTH_LONG).show();
                SendLeaves.this.finish();
            }
            else if (result.equalsIgnoreCase("not_sent")){

                Toast.makeText(SendLeaves.this, "Leave request was not sent. Your Description must contain less than 200 characters", Toast.LENGTH_LONG).show();

            }
            else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(SendLeaves.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }

        }

    }


}
