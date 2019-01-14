package com.project.dayshedule.dayshedule.Dishes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.project.dayshedule.dayshedule.Models.DishesModel;
import com.project.dayshedule.dayshedule.R;
import java.util.ArrayList;

public class DishesListAdapter extends BaseAdapter {

    Activity context;
    ArrayList<DishesModel> dishes;
    private static LayoutInflater inflater = null;

    public DishesListAdapter(Activity context, ArrayList<DishesModel> dishes) {
        this.context = context;
        this.dishes = dishes;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dishes.size();
    }

    @Override
    public Object getItem(int i) {
        return dishes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;
        itemView = (itemView == null) ? inflater.inflate(R.layout.item_dishes_list, null) : itemView;
        TextView viewName = (TextView) itemView.findViewById(R.id.dishes_list_name);
        TextView viewDescription = (TextView) itemView.findViewById(R.id.dishes_list_description);
        DishesModel selectedDishes = dishes.get(i);
        viewName.setText(selectedDishes.getName());
        viewDescription.setText(selectedDishes.getDescription());

        return itemView;
    }
}
