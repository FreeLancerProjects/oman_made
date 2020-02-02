package com.technology.circles.apps.omanmade.models;

import java.io.Serializable;
import java.util.List;

public class FeaturedCategoryDataModel implements Serializable {
    private List<FeaturedCategoryModel> featured_cats;

    public List<FeaturedCategoryModel> getFeatured_cats() {
        return featured_cats;
    }

    public class FeaturedCategoryModel implements Serializable {
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
