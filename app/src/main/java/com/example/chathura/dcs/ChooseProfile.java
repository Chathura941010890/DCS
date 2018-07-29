package com.example.chathura.dcs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseProfile extends AppCompatActivity {
    Button athority;
    Button employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_profile);

        athority = (Button)findViewById(R.id.btnAthoView);
        employee = (Button)findViewById(R.id.btnEmpView);

        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emp = new Intent(ChooseProfile.this , ListVC.class);
                startActivity(emp);
            }
        });

        athority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent atho = new Intent(ChooseProfile.this , ListAthority.class);
                startActivity(atho);
            }
        });
    }
}
