package com.example.chathura.dcs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class ListVC extends AppCompatActivity {
    String urlAddress = "http://10.0.2.2/ListVC.php";
    String [] name;
    String [] designation;
    String [] imagePath;
    ListView listView;
    int x =0;
    int y =0;
    String pName;
    String pRole;
    String pImage;
    BufferedInputStream input;
    String line = null;
    String result = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_vc);
        listView = (ListView)findViewById(R.id.listVC);

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        collectData();
        CustomListView customListView = new CustomListView(this, name, designation, imagePath);
        listView.setAdapter(customListView);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                pName = name[position].toString();
                pRole = designation[position].toString();
                pImage = imagePath[position].toString();
                Intent intent = new Intent(ListVC.this, Profile_View.class);
                intent.putExtra("GetName" ,pName);
                intent.putExtra("GetRole" , pRole);
                intent.putExtra("GetImage" , pImage);
                startActivity(intent);
            }
            });

    }

    private void collectData(){
        HttpURLConnection conn;
        try{
            URL url = new URL(urlAddress);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            input = new BufferedInputStream(conn.getInputStream());
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
            StringBuilder stringBuilder = new StringBuilder();

            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line + "\n");
            }

            input.close();
            result = stringBuilder.toString();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        try{
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject = null;
            name = new String[jsonArray.length()];
            designation = new String[jsonArray.length()];
            imagePath = new String[jsonArray.length()];
            for(int i=0; i<jsonArray.length(); i++){
                jsonObject = jsonArray.getJSONObject(i);
                name[i] = jsonObject.getString("FirstName");
                designation[i] = jsonObject.getString("Name");
                imagePath[i] = jsonObject.getString("Image");
                x=i;
            }
            if(x == jsonArray.length()-1){
                y=1;
            }
            else{
                y=0;
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }

}
