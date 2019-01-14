package com.project.dayshedule.dayshedule;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.project.dayshedule.dayshedule.Dishes.DishesListActivity;
import com.project.dayshedule.dayshedule.Groceries.GroceriesListActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setStatusBar();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setStatusBar() {
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
    }

    public void openGroceriesList(View v){
        Intent groceriesList = new Intent(this, GroceriesListActivity.class);
        startActivity(groceriesList);
    }

    public void openDishesList(View v){
        Intent dishesList = new Intent(this, DishesListActivity.class);
        startActivity(dishesList);
    }
}
