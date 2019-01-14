package com.project.dayshedule.dayshedule.Groceries;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.project.dayshedule.dayshedule.Enum.RequestType;
import com.project.dayshedule.dayshedule.Enum.ResultType;
import com.project.dayshedule.dayshedule.Interface.OnTaskCompleted;
import com.project.dayshedule.dayshedule.Models.GroceriesCategoryModel;
import com.project.dayshedule.dayshedule.R;
import com.project.dayshedule.dayshedule.RequestData;
import com.project.dayshedule.dayshedule.RequestDataParameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroceriesCategoryListActivity extends AppCompatActivity {

    private ListView lvGroceriesCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries_category);
        setStatusBar();

        lvGroceriesCategory = (ListView) findViewById(R.id.listView_groceries_category);

        getGroceriesCategory();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setStatusBar(){
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
    }

    private void getGroceriesCategory(){
        new RequestData(createGetGroceriesCategoryParam(), ResultType.ARRAY, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object obj) throws JSONException {
                JSONArray jArray = (JSONArray) obj;
                ArrayList<GroceriesCategoryModel> ArrayGroceriesCategory = new ArrayList<>();
                for (int i = 0; i < jArray.length(); i++)
                {
                    JSONObject g = (JSONObject) jArray.get(i);
                    ArrayGroceriesCategory.add(new GroceriesCategoryModel(Integer.parseInt(g.get("GID").toString()), g.get("Name").toString()));
                }
                GroceriesCategoryListAdapter adapter = new GroceriesCategoryListAdapter(GroceriesCategoryListActivity.this, ArrayGroceriesCategory);
                lvGroceriesCategory.setAdapter(adapter);
            }
        }).execute();
    }

    private RequestDataParameters createGetGroceriesCategoryParam(){
        RequestDataParameters param = new RequestDataParameters();
        param.setmContext(GroceriesCategoryListActivity.this);
        param.setMessage("Trwa pobieranie danych...");
        param.setUrl("GroceriesCategoryController.php");
        param.setReqType(RequestType.GET);
        return param;
    }

    public void openAddCategory(View v){
        Intent addCategory = new Intent(GroceriesCategoryListActivity.this, GroceriesCategoryAddActivity.class);
        Bundle type = new Bundle();
        type.putString("type", "add");
        addCategory.putExtras(type);
        startActivity(addCategory);
        finish();
    }
}
