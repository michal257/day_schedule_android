package com.project.dayshedule.dayshedule.Dishes;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.project.dayshedule.dayshedule.Enum.RequestType;
import com.project.dayshedule.dayshedule.Enum.ResultType;
import com.project.dayshedule.dayshedule.Interface.OnTaskCompleted;
import com.project.dayshedule.dayshedule.R;
import com.project.dayshedule.dayshedule.RequestData;
import com.project.dayshedule.dayshedule.RequestDataParameters;

import org.json.JSONException;
import org.json.JSONObject;

public class DishesAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_add);
        setStatusBar();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setStatusBar(){
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
    }

    @Override
    public void onBackPressed() {
        Intent groceriesList = new Intent(DishesAddActivity.this, DishesListActivity.class);
        startActivity(groceriesList);
        finish();
    }

    private void saveNewDish() throws JSONException {
        String[] dish = getNevDishValue();
        if (!dataVslidate(dish)){
            return;
        }
        JSONObject jObject = createNewDishObject(dish);

        new RequestData(createDishAddParameter(jObject), ResultType.OBJECT, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) throws JSONException {
                JSONObject jObject = (JSONObject) object;
                Boolean result = Boolean.parseBoolean(jObject.getString("result"));
                if (result){
                    Toast.makeText(DishesAddActivity.this, "Danie dodane", Toast.LENGTH_LONG).show();
                    openDishesListActivity();
                }
                else {
                    Toast.makeText(DishesAddActivity.this, "Nie udało się dodać produktu!", Toast.LENGTH_LONG).show();
                }
            }
        }).execute();
    }

    private JSONObject createNewDishObject(String[] dish) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.accumulate("Name", dish[0]);
        obj.accumulate("Description", dish[1]);
        return obj;
    }

    private String[] getNevDishValue(){
        EditText name = (EditText) findViewById(R.id.dishesAdd_editText_name);
        EditText description = (EditText) findViewById(R.id.dishesAdd_editText_description);
        String[] dish = new String[2];
        dish[0] = name.getText().toString();
        dish[1] = description.getText().toString();
        return dish;
    }

    private Boolean dataVslidate(String[] dish){
        if (dish[0].isEmpty()) {
            Toast.makeText(this, "Nie podano nazwy dania!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (dish[0].length() > 30){
            Toast.makeText(this, "Podana nazwa jest zbyt długa!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (dish[1].length() > 50){
            Toast.makeText(this, "Podany opis jest zbyt długi!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private RequestDataParameters createDishAddParameter(JSONObject jObject){
        RequestDataParameters parameters = new RequestDataParameters();
        parameters.setmContext(DishesAddActivity.this);
        parameters.setMessage("Trwa zapis danych...");
        parameters.setUrl("DishesController.php");
        parameters.setjObiect(jObject);
        parameters.setReqType(RequestType.POST);
        return parameters;
    }

    private void openDishesListActivity(){
        Intent dishesList = new Intent(DishesAddActivity.this, DishesListActivity.class);
        startActivity(dishesList);
        finish();
    }

    public void addNewDish(View v) throws JSONException {
        saveNewDish();
    }
}
