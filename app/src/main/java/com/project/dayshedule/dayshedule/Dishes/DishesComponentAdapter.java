package com.project.dayshedule.dayshedule.Dishes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.dayshedule.dayshedule.Models.DishesComponentModel;
import com.project.dayshedule.dayshedule.R;

import java.util.ArrayList;

public class DishesComponentAdapter extends BaseAdapter {

    Context context;
    ArrayList<DishesComponentModel> dishesComponent;
    private static LayoutInflater inflater = null;

    public DishesComponentAdapter(Context context, ArrayList<DishesComponentModel> dishesComponent) {
        this.context = context;
        this.dishesComponent = dishesComponent;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return dishesComponent.size();
    }

    @Override
    public Object getItem(int i) {
        return dishesComponent.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;
        itemView = (itemView == null) ? inflater.inflate(R.layout.item_dishes_component, null) : itemView;
        TextView viewName = (TextView) itemView.findViewById(R.id.item_dishesComponentName);
        TextView viewQuantity = (TextView) itemView.findViewById(R.id.item_dishesComponentQuantity);
        TextView viewUnit = (TextView) itemView.findViewById(R.id.item_dishesComponentUnit);
        DishesComponentModel selectedDishComponent = dishesComponent.get(i);

        viewName.setText(selectedDishComponent.getGroceriesName());
        viewQuantity.setText(String.valueOf(selectedDishComponent.getQuantity()));
        viewUnit.setText(selectedDishComponent.getUnit());

        return itemView;
    }
}
