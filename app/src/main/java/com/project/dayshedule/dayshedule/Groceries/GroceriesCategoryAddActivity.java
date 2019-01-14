package com.project.dayshedule.dayshedule.Groceries;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.dayshedule.dayshedule.Enum.RequestType;
import com.project.dayshedule.dayshedule.Enum.ResultType;
import com.project.dayshedule.dayshedule.Interface.OnTaskCompleted;
import com.project.dayshedule.dayshedule.R;
import com.project.dayshedule.dayshedule.RequestData;
import com.project.dayshedule.dayshedule.RequestDataParameters;

import org.json.JSONException;
import org.json.JSONObject;

public class GroceriesCategoryAddActivity extends AppCompatActivity {

    private String addEditName;
    private String typeWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries_category_add);
        setStatusBar();

        Bundle windowType = getIntent().getExtras();
        typeWindow = windowType.getString("type");

        implementControllers();
    }

    private void implementControllers(){
        TextView textViewName = (TextView) findViewById(R.id.groceriesCategoryAdd_textView);
        TextView textViewHeader = (TextView) findViewById(R.id.groceriesCategoryAdd_textView_header);
        Button btnSaveNewGroceriesCategory = (Button) findViewById(R.id.groceriesCategoryAdd_btnAddGroceries);

        if (typeWindow.matches("add")){
            btnSaveNewGroceriesCategory.setText("Dodaj kategorie");
        }else if (typeWindow.matches("edit")){
            btnSaveNewGroceriesCategory.setText("Zapisz zmiany");
            textViewName.setText("Podaj nową nazwę kategorii:");
            textViewHeader.setText("Edycja kategorii");
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setStatusBar(){
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
    }

    private boolean getValue()
    {
        EditText textName = (EditText) findViewById(R.id.groceriesCategoryAdd_editText);
        addEditName = textName.getText().toString();

        if (addEditName.isEmpty()){
            Toast.makeText(GroceriesCategoryAddActivity.this, "Nie podano żadnej nazwy!", Toast.LENGTH_LONG).show();
            return false;
        } else if (addEditName.length() > 30){
            Toast.makeText(GroceriesCategoryAddActivity.this, "Podana nazwa jest zbyt długa!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void saveNewCategory() throws JSONException {
        new RequestData(createSaveNewCategoryParam(), ResultType.OBJECT, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object obj) throws JSONException {
                JSONObject jObject = (JSONObject) obj;
                Boolean result = Boolean.parseBoolean(jObject.getString("result"));
                if (result){
                    Toast.makeText(GroceriesCategoryAddActivity.this, "Dodano nową kategorie", Toast.LENGTH_LONG).show();
                    openGroceriesCategoryList();
                }else{
                    Toast.makeText(GroceriesCategoryAddActivity.this, "Nie udało się dodać noej kategorii", Toast.LENGTH_LONG).show();
                }
            }
        }).execute();
    }

    private RequestDataParameters createSaveNewCategoryParam() throws JSONException {
        RequestDataParameters param = new RequestDataParameters();
        param.setmContext(GroceriesCategoryAddActivity.this);
        param.setMessage("Trwa zapis danych..");
        param.setjObiect(createSaveNewCategoryObject());
        param.setUrl("GroceriesCategoryController.php");
        param.setReqType(RequestType.POST);
        return param;
    }

    private JSONObject createSaveNewCategoryObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("Name", addEditName);
        return jsonObject;
    }

    public void btnAddEditGroceriesCategory(View v) throws JSONException {
        if (getValue()) {
            if(typeWindow.matches("add")){
                saveNewCategory();
            }else{
                //updateCategory();
            }
        }
    }

    private void openGroceriesCategoryList(){
        Intent groceriesCategoryList = new Intent(GroceriesCategoryAddActivity.this, GroceriesCategoryListActivity.class);
        startActivity(groceriesCategoryList);
        finish();
    }

    @Override
    public void onBackPressed(){
        if (typeWindow.matches("add")){
            Intent groceriesCategoryList = new Intent(GroceriesCategoryAddActivity.this, GroceriesCategoryListActivity.class);
            startActivity(groceriesCategoryList);
        }
        finish();
    }
}
