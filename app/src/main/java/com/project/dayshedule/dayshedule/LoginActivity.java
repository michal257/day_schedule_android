package com.project.dayshedule.dayshedule;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.project.dayshedule.dayshedule.Enum.RequestType;
import com.project.dayshedule.dayshedule.Enum.ResultType;
import com.project.dayshedule.dayshedule.Interface.OnTaskCompleted;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setStatusBar();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setStatusBar(){
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
    }

    public void Login(View v) throws JSONException, ExecutionException, InterruptedException {
        String[] loginValue = getLoginValue();
        JSONObject jObject = createLoginJsonObject(loginValue);

        new RequestData(createLoginParameterObject(jObject), ResultType.OBJECT, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) throws JSONException {
                JSONObject jObject = (JSONObject) object;
                Boolean log = Boolean.parseBoolean(jObject.getString("login"));

                 if (log){
                    String sessionId = jObject.getString("sessionId");
                    SharedService.saveSessionId(sessionId, LoginActivity.this);
                   openDesktop();
                }else{
                    TextView errorText = (TextView) findViewById(R.id.text_login_error);
                    errorText.setVisibility(TextView.VISIBLE);
                 }
            }
        }).execute();
    }

    private String[] getLoginValue(){
        EditText login = (EditText) findViewById(R.id.etext_login_login);
        EditText textPass = (EditText) findViewById(R.id.etext_login_pass);

        String[] loginValue = new String[2];

        loginValue[0] = login.getText().toString();
        loginValue[1] = textPass.getText().toString();

        return loginValue;
    }

    private JSONObject createLoginJsonObject(String[] loginValue) throws JSONException {
        JSONObject jObject = new JSONObject();
        jObject.put("Login", loginValue[0]);
        jObject.put("Password", loginValue[1]);

        return jObject;
    }

    private RequestDataParameters createLoginParameterObject(JSONObject jObject){
        RequestDataParameters parametres = new RequestDataParameters();
        parametres.setmContext(LoginActivity.this);
        parametres.setMessage("Trwa łączenie z serwerem...");
        parametres.setUrl("LoginController.php");
        parametres.setjObiect(jObject);
        parametres.setReqType(RequestType.POST);
        return parametres;
    }

    private void openDesktop(){
        Intent desktop = new Intent(LoginActivity.this, DesktopActivity.class);
        startActivity(desktop);
        finish();
    }
}
