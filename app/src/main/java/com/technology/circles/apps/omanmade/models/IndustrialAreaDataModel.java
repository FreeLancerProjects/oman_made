package com.technology.circles.apps.omanmade.models;

import java.io.Serializable;
import java.util.List;

public class IndustrialAreaDataModel implements Serializable {
    private List<IndustrialAreaModel> industrialAreas;

    public List<IndustrialAreaModel> getIndustrialAreas() {
        return industrialAreas;
    }

    public class IndustrialAreaModel implements Serializable {
        private int id;
        private String image;
        private String web_id;
        private String name;

        public int getId() {
            return id;
        }

        public String getImage() {
            return image;
        }

        public String getWeb_id() {
            return web_id;
        }

        public String getName() {
            return name;
        }
    }
}
