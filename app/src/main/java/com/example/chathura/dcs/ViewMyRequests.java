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

public class ViewMyRequests extends AppCompatActivity {

    String urlAddress = "http://10.0.2.2/MyView.php";
    String [] LeaveType;
    String [] ReqTime;
    String [] WantDate;
    String [] status;
    String [] UserName;
    ListView listView;
    String Username;
    int x =0;
    int y =0;
    String viewList;
    BufferedInputStream input;
    String line = null;
    String result = null;
    String lType;
    String wDate;
    String rTime;
    String S;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_requests);
        listView = (ListView)findViewById(R.id.MyList);


        Username = getIntent().getExtras().getString("CheckUN");
        //Username.replace("" ,U);

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        collectData();
        CustomMy customListView = new CustomMy(this, LeaveType, ReqTime, WantDate, status);
        listView.setAdapter(customListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                lType = LeaveType[position].toString();
                wDate = WantDate[position].toString();
                S = status[position].toString();
                rTime = ReqTime[position].toString();
                if(S.equals("Acc")) {
                    Intent intentACC = new Intent(ViewMyRequests.this, ViewStatus.class);
                    intentACC.putExtra("XXLeave", lType);
                    intentACC.putExtra("XXDate", wDate);
                    intentACC.putExtra("XXS", S);
                    startActivity(intentACC);
                    ViewMyRequests.this.finish();
                }
                else if(S.equals("Rej")) {
                    Intent intentRej = new Intent(ViewMyRequests.this, ViewStatusRej.class);
                    intentRej.putExtra("XXLeave", lType);
                    intentRej.putExtra("XXDate", wDate);
                    intentRej.putExtra("XXS", S);
                    startActivity(intentRej);
                    ViewMyRequests.this.finish();
                }
                else{
                    Intent intentPnd = new Intent(ViewMyRequests.this, ViewStatusPending.class);
                    intentPnd.putExtra("XXLeave", lType);
                    intentPnd.putExtra("XXDate", wDate);
                    intentPnd.putExtra("XXS", S);
                    intentPnd.putExtra("XXR" , rTime);
                    startActivity(intentPnd);
                    ViewMyRequests.this.finish();
                }
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
            LeaveType = new String[jsonArray.length()];
            ReqTime = new String[jsonArray.length()];
            WantDate = new String[jsonArray.length()];
            status = new String[jsonArray.length()];
            UserName = new String[jsonArray.length()];
            for(int i=0; i<jsonArray.length(); i++){

                jsonObject = jsonArray.getJSONObject(i);
                UserName[i] = jsonObject.getString("UserName");
                if(UserName[i].equals(Username)) {
                    LeaveType[ i ] = jsonObject.getString("Name");
                    ReqTime[ i ] = jsonObject.getString("RequestedTime");
                    WantDate[ i ] = jsonObject.getString("WantedDate");
                    status[ i ] = jsonObject.getString("Approved");
                    x++;
                }
                else{
                    LeaveType[ i ] =null;// jsonObject.getString("Name");
                    ReqTime[ i ] =null;// jsonObject.getString("RequestedTime");
                    WantDate[ i ] =null;// jsonObject.getString("WantedDate");
                    status[ i ] =null;// jsonObject.getString("Approved");
                    //x++;
                }


            }
            if(x == 0){
                y=1;
                Toast.makeText(ViewMyRequests.this, "No Requests to show", Toast.LENGTH_LONG).show();
                ViewMyRequests.this.finish();
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
