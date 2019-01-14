package com.project.dayshedule.dayshedule.Groceries;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.project.dayshedule.dayshedule.Enum.RequestType;
import com.project.dayshedule.dayshedule.Enum.ResultType;
import com.project.dayshedule.dayshedule.Interface.OnTaskCompleted;
import com.project.dayshedule.dayshedule.Models.GroceriesModel;
import com.project.dayshedule.dayshedule.R;
import com.project.dayshedule.dayshedule.RequestData;
import com.project.dayshedule.dayshedule.RequestDataParameters;
import org.json.JSONException;
import org.json.JSONObject;

public class GroceriesDetailsActivity extends AppCompatActivity {

    private int GroceriesGID;
    private String GroceriesName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries_details);
        setStatusBar();

        Bundle grocId = getIntent().getExtras();
        GroceriesGID = grocId.getInt("id");
        getGroceriesDetails();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setStatusBar(){
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
    }

    private void openEditGroceries(){
        Intent addGroceries = new Intent(GroceriesDetailsActivity.this, GroceriesAddActivity.class);
        Bundle type = new Bundle();
        type.putString("type", "edit");
        type.putInt("editGID", GroceriesGID);
        type.putString("editName", GroceriesName);
        addGroceries.putExtras(type);
        startActivity(addGroceries);
        finish();
    }

    private void getGroceriesDetails(){
        new RequestData(createGetGroceriesDetailsParam(), ResultType.OBJECT, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object obj) throws JSONException {
                JSONObject jObject = (JSONObject) obj;
                GroceriesModel grocObject = new GroceriesModel(jObject.getInt("GID"), jObject.get("Name").toString(), jObject.get("Category").toString());
                GroceriesName = grocObject.getName();
                TextView textViewName = (TextView) findViewById(R.id.groceriesDetails_productName);
                textViewName.setText(GroceriesName);
                String groceriesCategory = grocObject.getCategory();
                TextView textViewCategory = (TextView) findViewById(R.id.groceriesDetails_productCategory);
                textViewCategory.setText(groceriesCategory);
            }
        }).execute();
    }

    private RequestDataParameters createGetGroceriesDetailsParam(){
        RequestDataParameters param = new RequestDataParameters();
        param.setmContext(GroceriesDetailsActivity.this);
        param.setMessage("Trwa usuwanie produktu...");
        param.setUrl("GroceriesController.php?groceriesGID=" + GroceriesGID);
        param.setReqType(RequestType.GET);
        return param;
    }

    private void deleteGroceries() throws JSONException {
        new RequestData(createDeleteGroceriesParam(), ResultType.OBJECT, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object obj) throws JSONException {
                JSONObject jObject = (JSONObject) obj;
                Boolean result = Boolean.parseBoolean(jObject.getString("result"));
                if (result){
                    Toast.makeText(GroceriesDetailsActivity.this, "Produkt został usunięty.", Toast.LENGTH_LONG).show();
                    openGroceriesList();
                } else {
                    Toast.makeText(GroceriesDetailsActivity.this, "Nie udało się zapisać zmian!", Toast.LENGTH_LONG).show();
                }
            }
        }).execute();
    }

    private RequestDataParameters createDeleteGroceriesParam() throws JSONException {
        RequestDataParameters param = new RequestDataParameters();
        param.setmContext(GroceriesDetailsActivity.this);
        param.setMessage("Trwa pobieranie danych...");
        param.setUrl("GroceriesController.php");
        param.setReqType(RequestType.DELETE);
        param.setjObiect(createDeleteGroceriesObject());
        return param;
    }

    private JSONObject createDeleteGroceriesObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("GID", GroceriesGID);
        return jsonObject;
    }

    private void openGroceriesList(){
        Intent grocList = new Intent(GroceriesDetailsActivity.this, GroceriesListActivity.class);
        startActivity(grocList);
        finish();
    }

    public void btnBack(View v){
        Intent groceriesList = new Intent(GroceriesDetailsActivity.this, GroceriesListActivity.class);
        startActivity(groceriesList);
        finish();
    }

    public void btnDelete(View v) throws JSONException {
        deleteGroceries();
    }

    public void btnEdit(View v){
        openEditGroceries();
    }
}
