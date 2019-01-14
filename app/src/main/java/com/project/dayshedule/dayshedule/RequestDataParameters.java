package com.project.dayshedule.dayshedule;

import android.content.Context;

import com.project.dayshedule.dayshedule.Enum.RequestType;

import org.json.JSONObject;

public class RequestDataParameters {

    private Context mContext;
    String message;
    String url;
    JSONObject jObiect = null;
    RequestType reqType;

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
            this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JSONObject getjObiect() {
        return jObiect;
    }

    public void setjObiect(JSONObject jObiect) {
        this.jObiect = jObiect;
    }

    public RequestType getReqType() {
        return reqType;
    }

    public void setReqType(RequestType reqType) {
            this.reqType = reqType;
    }
}
