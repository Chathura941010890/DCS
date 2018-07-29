package com.example.chathura.dcs;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LeaveRequestList extends AppCompatActivity {

    String urlAddress = "http://10.0.2.2/ListLeaveRequests.php";
    String [] name;
    String [] designation;
    String [] imagePath;
    String [] app;
    ListView listView;
    int x =0;
    int y =0;
    String pName;
    String pRole;
    String pImage;
    String viewList;
    BufferedInputStream input;
    String line = null;
    String result = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_request_list);
        listView = (ListView)findViewById(R.id.leaveRequestList);


        final String athoDesi = getIntent().getExtras().getString("Designation");
        if(athoDesi.equals("HOD")){
            viewList = "No";
        }
        if(athoDesi.equals("DEAN")){
            viewList = "FH";
        }
        if(athoDesi.equals("VC")){
            viewList = "FD";
        }

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
                Intent intent = new Intent(LeaveRequestList.this, LeaveRequestView.class);
                intent.putExtra("GetNameL" ,pName);
                intent.putExtra("GetRoleL" , pRole);
                intent.putExtra("GetImageL" , pImage);
                intent.putExtra("DesignationSend" , athoDesi);
                startActivity(intent);
                LeaveRequestList.this.finish();
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
            app = new String[jsonArray.length()];
            for(int i=0; i<jsonArray.length(); i++){

                    jsonObject = jsonArray.getJSONObject(i);
                    app[i] = jsonObject.getString("Approved");
                if(app[i].equals(viewList)) {
                    name[ i ] = jsonObject.getString("FirstName");
                    designation[ i ] = jsonObject.getString("Name");
                    imagePath[ i ] = jsonObject.getString("Image");
                    //x = i;
                }
                else{
                    //jsonArray.remove(i);
                    name[i] = null;
                    designation[i] = null;
                    imagePath[i] = null;
                    x++;
                }

            }
            if(x == jsonArray.length()){
                y=1;
                Toast.makeText(LeaveRequestList.this, "No Requests to show", Toast.LENGTH_LONG).show();
                LeaveRequestList.this.finish();
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
