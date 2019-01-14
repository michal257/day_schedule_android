package com.project.dayshedule.dayshedule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.project.dayshedule.dayshedule.Enum.RequestType;
import com.project.dayshedule.dayshedule.Enum.ResultType;
import com.project.dayshedule.dayshedule.Interface.OnTaskCompleted;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            checkLogin();
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkLogin() throws ExecutionException, InterruptedException, JSONException {

        new RequestData(createParameterObject(), ResultType.OBJECT, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) throws JSONException {
                JSONObject jObject = (JSONObject) object;
                Boolean login = Boolean.parseBoolean(jObject.getString("login"));
                openActivity(login);
            }
        }).execute();
}

    private void openActivity(Boolean login) {

        if (login){
            Intent desktop = new Intent(MainActivity.this, DesktopActivity.class);
            startActivity(desktop);
            finish();
        }else{
            Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginActivity);
            finish();
        }
    }

    private RequestDataParameters createParameterObject(){
        RequestDataParameters parameters = new RequestDataParameters();
        parameters.setjObiect(null);
        parameters.setmContext(MainActivity.this);
        parameters.setMessage("Trwa łączenie z serweeram...");
        parameters.setUrl("Check.php");
        parameters.setReqType(RequestType.POST);
        return parameters;
    }
}
