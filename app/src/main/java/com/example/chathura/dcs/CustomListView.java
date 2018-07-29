package com.example.chathura.dcs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import java.io.InputStream;
import java.io.PipedOutputStream;

/**
 * Created by chathura on 3/27/18.
 */

public class CustomListView extends ArrayAdapter<String> {

    private String[] profileName;
    private String[] desig;
    private String[] imaPath;
    private Activity context;
    Bitmap bitmap;
    boolean x  = false;

    public CustomListView(Activity context, String[] profileName, String[] desig, String[] imaPath){
        super(context, R.layout.layout,profileName);
        this.context = context;
        this.profileName=profileName;
        this.desig = desig;
        this.imaPath = imaPath;
    }

    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View r = convertView;
        ViewHolder viewHolder = null;
            if(r==null){
                LayoutInflater layoutInflater = context.getLayoutInflater();
                r= layoutInflater.inflate(R.layout.layout, null ,  true);
                viewHolder = new ViewHolder(r);
                    r.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder)r.getTag();
            }
            int a = position;
            if(profileName[a] != null && desig[a] != null) {
                    x = false;
                viewHolder.tvName.setText(profileName[ a ]);
                viewHolder.tvDesignation.setText(desig[ a ]);
                new getImageFromURL(viewHolder.imageView).execute(imaPath[ a ]);
            }
            else{
                r.setVisibility(View.GONE);
                r.setEnabled(false);
                viewHolder.rx.setVisibility(View.GONE);
                viewHolder.tvName.setVisibility(View.GONE);
                viewHolder.tvDesignation.setVisibility(View.GONE);
                viewHolder.imageView.setVisibility(View.GONE);
                x = true;
            }


    return r;

    }

    class ViewHolder{
        TextView tvName;
        TextView tvDesignation;
        ImageView imageView;
        RelativeLayout rx;

        ViewHolder(View v){

                tvName = (TextView) v.findViewById(R.id.tvName);
                tvDesignation = (TextView) v.findViewById(R.id.tvDesignation);
                imageView = (ImageView) v.findViewById(R.id.imageView);
                rx = (RelativeLayout)v.findViewById(R.id.rl136);
        }
    }


    public class getImageFromURL extends AsyncTask<String, Void, Bitmap>{

            ImageView imdView;

            public getImageFromURL(ImageView imgv){
                this.imdView=imgv;
            }

        @Override
        protected Bitmap doInBackground(String... url) {
            String urlDisplay = url[0];
            bitmap = null;

            try{
                InputStream ist = new java.net.URL(urlDisplay).openStream();
                bitmap = BitmapFactory.decodeStream(ist);

            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap){
                super.onPostExecute(bitmap);
                imdView.setImageBitmap(bitmap);
        }
    }
}
