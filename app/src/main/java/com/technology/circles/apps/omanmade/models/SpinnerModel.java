package com.technology.circles.apps.omanmade.models;

import java.io.Serializable;

public class SpinnerModel implements Serializable {

    private int id;
    private String name;
    private int parent;

    public SpinnerModel() {
    }

    public SpinnerModel(int id, String name, int parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getParent() {
        return parent;
    }
}
