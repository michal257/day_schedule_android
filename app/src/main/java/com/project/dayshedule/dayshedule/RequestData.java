package com.project.dayshedule.dayshedule;

import android.os.AsyncTask;

import com.project.dayshedule.dayshedule.Enum.ResultType;
import com.project.dayshedule.dayshedule.Interface.OnTaskCompleted;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.project.dayshedule.dayshedule.Enum.ResultType.ARRAY;
import static com.project.dayshedule.dayshedule.Enum.ResultType.OBJECT;

public class RequestData extends AsyncTask<Void, Void, String> {

    private Spiner spiner;
    private RequestDataParameters param;
    private OnTaskCompleted callback;
    private ResultType type;

    public RequestData(RequestDataParameters param, ResultType type, OnTaskCompleted state) {
        this.param = param;
        this.callback = state;
        this.type = type;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        spiner = new Spiner(param.getmContext());
        spiner.start(param.getMessage());
    }

    @Override
    protected String doInBackground(Void... params) {

        ConnectionAPI conn = new ConnectionAPI(param.getmContext());

        String jsonString = conn.connectAPI(param.getUrl(), param.getjObiect(), param.getReqType());

        return jsonString;
    }

    @Override
    protected void onPostExecute(String json) {
        spiner.stop();
        try {
            Object object = null;
            if (type == OBJECT){
                object = new JSONObject(json);
            }else if (type == ARRAY){
                object = new JSONArray(json);
            }
            callback.onTaskCompleted(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
