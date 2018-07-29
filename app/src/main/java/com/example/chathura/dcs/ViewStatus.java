package com.example.chathura.dcs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewStatus extends AppCompatActivity {
    TextView status;
    Button okay;
    String LeaveType;
    String wantDate;
    String incomingStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_status);

        LeaveType = getIntent().getExtras().getString("XXLeave");
        wantDate = getIntent().getExtras().getString("XXDate");
        incomingStatus = getIntent().getExtras().getString("XXS");

        status = (TextView)findViewById(R.id.tvViewStatusACC);
        status.setText("Your " + LeaveType + " leave request on " + wantDate + " was accepted.");

        okay = (Button)findViewById(R.id.btnOkayACC);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewStatus.this.finish();
            }
        });

    }
}
