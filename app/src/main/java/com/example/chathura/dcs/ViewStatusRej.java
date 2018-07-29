package com.example.chathura.dcs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewStatusRej extends AppCompatActivity {
    TextView status;
    Button okay;
    String LeaveType;
    String wantDate;
    String incomingStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_status_rej);

        LeaveType = getIntent().getExtras().getString("XXLeave");
        wantDate = getIntent().getExtras().getString("XXDate");
        incomingStatus = getIntent().getExtras().getString("XXS");

        status = (TextView)findViewById(R.id.tvViewStatusREJ);
        status.setText("Your " + LeaveType + " leave request on " + wantDate + " was rejected.");

        okay = (Button)findViewById(R.id.btnOkayREJ);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewStatusRej.this.finish();
            }
        });
    }
}
