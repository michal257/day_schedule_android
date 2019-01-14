package com.project.dayshedule.dayshedule;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedService {

    public static String getSessionId(Context mContext){
        SharedPreferences pref = mContext.getSharedPreferences("SessionID", MODE_PRIVATE);
        String sessId = "PHPSESSID=" + pref.getString("id", "");
        return sessId;
    }

    public static void saveSessionId(String sessionId, Context context){
        SharedPreferences pref = context.getSharedPreferences("SessionID", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = pref.edit();
        mEditor.putString("id", sessionId);
        mEditor.apply();
    }

}
