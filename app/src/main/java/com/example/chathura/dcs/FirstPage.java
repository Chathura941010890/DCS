package com.example.chathura.dcs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class FirstPage extends AppCompatActivity {
    Button vc;
    Button dean;
    Button hod;
    Button employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        if(!isConnected(FirstPage.this)) buildDialog(FirstPage.this).show();
        else{
            vc = (Button) findViewById(R.id.btnVC);
            dean = (Button) findViewById(R.id.btnDEAN);
            hod = (Button) findViewById(R.id.btnHOD);
            employee = (Button) findViewById(R.id.btnEMP);



            vc.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Intent intentLoadVC = new Intent(FirstPage.this, VCLogin.class);
                    startActivity(intentLoadVC);
                }
            });

            dean.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentLoadDean = new Intent(FirstPage.this, DeanLogin.class);
                    startActivity(intentLoadDean);
                }
            });

            hod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentLoadHod = new Intent(FirstPage.this, HodLogin.class);
                    startActivity(intentLoadHod);
                }
            });

            employee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentLoadEmp = new Intent(FirstPage.this, EmpLogin.class);
                    startActivity(intentLoadEmp);
                }
            });
        }
    }

    public boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if(netInfo != null && netInfo.isConnectedOrConnecting()){
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if(mobile != null && mobile.isConnectedOrConnecting() || wifi != null && wifi.isConnectedOrConnecting()){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public AlertDialog.Builder buildDialog(Context c){
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have mobile data of WIFI to access this. Otherwise press ok to exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        return builder;
    }
}

