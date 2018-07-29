package com.example.chathura.dcs;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class VC_Main extends AppCompatActivity {

    Button profileView;
    Button passwordReset;
    Button ViewLeaves;
    String EnteredUN;
    String EnteredPW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vc__main);

        profileView = (Button)findViewById(R.id.viewProfile);
        passwordReset = (Button)findViewById(R.id.btnResetPW);
        ViewLeaves = (Button)findViewById(R.id.btnLeaveVC);
        EnteredUN = getIntent().getExtras().getString("GetUN");
        EnteredPW = getIntent().getExtras().getString("GetPW");


        profileView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intentLoadVC = new Intent(VC_Main.this, ChooseProfile.class);
                startActivity(intentLoadVC);
            }
        });

        passwordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReset = new Intent(VC_Main.this , PassResetAthor.class);
                intentReset.putExtra("CheckUN" , EnteredUN);
                intentReset.putExtra("CheckPW" , EnteredPW);
                startActivity(intentReset);
                VC_Main.this.finish();
            }
        });

        ViewLeaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent leave = new Intent(VC_Main.this , LeaveRequestList.class);
                leave.putExtra("Designation" , "VC");
                startActivity(leave);
            }
        });
    }
}
