package com.example.chathura.dcs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.GregorianCalendar;

public class ChooseDate extends AppCompatActivity {
    Button Okay;
    CalendarView vi;
    String Date1 = "dfds";
    TextView choosedDate;
    Activity context;
    String y;
    private static final String TAG = "ChooseDate";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date);
        vi = (CalendarView)findViewById(R.id.calendarView);
        choosedDate = (TextView)findViewById(R.id.tvChoosedDate);
        Okay = (Button)findViewById(R.id.btnOkay);
        context = ChooseDate.this;

        vi.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView vi, int year, int month, int dayOfMonth) {
                month++;
                Date1 = (year + "-" + month + "-" + dayOfMonth);
                choosedDate.setText(Date1);
                Log.d(TAG , "onSelectedDatChane : Date : " + Date1);
            }
        });


        Okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                y = getIntent().getExtras().getString("SendNIC");
                Intent intent = new Intent(ChooseDate.this, SendLeaves.class);
                intent.putExtra("GetDate" , Date1);
                intent.putExtra("NIC" , y);
                startActivity(intent);
                ChooseDate.this.finish();
            }
        });
    }

}
