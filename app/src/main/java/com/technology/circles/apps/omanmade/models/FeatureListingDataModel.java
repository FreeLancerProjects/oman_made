package com.technology.circles.apps.omanmade.models;

import java.io.Serializable;
import java.util.List;

public class FeatureListingDataModel implements Serializable {

    private List<FeatureModel> featured_lists;

    public List<FeatureModel> getFeatured_lists() {
        return featured_lists;
    }

    public static class FeatureModel implements Serializable
    {
        private int id;
        private String image;
        private String name;
        private int web_id;

        public int getId() {
            return id;
        }

        public String getImage() {
            return image;
        }

        public int getWeb_id() {
            return web_id;
        }

        public String getName() {
            return name;
        }
    }
}
