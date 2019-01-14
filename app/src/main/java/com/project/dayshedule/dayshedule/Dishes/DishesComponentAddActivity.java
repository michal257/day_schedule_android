package com.project.dayshedule.dayshedule.Dishes;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.dayshedule.dayshedule.Enum.RequestType;
import com.project.dayshedule.dayshedule.Enum.ResultType;
import com.project.dayshedule.dayshedule.Groceries.GroceriesListAdapter;
import com.project.dayshedule.dayshedule.Interface.OnTaskCompleted;
import com.project.dayshedule.dayshedule.Models.DishesModel;
import com.project.dayshedule.dayshedule.Models.GroceriesModel;
import com.project.dayshedule.dayshedule.R;
import com.project.dayshedule.dayshedule.RequestData;
import com.project.dayshedule.dayshedule.RequestDataParameters;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class DishesComponentAddActivity extends AppCompatActivity {

    private ListView lvGroceries;
    private DishesModel dish;
    private LinearLayout quantityBox;
    private int groceriesGID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_component_add);
        setStatusBar();
        implementControllers();

        Intent intent = getIntent();
        dish = (DishesModel) intent.getSerializableExtra("DishModel");
        setHeaderName();
        getGroceries();

        lvGroceries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickAddElement(i);
            }
        });
    }

    private void implementControllers(){
        quantityBox = (LinearLayout) findViewById(R.id.dishesComponentAdd_quantityBox);
        lvGroceries = (ListView) findViewById(R.id.dishComponentAdd_groceriesList);
    }

    private void clickAddElement(int i){
        GroceriesModel gro = (GroceriesModel) lvGroceries.getItemAtPosition(i);
        quantityBox.setVisibility(View.VISIBLE);
        lvGroceries.setVisibility(View.GONE);
        setGrocName(gro.getName());
        groceriesGID = gro.getGID();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setStatusBar(){
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
    }

    private void close(){
        Intent detailsDishActivity = new Intent(DishesComponentAddActivity.this, DishesDetailsActivity.class);
        Bundle params = new Bundle();
        params.putInt("id", dish.getGID());
        params.putInt("tab", 1);
        detailsDishActivity.putExtras(params);
        startActivity(detailsDishActivity);
        finish();
    }
    @Override
    public void onBackPressed() {
        close();
    }

    private void getGroceries(){
        new RequestData(createGetGroceriesParameters(), ResultType.ARRAY, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) throws JSONException {
                JSONArray jArray = (JSONArray) object;
                ArrayList<GroceriesModel> ArrayGroceriesList1 = new ArrayList<>();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject g = (JSONObject) jArray.get(i);
                    ArrayGroceriesList1.add(new GroceriesModel(Integer.parseInt(g.get("GID").toString()), g.get("Name").toString(), g.get("Category").toString()));
                }
                GroceriesListAdapter adapter1 = new GroceriesListAdapter(DishesComponentAddActivity.this, ArrayGroceriesList1);
                lvGroceries.setAdapter(adapter1);
            }
        }).execute();
    }

    private RequestDataParameters createGetGroceriesParameters(){
        RequestDataParameters param = new RequestDataParameters();
        param.setmContext(DishesComponentAddActivity.this);
        param.setMessage("Trwa pobieranie danych...");
        param.setUrl("GroceriesController.php");
        param.setReqType(RequestType.GET);
        return param;
    }

    private void SaveComponent() throws JSONException {
        new RequestData(createSaveComponentParameters(createNewComponentObject()), ResultType.OBJECT, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) throws JSONException {
                JSONObject jObject = (JSONObject) object ;
                Boolean result = Boolean.parseBoolean(jObject.getString("result"));
                if (result){
                    Toast.makeText(DishesComponentAddActivity.this, "Składnik dodany", Toast.LENGTH_LONG).show();
                    close();
                }else {
                    Toast.makeText(DishesComponentAddActivity.this, "Nie udało się dodać produktu!", Toast.LENGTH_LONG).show();
                }
            }
        }).execute();
    }

    private RequestDataParameters createSaveComponentParameters(JSONObject jObiect){
        RequestDataParameters param = new RequestDataParameters();
        param.setmContext(DishesComponentAddActivity.this);
        param.setMessage("Trwa zapis składnika...");
        param.setUrl("DishComponentsController.php");
        param.setReqType(RequestType.POST);
        param.setjObiect(jObiect);
        return param;
    }

    private JSONObject createNewComponentObject() throws JSONException {

        EditText editQuantity = (EditText) findViewById(R.id.dishComponentAdd_editQuantity);
        float quantity = Integer.parseInt(editQuantity.getText().toString());
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("DishGID", dish.getGID());
        jsonObject.accumulate("GrocGID", groceriesGID);
        jsonObject.accumulate("Quantity", quantity);
        return jsonObject;
    }

    public void saveNewComponent(View v) throws JSONException {
        SaveComponent ();
    }

    public void thisClose(View v){
        close();
    }

    private void setHeaderName(){
        TextView headerName = (TextView) findViewById(R.id.dishComponentAdd_headerName);
        headerName.setText(dish.getName());
    }

    private void setGrocName(String name){
        TextView grocName = (TextView) findViewById(R.id.dishComponentAdd_groceresName);
        grocName.setText(name);
    }
}
