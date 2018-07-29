package com.example.chathura.dcs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.RelativeLayout;
import android.widget.TextView;


public class CustomMy extends ArrayAdapter<String> {
    private String[] LeaveType;
    private String[] ReqTime;
    private String[] WantDate;
    private String[] Status;
    private Activity context;

    boolean x  = false;

    public CustomMy(Activity context, String[] LeaveType, String[] ReqTime, String[] WantDate, String[] Status){
        super(context, R.layout.layoutmyrequests,LeaveType);
        this.context = context;
        this.LeaveType=LeaveType;
        this.ReqTime = ReqTime;
        this.WantDate = WantDate;
        this.Status = Status;
    }

    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View r = convertView;
        CustomMy.ViewHolder viewHolder = null;
        if(r==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r= layoutInflater.inflate(R.layout.layoutmyrequests, null ,  true);
            viewHolder = new CustomMy.ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder = (CustomMy.ViewHolder)r.getTag();
        }
        int a = position;

        if(LeaveType[ a ] == null && ReqTime[ a ] == null && WantDate[ a ] == null && Status[ a ] == null){
            r.setVisibility(View.GONE);
            r.setEnabled(false);
            viewHolder.rx.setVisibility(View.GONE);
            viewHolder.tvLeaveTypeMy.setVisibility(View.GONE);
            viewHolder.tvReqTimeMy.setVisibility(View.GONE);
            viewHolder.tvWantMy.setVisibility(View.GONE);
            viewHolder.tvStatus.setVisibility(View.GONE);
            x = true;
        }
        else{
            x = false;
            viewHolder.tvLeaveTypeMy.setText(LeaveType[ a ]);
            viewHolder.tvReqTimeMy.setText(ReqTime[ a ]);
            viewHolder.tvWantMy.setText(WantDate[ a ]);
            viewHolder.tvStatus.setText(Status[ a ]);
        }

        return r;

    }

    class ViewHolder{
        TextView tvLeaveTypeMy;
        TextView tvReqTimeMy;
        TextView tvWantMy;
        TextView tvStatus;
        RelativeLayout rx;


        ViewHolder(View v){

            tvLeaveTypeMy = (TextView) v.findViewById(R.id.tvLeaveTypeMy);
            tvReqTimeMy = (TextView) v.findViewById(R.id.tvReqTimeMy);
            tvWantMy = (TextView) v.findViewById(R.id.tvWantMy);
            tvStatus = (TextView) v.findViewById(R.id.tvStatusMy);
            rx = (RelativeLayout)v.findViewById(R.id.rl12);

        }
    }

}
