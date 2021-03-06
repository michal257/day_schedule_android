package com.project.dayshedule.dayshedule.Groceries;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.dayshedule.dayshedule.Models.GroceriesModel;
import com.project.dayshedule.dayshedule.R;

import java.util.ArrayList;

public class GroceriesListAdapter extends BaseAdapter {

    Activity context;
    ArrayList<GroceriesModel> groceries;
    private static LayoutInflater inflater = null;

    public GroceriesListAdapter(Activity context, ArrayList<GroceriesModel> groceries) {
        this.context = context;
        this.groceries = groceries;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return groceries.size();
    }

    @Override
    public Object getItem(int i) {
        return groceries.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View itemView = view;
        itemView = (itemView == null) ? inflater.inflate(R.layout.item_groceries_list, null) : itemView;
        TextView viewName = (TextView) itemView.findViewById(R.id.groceries_list_name);
        TextView viewCategory = (TextView) itemView.findViewById(R.id.groceries_list_category);
        GroceriesModel selectedGroceries = groceries.get(i);
        viewName.setText(selectedGroceries.getName());
        if (selectedGroceries.getCategory() == "null")
            viewCategory.setText("");
        else
            viewCategory.setText(selectedGroceries.getCategory());

        return itemView;
    }
}
