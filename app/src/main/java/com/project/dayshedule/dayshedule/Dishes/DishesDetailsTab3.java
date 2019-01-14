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

public class DishesDetailsTab3 extends Fragment {

    private DishesModel dish;
    private Button btnSaveRecipe;
    View tab3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tab3 = inflater.inflate(R.layout.activity_dishes_details_tab3, container, false);
        btnSaveRecipe = (Button) tab3.findViewById(R.id.dishRecipe_btnSaveRecipe);

        btnSaveRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    updateDishRecipe();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        getDishRecipe();

        return tab3;
    }

    public void setDish(DishesModel dish){
        this.dish = dish;
    }


    private void getDishRecipe(){
        new RequestData(createGetDishRecipeParam(), ResultType.OBJECT, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object obj) throws JSONException {
                JSONObject jObject = (JSONObject) obj;
                String recipe = jObject.get("Recipe").toString();
                if(!recipe.isEmpty()){
                    EditText editRecipe = (EditText) tab3.findViewById(R.id.dishRecipe_editRecipe);
                    editRecipe.setText(recipe);
                }
            }
        }).execute();
    }

    private RequestDataParameters createGetDishRecipeParam(){
        RequestDataParameters param = new RequestDataParameters();
        param.setmContext(getContext());
        param.setMessage("Trwa pobieranie danych...");
        param.setUrl("DishesController.php?dishGID=" + dish.getGID());
        param.setReqType(RequestType.GET);
        return param;
    }

    private void updateDishRecipe() throws JSONException {
        new RequestData(createupdateDishRecipeParam(), ResultType.OBJECT, new OnTaskCompleted() {
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

    private RequestDataParameters createupdateDishRecipeParam() throws JSONException {
        RequestDataParameters param = new RequestDataParameters();
        param.setmContext(getContext());
        param.setMessage("Trwa zapis danych...");
        param.setUrl("DishesController.php");
        param.setReqType(RequestType.PUT);
        param.setjObiect(createUpdateRecipeObject());
        return param;
    }

    private JSONObject createUpdateRecipeObject() throws JSONException {
        EditText editRecipe = (EditText) tab3.findViewById(R.id.dishRecipe_editRecipe);
        String recipe = editRecipe.getText().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("Type", "Recipe");
        jsonObject.accumulate("GID", dish.getGID());
        jsonObject.accumulate("Recipe", recipe);
        return  jsonObject;
    }
}
