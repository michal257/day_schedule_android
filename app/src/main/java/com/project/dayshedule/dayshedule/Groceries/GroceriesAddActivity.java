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

public class GroceriesAddActivity extends AppCompatActivity {

    private String addEditName;
    private String TypeWindow;
    private int editGID;
    private int grocCatGID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries_add);
        setStatusBar();

        Bundle windowType = getIntent().getExtras();
        TypeWindow = windowType.getString("type");
        editGID = windowType.getInt("editGID");
        String editName = (String) windowType.get("editName");
        implementControllers(editName);
    }

    private void implementControllers(String editName){
        TextView textViewName = (TextView) findViewById(R.id.groceriesAdd_textView);
        TextView textViewHeader = (TextView) findViewById(R.id.groceriesAdd_textView_header);
        Button btnSaveNewGroceries = (Button) findViewById(R.id.groceriesAdd_btnAddGroceries);
        Button btnSelectCategory = (Button) findViewById(R.id.groceriesAdd_btnSelectCategory);
        EditText editTextName = (EditText) findViewById(R.id.groceriesAdd_editText);

        if (TypeWindow.matches("add")){
            btnSaveNewGroceries.setText("Dodaj produkt");
        }else if (TypeWindow.matches("edit")){
            textViewName.setText("Podaj nową nazwę produktu");
            btnSaveNewGroceries.setText("Zapisz zmiany");
            btnSelectCategory.setText("Zmień kategorię");
            textViewHeader.setText("Edycja produktu");
            editTextName.setText(editName);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setStatusBar(){
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
    }

    @Override
    public void onBackPressed() {

        if (TypeWindow.matches("add")){
            Intent groceriesList = new Intent(GroceriesAddActivity.this, GroceriesListActivity.class);
            startActivity(groceriesList);
        } else if (TypeWindow.matches("edit")){
            Intent grocDetails = new Intent(GroceriesAddActivity.this, GroceriesDetailsActivity.class);
            Bundle id = new Bundle();
            id.putInt("id", editGID);
            grocDetails.putExtras(id);
            startActivity(grocDetails);
        }
        finish();
    }

    private boolean getValue()
    {
        EditText textName = (EditText) findViewById(R.id.groceriesAdd_editText);
        addEditName = textName.getText().toString();

        if (addEditName.isEmpty()){
            Toast.makeText(GroceriesAddActivity.this, "Nie podano żadnej nazwy!", Toast.LENGTH_LONG).show();
            return false;
        } else if (addEditName.length() > 30){
            Toast.makeText(GroceriesAddActivity.this, "Podana nazwa jest zbyt długa!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    //-----------------------------------------------------------------------------------------

    private void saveNewGroceries() throws JSONException {
        new RequestData(createSaveNewGroceriesParam(), ResultType.OBJECT, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object obj) throws JSONException {
                JSONObject jObject = (JSONObject) obj;
                Boolean result = Boolean.parseBoolean(jObject.getString("result"));
                if (result){
                    Toast.makeText(GroceriesAddActivity.this, "Produkt dodany", Toast.LENGTH_LONG).show();
                    openGroceriesList();
                }
                else {
                    Toast.makeText(GroceriesAddActivity.this, "Nie udało się dodać produktu!", Toast.LENGTH_LONG).show();
                }
            }
        }).execute();
    }

    private RequestDataParameters createSaveNewGroceriesParam() throws JSONException {
        RequestDataParameters param = new RequestDataParameters();
        param.setmContext(GroceriesAddActivity.this);
        param.setMessage("Trwa zapis danych...");
        param.setjObiect(createSaveNewGroceriesObject());
        param.setUrl("GroceriesController.php");
        param.setReqType(RequestType.POST);
        return param;
    }

    private JSONObject createSaveNewGroceriesObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("Name", addEditName);
        return jsonObject;
    }

    private void openGroceriesList(){
        Intent groceriesList = new Intent(GroceriesAddActivity.this, GroceriesListActivity.class);
        startActivity(groceriesList);
        finish();
    }

    //------------------------------------------------------------------------------------------

    private void updateGroceries() throws JSONException {
        new RequestData(createUpdateGroceriesParam(), ResultType.OBJECT, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object obj) throws JSONException {
                JSONObject jObject = (JSONObject) obj;
                Boolean result = Boolean.parseBoolean(jObject.getString("result"));
                if (result){
                    Toast.makeText(GroceriesAddActivity.this, "Zmiany zapisane.", Toast.LENGTH_LONG).show();
                    openGroceriesDetails();
                }
                else {
                    Toast.makeText(GroceriesAddActivity.this, "Nie udało się zapisać zmian!", Toast.LENGTH_LONG).show();
                }
            }
        }).execute();
    }

    private RequestDataParameters createUpdateGroceriesParam() throws JSONException {
        RequestDataParameters param = new RequestDataParameters();
        param.setmContext(GroceriesAddActivity.this);
        param.setMessage("Trwa zapis danych...");
        param.setjObiect(createUpdateGroceriesObject());
        param.setUrl("GroceriesController.php");
        param.setReqType(RequestType.PUT);
        return param;
    }

    private JSONObject createUpdateGroceriesObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("GID", editGID);
        jsonObject.accumulate("Name", addEditName);
        jsonObject.accumulate("Category", grocCatGID);
        return jsonObject;
    }

    private void openGroceriesDetails(){
        Intent grocDetails = new Intent(GroceriesAddActivity.this, GroceriesDetailsActivity.class);
        Bundle id = new Bundle();
        id.putInt("id", editGID);
        grocDetails.putExtras(id);
        startActivity(grocDetails);
        finish();
    }

    public void btnAddEditGroceries(View v){
        if (getValue()){
            if(TypeWindow.matches("add")){
                try {
                    saveNewGroceries();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (TypeWindow.matches("edit")){
                try {
                    updateGroceries();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void btnSelectCategory(View v){
        Intent selectCategory = new Intent(this, GroceriesCategorySelectListActivity.class);
        startActivityForResult(selectCategory, 1000);


        //startActivity(selectCategory);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        grocCatGID = data.getIntExtra("result", 0);
    }
}
