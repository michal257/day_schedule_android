package com.project.dayshedule.dayshedule.Dishes;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.dayshedule.dayshedule.Enum.RequestType;
import com.project.dayshedule.dayshedule.Enum.ResultType;
import com.project.dayshedule.dayshedule.Interface.OnTaskCompleted;
import com.project.dayshedule.dayshedule.Models.DishesModel;
import com.project.dayshedule.dayshedule.R;
import com.project.dayshedule.dayshedule.RequestData;
import com.project.dayshedule.dayshedule.RequestDataParameters;
import org.json.JSONException;
import org.json.JSONObject;

public class DishesDetailsTab1 extends Fragment {

    private DishesModel dish;
    private Button btnSaveDescription;
    View tab1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tab1 = inflater.inflate(R.layout.activity_dishes_details_tab1, container, false);
        btnSaveDescription = (Button) tab1.findViewById(R.id.dishDescription_btnSaveSescription);
        btnSaveDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    updateDish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        getDishDetails();
        return tab1;
    }

    public void setDish(DishesModel dish){
        this.dish = dish;
    }

    private void getDishDetails(){
        new RequestData(createGetDishDetailsParameters(), ResultType.OBJECT, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object obj) throws JSONException {
                JSONObject jObject = (JSONObject) obj;
                String description = jObject.get("DescriptionLong").toString();
                if(!description.isEmpty() && description != "null") {
                    setEditTextDescription(description);
                }
            }
        }).execute();
    }

    private RequestDataParameters createGetDishDetailsParameters(){
        RequestDataParameters param = new RequestDataParameters();
        param.setmContext(getContext());
        param.setMessage("Trwa pobieranie danych...");
        param.setUrl("DishesController.php?dishGID=" + dish.getGID());
        param.setReqType(RequestType.GET);
        return param;
    }

    private void setEditTextDescription(String content){
        EditText editDescriptionL = (EditText) tab1.findViewById(R.id.dishDescription_editDescription);
        editDescriptionL.setText(content);
    }

    private void updateDish() throws JSONException {

        new RequestData(createUpdateDishParameters(), ResultType.OBJECT, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object obj) throws JSONException {
                JSONObject jObject = (JSONObject) obj;
                Boolean result = Boolean.parseBoolean(jObject.getString("result"));
                if (result){
                    Toast.makeText(getContext(), "Zmiany zapisane.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Nie udało się zapisać zmian!", Toast.LENGTH_LONG).show();
                }
            }
        }).execute();
    }

    private RequestDataParameters createUpdateDishParameters() throws JSONException {
        RequestDataParameters param = new RequestDataParameters();
        param.setmContext(getContext());
        param.setMessage("Trwa zapis danych...");
        param.setjObiect(createUpdateObject());
        param.setUrl("DishesController.php");
        param.setReqType(RequestType.PUT);
        return param;
    }

    private JSONObject createUpdateObject() throws JSONException {
        EditText editDescriptionL = (EditText) tab1.findViewById(R.id.dishDescription_editDescription);
        String description = editDescriptionL.getText().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("Type", "Description");
        jsonObject.accumulate("GID", dish.getGID());
        jsonObject.accumulate("Description", description);
        return jsonObject;
    }
}
