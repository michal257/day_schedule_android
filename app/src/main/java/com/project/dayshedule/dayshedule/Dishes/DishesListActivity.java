package com.project.dayshedule.dayshedule.Dishes;

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
import com.project.dayshedule.dayshedule.Models.DishesModel;
import com.project.dayshedule.dayshedule.R;
import com.project.dayshedule.dayshedule.RequestData;
import com.project.dayshedule.dayshedule.RequestDataParameters;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DishesListActivity extends AppCompatActivity {

    private ListView lvDishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_list);
        setStatusBar();

        lvDishes = (ListView) findViewById(R.id.listView_dishes);

        lvDishes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DishesModel o = (DishesModel) lvDishes.getItemAtPosition(i);
                openDetailsDishActivity(o.getGID());
            }
        });

        getDishes();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setStatusBar(){
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
    }

    private void openAddDishActivity(){
        Intent addDishActivity = new Intent(DishesListActivity.this, DishesAddActivity.class);
        startActivity(addDishActivity);
        finish();
    }

    private void openDetailsDishActivity(int dishesID)
    {
        Intent detailsDishActivity = new Intent(DishesListActivity.this, DishesDetailsActivity.class);
        Bundle params = new Bundle();
        params.putInt("id", dishesID);
        params.putInt("tab", 0);
        detailsDishActivity.putExtras(params);
        startActivity(detailsDishActivity);
    }

    private void getDishes(){
        new RequestData(createGetDishesParam(), ResultType.ARRAY, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object obj) throws JSONException {
                JSONArray jArray = (JSONArray) obj;
                ArrayList<DishesModel> ArrayDishesList = new ArrayList<>();
                for (int i = 0; i < jArray.length(); i++)
                {
                    JSONObject g = (JSONObject) jArray.get(i);
                    ArrayDishesList.add(new DishesModel(Integer.parseInt(g.get("GID").toString()), g.get("Name").toString(), g.get("Description").toString()));
                }
                DishesListAdapter adapter = new DishesListAdapter(DishesListActivity.this, ArrayDishesList);
                lvDishes.setAdapter(adapter);
            }
        }).execute();
    }

    private RequestDataParameters createGetDishesParam(){
        RequestDataParameters param = new RequestDataParameters();
        param.setmContext(DishesListActivity.this);
        param.setMessage("Trwa pobieranie danych...");
        param.setUrl("DishesController.php");
        param.setReqType(RequestType.GET);
        return param;
    }

    public void addDish(View v){
        openAddDishActivity();
    }
}
