package com.example.chathura.dcs;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class EMP_Main extends AppCompatActivity {

    Button profileView;
    Button passwordReset;
    Button SendLeaves;
    Button ViewMy;
    String EnteredUN;
    String EnteredPW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp__main);

        profileView = (Button)findViewById(R.id.viewProfile);
        passwordReset = (Button)findViewById(R.id.btnResetPW);
        EnteredUN = getIntent().getExtras().getString("GetUN");
        EnteredPW = getIntent().getExtras().getString("GetPW");
        SendLeaves = (Button)findViewById(R.id.btnLeave);
        ViewMy = (Button)findViewById(R.id.btnViewMy);


        profileView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intentLoadVC = new Intent(EMP_Main.this, ChooseProfile.class);
                startActivity(intentLoadVC);
            }
        });

        passwordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReset = new Intent(EMP_Main.this , PasswordReset.class);
                intentReset.putExtra("CheckUN" , EnteredUN);
                intentReset.putExtra("CheckPW" , EnteredPW);
                startActivity(intentReset);
                EMP_Main.this.finish();
            }
        });
        SendLeaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSend = new Intent(EMP_Main.this , CheckLeaves.class);
                startActivity(intentSend);
            }
        });

        ViewMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentView = new Intent(EMP_Main.this , ViewMyRequests.class);
                intentView.putExtra("CheckUN" , EnteredUN);
                startActivity(intentView);
            }
        });
    }
}