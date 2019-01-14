package com.project.dayshedule.dayshedule.Groceries;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.project.dayshedule.dayshedule.Enum.RequestType;
import com.project.dayshedule.dayshedule.Enum.ResultType;
import com.project.dayshedule.dayshedule.Interface.OnTaskCompleted;
import com.project.dayshedule.dayshedule.Models.GroceriesModel;
import com.project.dayshedule.dayshedule.R;
import com.project.dayshedule.dayshedule.RequestData;
import com.project.dayshedule.dayshedule.RequestDataParameters;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroceriesListActivity extends AppCompatActivity {

    private ListView lvGroceries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries_list);
        setStatusBar();

        lvGroceries = (ListView) findViewById(R.id.listView_groceries);

        lvGroceries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GroceriesModel groc = (GroceriesModel) lvGroceries.getItemAtPosition(i);
                openDetailsGroceries(groc.getGID());
            }
        });
        getGroceries();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setStatusBar(){
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
    }

    private void openDetailsGroceries(int groceriesID){
        Intent grocDetails = new Intent(GroceriesListActivity.this, GroceriesDetailsActivity.class);
        Bundle id = new Bundle();
        id.putInt("id", groceriesID);
        grocDetails.putExtras(id);
        startActivity(grocDetails);
        finish();
    }

    private void getGroceries(){
        new RequestData(createGetGroceriesParam(), ResultType.ARRAY, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object obj) throws JSONException {
                JSONArray jArray = (JSONArray) obj;
                ArrayList<GroceriesModel> ArrayGroceriesList = new ArrayList<>();
                for (int i = 0; i < jArray.length(); i++)
                {
                    JSONObject g = (JSONObject) jArray.get(i);
                    ArrayGroceriesList.add(new GroceriesModel(Integer.parseInt(g.get("GID").toString()), g.get("Name").toString(), g.get("Category").toString()));
                }
                GroceriesListAdapter adapter = new GroceriesListAdapter(GroceriesListActivity.this, ArrayGroceriesList);
                lvGroceries.setAdapter(adapter);
            }
        }).execute();
    }

    private RequestDataParameters createGetGroceriesParam(){
        RequestDataParameters param = new RequestDataParameters();
        param.setmContext(GroceriesListActivity.this);
        param.setMessage("Trwa pobieranie danych...");
        param.setUrl("GroceriesController.php");
        param.setReqType(RequestType.GET);
        return param;
    }

    public void btnAddGroc(View v){
        Intent addGroceries = new Intent(GroceriesListActivity.this, GroceriesAddActivity.class);
        Bundle type = new Bundle();
        type.putString("type", "add");
        addGroceries.putExtras(type);
        startActivity(addGroceries);
        finish();
    }

    public void btnGroceriesCategoryList(View v){
        Intent grocCategoryList = new Intent(GroceriesListActivity.this, GroceriesCategoryListActivity.class);
        startActivity(grocCategoryList);
    }
}
