package com.project.dayshedule.dayshedule.Models;

import java.io.Serializable;

public class DishesModel implements Serializable {

    private int gid;
    private String name;
    private String description;

    public DishesModel(int gid, String name, String description) {
        this.gid = gid;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
