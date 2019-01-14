package com.project.dayshedule.dayshedule.Models;

public class GroceriesModel {

    private int gid;
    private String name;
    private String category;

    public GroceriesModel(int gid, String name, String category) {
        this.gid = gid;
        this.name = name;
        this.category = category;
    }

    public int getGID() {
        return gid;
    }

    public void setGID(int GID) {
        this.gid = GID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
