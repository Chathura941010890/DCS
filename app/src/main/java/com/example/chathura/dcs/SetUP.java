package com.example.chathura.dcs;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
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
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

public class SetUP extends AppCompatActivity {
    EditText Username;
    EditText Password;
    Button Submit;
    Button SMS;
    Button Email;
    String uName;
    String pWord;
    String Phone;
    String mail;
    String NIC;
    final static String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm.@#$?";
    private final static int REQUEST_CODE_PERMISION_SEND_SMS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        Username = (EditText)findViewById(R.id.etSetUsername);
        Password = (EditText)findViewById(R.id.etSetPassword);
        Submit = (Button)findViewById(R.id.btnSubmitUser);
        SMS = (Button)findViewById(R.id.btnSendSMS);
        Email = (Button)findViewById(R.id.btnSendEmail);
        Email.setEnabled(false);

//SMS
        SMS.setEnabled(false);
        if(checkPermission(Manifest.permission.SEND_SMS)){
            SMS.setEnabled(true);
        }
        else{
            ActivityCompat.requestPermissions(SetUP.this , new String[]{
                    (Manifest.permission.SEND_SMS)
            } , REQUEST_CODE_PERMISION_SEND_SMS);
        }

        SMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "Your username & password for the Attedance Management System of Department of Computer Science has successfully been sent to your e-mail.";
                Phone = getIntent().getExtras().getString("GetPhone");


                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(Phone , null , msg , null ,null);


                Toast.makeText(SetUP.this, "Message Sent", Toast.LENGTH_LONG).show();
            }
        });

//Email

        Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String message = "Your User name is " + uName + " & Your Password is " + pWord;
               String subject = "Username & PW for Attendance Management System";
               mail = getIntent().getExtras().getString("GetEmail");
               String[] ee = mail.split(",");

               Intent intentEmail = new Intent(Intent.ACTION_SEND);
               intentEmail.putExtra(Intent.EXTRA_EMAIL , ee);
               intentEmail.putExtra(Intent.EXTRA_SUBJECT , subject);
               intentEmail.putExtra(Intent.EXTRA_TEXT , message);

               intentEmail.setType("message/rfc822");
               startActivity(Intent.createChooser(intentEmail , "Choose an email client"));
               finish();
            }
        });

        Password.setFocusable(false);
        //Password.setEnabled(false);

        final String x = getRandomString(8);
        Password.setText(x);
    }

    public boolean checkPermission(String permission){
        int checkPermission = ContextCompat.checkSelfPermission(this, permission);
        return checkPermission == PackageManager.PERMISSION_GRANTED;

    }

    public void SubmitUser(View view){
        uName = Username.getText().toString();
        pWord = Password.getText().toString();

        NIC = getIntent().getExtras().getString("GetNIC");

        if(uName == null || pWord == null){
            Toast.makeText(SetUP.this, "Please fill all the fields." , Toast.LENGTH_LONG).show();
        }
        else{
            BackgroundWorkerSet backgroundWorker = new BackgroundWorkerSet(this);
            backgroundWorker.execute(NIC, uName, pWord);
        }
    }

    public class BackgroundWorkerSet extends AsyncTask<String, Void, String> {
        String urlSet = "http://10.0.2.2/setUp.php";
        Context ctx;
        HttpURLConnection connSet;
        URL url = null;
        ProgressDialog pdLoading = new ProgressDialog(SetUP.this);

        BackgroundWorkerSet(Context ctx) {
            this.ctx = ctx;
        }

        public void onPreExecute() {
            super.onPreExecute();


            pdLoading.setMessage("\tInserting...");
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
                        .appendQueryParameter("passNic", params[ 0 ])
                        .appendQueryParameter("passU", params[ 1 ])
                        .appendQueryParameter("passP", params[ 2 ]);
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
                connSet.disconnect();
            }


        }

        public void onPostExecute(String result) {
            pdLoading.dismiss();
            if (result.equalsIgnoreCase("Inserting_Successful")) {

                SMS.setEnabled(true);
                Email.setEnabled(true);
                Toast.makeText(SetUP.this, "Inserted", Toast.LENGTH_LONG).show();

            }
            else if (result.equalsIgnoreCase("Inserting_Failed")) {

                Toast.makeText(SetUP.this, "Not Inserted", Toast.LENGTH_LONG).show();

            }
            else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(SetUP.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode , @NonNull String permissions[] , @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode , permissions , grantResults);
        switch(requestCode){
            case REQUEST_CODE_PERMISION_SEND_SMS:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    SMS.setEnabled(true);
                }

        }
    }


    public static String getRandomString(int size){
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(size);
        for(int i=0;i<size;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

}
