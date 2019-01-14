package com.project.dayshedule.dayshedule.Models;

public class DishesComponentModel {

    private int gid;
    private String groceriesName;
    private float quantity;
    private String unit;

    public DishesComponentModel(int gid, String groceriesName, float quantity, String unit) {
        this.gid = gid;
        this.groceriesName = groceriesName;
        this.quantity = quantity;
        this.unit = unit;
    }

    public int getGID() {
        return gid;
    }

    public void setGID(int gid) {
        this.gid = gid;
    }

    public String getGroceriesName() {
        return groceriesName;
    }

    public void setGroceriesName(String groceriesName) {
        this.groceriesName = groceriesName;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
