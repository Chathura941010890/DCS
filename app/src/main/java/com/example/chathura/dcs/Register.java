package com.example.chathura.dcs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import  android.content.Intent;

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
import java.net.URLEncoder;
import java.util.ArrayList;

public class Register extends AppCompatActivity {

    EditText FirstName;
    EditText LastName;
    EditText Initials;
    EditText NIC;
    EditText Phone;
    EditText Email;
    EditText Address;
    BufferedInputStream isSpinner;
    String resultSpinner;
    String urlSpinner = "http://10.0.2.2/SpinnerAthorities.php";
    //String urlReg = "http://10.0.2.2/RegisterSend.php";
    HttpURLConnection connSpinner;
   // HttpURLConnection connReg;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> listAdapter;
    Spinner sp;
    String fName;
    String lName;
    String ini;
    String nic;
    String contact;
    String email;
    String address;
    String Designation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirstName = (EditText)findViewById(R.id.etFirstName);
        LastName = (EditText)findViewById(R.id.etLastName);
        Initials = (EditText)findViewById(R.id.etInitials);
        NIC = (EditText)findViewById(R.id.etNIC);
        Phone = (EditText)findViewById(R.id.etContact);
        Email = (EditText)findViewById(R.id.etEmail);
        Address = (EditText)findViewById(R.id.etAddress);
        sp = (Spinner)findViewById(R.id.spinner);
        listAdapter = new ArrayAdapter<String>(this, R.layout.spinner_resource , R.id.text , listItems);

        sp.setAdapter(listAdapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> listAdapter, View view, int position, long id) {
                Designation = sp.getSelectedItem().toString();
                Toast.makeText(listAdapter.getContext() , "Selected : " + Designation, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected void onStart(){
        super.onStart();
        BackTask bt = new BackTask();
        bt.execute();
    }

    public class BackTask extends AsyncTask<Void,Void , Void>{
        ArrayList<String> list;
        protected void onPreExecute(){
            super.onPreExecute();
            list = new ArrayList<>();
        }

        protected Void doInBackground(Void... params){
            isSpinner = null;
            resultSpinner = "";

            try{
                URL url = new URL(urlSpinner);
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


    public void Submit(View view){

        fName = FirstName.getText().toString();
        lName = LastName.getText().toString();
        ini = Initials.getText().toString();
        nic = NIC.getText().toString();
        contact = Phone.getText().toString();
        email = Email.getText().toString();
        address = Address.getText().toString();
        //Designation = "EMP";
        boolean ok = false;
        boolean dotOk = false;
        boolean nicOkay = true;
        boolean cOkay = true;

        for(int i=0; i<email.length(); i++){
            char x = email.charAt(i);
            if(x == '@'){
                ok = true;
            }
            if(x == '.'){
                dotOk = true;
            }
        }

        for(int i=0; i<nic.length(); i++){
            char x = nic.charAt(i);
            if(x == '0' || x== '1' || x == '2' || x == '3' || x == '4' || x == '5' || x == '6'|| x =='7' || x == '8' || x =='9' || x == 'v'){
            }
            else{
                nicOkay = false;
            }
        }

        for(int i=0; i<contact.length(); i++){
            char x = contact.charAt(i);
            if(x == '0' || x== '1' || x == '2' || x == '3' || x == '4' || x == '5' || x == '6'|| x =='7' || x == '8' || x =='9'){
            }
            else{
                cOkay = false;
            }
        }

        if(fName.equals("") || lName.equals("") || ini.equals("") || nic.equals("") || contact.equals("") || email.equals("") || Address.equals("")){
            Toast.makeText(Register.this, "Please fill all the fields." , Toast.LENGTH_SHORT).show();
        }
        else if(ok == false && dotOk == false){
            Toast.makeText(Register.this, "Please enter a valid email address" , Toast.LENGTH_SHORT).show();
            Email.setTextColor(0xFFFF0000 );

        }
        else if(nicOkay == false){
            Toast.makeText(Register.this, "Please enter a valid nic number" , Toast.LENGTH_SHORT).show();
            NIC.setTextColor(0xFFFF0000 );
        }
        else if(cOkay == false){
            Toast.makeText(Register.this, "Please enter a valid contact number" , Toast.LENGTH_SHORT).show();
            Phone.setTextColor(0xFFFF0000 );
        }
        else{
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(fName , lName, ini, nic, address , email , Designation , contact);
            finish();
        }

        NIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NIC.setTextColor(0xff000000 );
            }
        });

        Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email.setTextColor(0xff000000 );
            }
        });
        Phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Phone.setTextColor(0xff000000 );
            }
        });

    }

    public class BackgroundWorker extends AsyncTask<String, Void, String> {
        String urlReg = "http://10.0.2.2/RegisterSend.php";
        Context ctx;
        HttpURLConnection connReg;
        URL url = null;
        ProgressDialog pdLoading = new ProgressDialog(Register.this);

        BackgroundWorker(Context ctx){
            this.ctx = ctx;
        }

        public void onPreExecute(){
            super.onPreExecute();


            pdLoading.setMessage("\tInserting...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        public String doInBackground(String... params){

            try {
                url = new URL(urlReg);
                connReg = (HttpURLConnection) url.openConnection();
                connReg.setRequestMethod("POST");
                connReg.setDoOutput(true);
                connReg.setDoInput(true);
            }
            catch(Exception e) {
                e.printStackTrace();
                return "exception";
            }
            try{

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("passFname", params[0])
                        .appendQueryParameter("passLname", params[1])
                        .appendQueryParameter("passIni", params[2])
                        .appendQueryParameter("passNic", params[3])
                        .appendQueryParameter("passAdd", params[4])
                        .appendQueryParameter("passEmail", params[5])
                        .appendQueryParameter("passDesi", params[6])
                        .appendQueryParameter("passContact", params[7]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = connReg.getOutputStream();
                BufferedWriter bwRegister = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                bwRegister.write(query);
                bwRegister.flush();
                bwRegister.close();
                os.close();
                connReg.connect();

            }
            catch (Exception e) {
                e.printStackTrace();
                return "exception";
            }

            try {

                int response_code = connReg.getResponseCode();

                if (response_code == HttpURLConnection.HTTP_OK) {

                    InputStream input = connReg.getInputStream();
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
                connReg.disconnect();
            }


        }

    public void onPostExecute(String result){
        pdLoading.dismiss();
        if(result.equalsIgnoreCase("Inserting_Successful"))
        {
            Intent intent = new Intent(Register.this,SetUP.class);
            intent.putExtra("GetPhone" ,contact);
            intent.putExtra("GetEmail" ,email);
            intent.putExtra("GetNIC" ,nic);
            startActivity(intent);

            Toast.makeText(Register.this, "Inserted", Toast.LENGTH_LONG).show();

        }
        else if (result.equalsIgnoreCase("Inserting_Failed")){

            Toast.makeText(Register.this, "Not Inserted", Toast.LENGTH_LONG).show();

        }
        else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

            Toast.makeText(Register.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

        }
    }
}



}
