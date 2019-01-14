package com.project.dayshedule.dayshedule.Dishes;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.project.dayshedule.dayshedule.Enum.RequestType;
import com.project.dayshedule.dayshedule.Enum.ResultType;
import com.project.dayshedule.dayshedule.Interface.OnTaskCompleted;
import com.project.dayshedule.dayshedule.Models.DishesComponentModel;
import com.project.dayshedule.dayshedule.Models.DishesModel;
import com.project.dayshedule.dayshedule.R;
import com.project.dayshedule.dayshedule.RequestData;
import com.project.dayshedule.dayshedule.RequestDataParameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class DishesDetailsTab2 extends Fragment {

    private DishesModel dish;
    private ArrayList<DishesComponentModel> ArrayDishesComponentList = new ArrayList<>();
    private ListView lvDishesComponent;
    private Button btnAddComponent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View tab2 = inflater.inflate(R.layout.activity_dishes_details_tab2, container, false);
        lvDishesComponent = (ListView) tab2.findViewById(R.id.dishesDetails_componentList);
        btnAddComponent = (Button) tab2.findViewById(R.id.dishesComponentList_addNewComponent);
        btnAddComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddComponent();
            }
        });

        lvDishesComponent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickComponentElement(i);
            }
        });
        getDishComponent();

        return tab2;
    }

    private void clickComponentElement(int i){
        final DishesComponentModel selectComponent = (DishesComponentModel) lvDishesComponent.getItemAtPosition(i);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage("Czy chcesz usunąć składnik: " + selectComponent.getGroceriesName());
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Tak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            deleteComponent(selectComponent.getGID());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Nie",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder1.create();
        alert.show();
    }

    public void setDish(DishesModel dish){
        this.dish = dish;
    }

    private void openAddComponent(){
        Intent addComponet = new Intent(getContext(), DishesComponentAddActivity.class);
        addComponet.putExtra("DishModel", (Serializable) dish);
        startActivity(addComponet);
        getActivity().finish();
    }

    private void getDishComponent(){
        new RequestData(creategetDishComponentParam(), ResultType.ARRAY, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object obj) throws JSONException {
                JSONArray jArray = (JSONArray) obj;
                for (int i = 0; i < jArray.length(); i++)
                {
                    JSONObject g = (JSONObject) jArray.get(i);
                    ArrayDishesComponentList.add(new DishesComponentModel(Integer.parseInt(g.get("GID").toString()), g.get("GroceriesName").toString(), Float.parseFloat(g.get("Quantity").toString()), g.get("Unit").toString()));
                }
                DishesComponentAdapter adapter = new DishesComponentAdapter(getContext(), ArrayDishesComponentList);
                lvDishesComponent.setAdapter(adapter);
            }
        }).execute();
    }

    private RequestDataParameters creategetDishComponentParam(){
        RequestDataParameters param = new RequestDataParameters();
        param.setmContext(getContext());
        param.setMessage("Trwa pobieranie danych...");
        param.setUrl("DishComponentsController.php?dishGID=" + dish.getGID());
        param.setReqType(RequestType.GET);
        return param;
    }

    private void deleteComponent(int deleteGID) throws JSONException {
        new RequestData(createDeleteComponentParam(deleteGID), ResultType.OBJECT, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object obj) throws JSONException {
                JSONObject jObject = (JSONObject) obj;
                Boolean result = Boolean.parseBoolean(jObject.getString("result"));
                if (result){
                    ArrayDishesComponentList.clear();
                    lvDishesComponent.setAdapter(null);
                    getDishComponent();
                } else {
                    Toast.makeText(getContext(), "Nie udało się zapisać zmian!", Toast.LENGTH_LONG).show();
                }
            }
        }).execute();
    }

    private RequestDataParameters createDeleteComponentParam(int deleteGID) throws JSONException {
        RequestDataParameters param = new RequestDataParameters();
        param.setmContext(getContext());
        param.setMessage("Trwa usuwanie składnika...");
        param.setjObiect(createDeleteComponentObject(deleteGID));
        param.setUrl("DishComponentsController.php");
        param.setReqType(RequestType.DELETE);
        return param;
    }

    private JSONObject createDeleteComponentObject(int deleteGID) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("GID", deleteGID);
        return jsonObject;
    }
}
