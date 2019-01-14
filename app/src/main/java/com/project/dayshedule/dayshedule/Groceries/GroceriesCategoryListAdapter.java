package com.project.dayshedule.dayshedule.Groceries;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.dayshedule.dayshedule.Models.GroceriesCategoryModel;
import com.project.dayshedule.dayshedule.R;

import java.util.ArrayList;

public class GroceriesCategoryListAdapter extends BaseAdapter {

    Activity context;
    ArrayList<GroceriesCategoryModel> groceriesCategory;
    private static LayoutInflater inflater = null;

    public GroceriesCategoryListAdapter(Activity context, ArrayList<GroceriesCategoryModel> groceries) {
        this.context = context;
        this.groceriesCategory = groceries;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return groceriesCategory.size();
    }

    @Override
    public Object getItem(int i) {
        return groceriesCategory.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;
        itemView = (itemView == null) ? inflater.inflate(R.layout.item_groceries_category_list, null) : itemView;
        TextView viewName = (TextView) itemView.findViewById(R.id.groceries_list_name);
        GroceriesCategoryModel selectedGroceries = groceriesCategory.get(i);
        viewName.setText(selectedGroceries.getName());

        return itemView;
    }
}
