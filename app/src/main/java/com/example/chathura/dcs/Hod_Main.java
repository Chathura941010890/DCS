package com.example.chathura.dcs;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class Hod_Main extends AppCompatActivity {

    Button profileView;
    Button registerEmployee;
    Button passwordReset;
    Button RemoveUser;
    Button ViewLeaveRequests;
    String EnteredUN;
    String EnteredPW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hod__main);

        profileView = (Button)findViewById(R.id.viewProfile);
        registerEmployee = (Button)findViewById(R.id.Register);
        passwordReset = (Button)findViewById(R.id.btnResetPW);
        RemoveUser = (Button)findViewById(R.id.btnRemove);
        ViewLeaveRequests = (Button)findViewById(R.id.btnViewLeave);
        EnteredUN = getIntent().getExtras().getString("GetUN");
        EnteredPW = getIntent().getExtras().getString("GetPW");


        profileView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intentLoadVC = new Intent(Hod_Main.this, ChooseProfile.class);
                startActivity(intentLoadVC);
            }
        });

        registerEmployee.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intentRegister = new Intent(Hod_Main.this, Register.class);
                startActivity(intentRegister);
            }
        });

        passwordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReset = new Intent(Hod_Main.this , PassResetAthor.class);
                intentReset.putExtra("CheckUN" , EnteredUN);
                intentReset.putExtra("CheckPW" , EnteredPW);
                startActivity(intentReset);
                Hod_Main.this.finish();
            }
        });

        RemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister = new Intent(Hod_Main.this, RemoveUser.class);
                startActivity(intentRegister);
            }
        });


        ViewLeaveRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listLeave = new Intent(Hod_Main.this , LeaveRequestList.class);
                listLeave.putExtra("Designation" , "HOD");
                startActivity(listLeave);
            }
        });
    }
}