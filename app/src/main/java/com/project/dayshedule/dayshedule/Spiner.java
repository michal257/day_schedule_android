package com.project.dayshedule.dayshedule;

import android.app.ProgressDialog;
import android.content.Context;

public class Spiner {

    private ProgressDialog dialog;
    private Context mContext;

    public Spiner(Context context) {
        this.mContext = context;
    }

    public void start(String message){
      dialog = new ProgressDialog(mContext);
        dialog.setMessage(message);
        dialog.setCancelable(true);
        dialog.show();
    }

    public void stop(){
        dialog.cancel();
    }
}
