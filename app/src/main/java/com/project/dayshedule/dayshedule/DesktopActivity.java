package com.project.dayshedule.dayshedule;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.project.dayshedule.dayshedule.Enum.RequestType;
import com.project.dayshedule.dayshedule.Enum.ResultType;
import com.project.dayshedule.dayshedule.Interface.OnTaskCompleted;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class DesktopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desktop);
        setStatusBar();
        setToolBar();
    }

    private void setToolBar(){
        Toolbar toolbar = findViewById(R.id.app_bar);
        toolbar.setTitle("Plan Dnia");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOut:
                Logout();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setStatusBar() {
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
    }

    private void openLoginActivity(){
        Intent login = new Intent(DesktopActivity.this, LoginActivity.class);
        startActivity(login);
        finish();
    }

    private void errorInfo(){
        Toast.makeText(DesktopActivity.this, "Nie udało się wylogować!", Toast.LENGTH_SHORT).show();
    }

    private void Logout(){
        new RequestData(createLogoutParameterObject(), ResultType.OBJECT, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) throws JSONException {
                JSONObject jObject = (JSONObject) object;
                Boolean result = Boolean.parseBoolean(jObject.getString("result"));
                if (result){
                    openLoginActivity();
                }else{
                    errorInfo();
                }
            }
        }).execute();
    }

    private RequestDataParameters createLogoutParameterObject(){
        RequestDataParameters parameters = new RequestDataParameters();
        parameters.setmContext(DesktopActivity.this);
        parameters.setMessage("Trwa wylogowywanie...");
        parameters.setUrl("Logout.php");
        parameters.setReqType(RequestType.POST);
        return parameters;
    }

    public void openMainMenu(View v){
        Intent mainMenu = new Intent(this, MainMenuActivity.class);
        startActivity(mainMenu);
    }

}
