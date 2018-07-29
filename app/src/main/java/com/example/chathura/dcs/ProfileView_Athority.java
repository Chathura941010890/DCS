package com.example.chathura.dcs;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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

public class ProfileView_Athority extends AppCompatActivity {
    // String urlAddress = "http://localhost/ProfileViewDetails.php";
    TextView FName;
    TextView role;
    TextView LNameA;
    TextView NicNoA;
    TextView PhoneA;
    TextView emailA;
    TextView addrA;
    ImageView imdView;
    StringBuilder result;
    String pName;
    String pRole;
    String pImage;
    Bitmap bitmap;
    Button EmailSendA;
    String mailA;
    Button makeCallA;
    String callNumA;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    public static final int REQUEST_CALL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view__athority);


        FName = (TextView) findViewById(R.id.tvFirstNameA);
        role = (TextView) findViewById(R.id.tvRoleA);
        LNameA = (TextView) findViewById(R.id.tvLastNameA);
        NicNoA = (TextView) findViewById(R.id.tvNICA);
        PhoneA = (TextView) findViewById(R.id.tvTPA);
        emailA = (TextView) findViewById(R.id.tvEmailA);
        addrA = (TextView) findViewById(R.id.tvAddressA);
        imdView = (ImageView)findViewById(R.id.profilePictureA);
        EmailSendA = (Button)findViewById(R.id.btnEmailA);
        makeCallA = (Button)findViewById(R.id.btnCallA);


        pName = getIntent().getExtras().getString("GetNameA");
        pRole = getIntent().getExtras().getString("GetRoleA");
        pImage = getIntent().getExtras().getString("GetImageA");
        new ProfileView_Athority.AsyncSet().execute(pName,pRole);
        new ProfileView_Athority.getImageFromURL(imdView).execute(pImage);


        EmailSendA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] ee = mailA.split(",");

                Intent intentEmail = new Intent(Intent.ACTION_SEND);
                intentEmail.putExtra(Intent.EXTRA_EMAIL , ee);

                intentEmail.setType("message/rfc822");
                startActivity(Intent.createChooser(intentEmail , "Choose an email client"));
                finish();
            }
        });

        makeCallA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCallA();
            }
        });
    }

    private class AsyncSet extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ProfileView_Athority.this);
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
                url = new URL("http://10.0.2.2/profileViewAtho.php");

            } catch (MalformedURLException e) {
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
                Toast.makeText(ProfileView_Athority.this, "Details cannot be loaded.", Toast.LENGTH_LONG).show();

            }
            else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(ProfileView_Athority.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
            }
            else {
                FName.setText(pName);
                role.setText(pRole);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = null;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String Lastname = jsonObject.getString("LastName");
                        String NICNo = jsonObject.getString("NICNumber");
                        String PhoneNumber = jsonObject.getString("PhoneNumber");
                        String Email = jsonObject.getString("Email");
                        String Address = jsonObject.getString("Address");

                        LNameA.setText(Lastname);
                        NicNoA.setText(NICNo);
                        PhoneA.setText(PhoneNumber);
                        callNumA = PhoneNumber;
                        emailA.setText(Email);
                        mailA = Email;
                        addrA.setText(Address);

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

    private void makePhoneCallA(){
        if(callNumA.trim().length() > 0){
            if(ContextCompat.checkSelfPermission(ProfileView_Athority.this , Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(ProfileView_Athority.this , new String[] {Manifest.permission.CALL_PHONE} , REQUEST_CALL);
            }
            else{
                String dial = "tel:" + callNumA;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }else{
            Toast.makeText(ProfileView_Athority.this, "No contact number to call" , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCallA();
            }
            else{
                Toast.makeText(this, "Permission Denied" , Toast.LENGTH_LONG).show();
            }
        }
    }

}
