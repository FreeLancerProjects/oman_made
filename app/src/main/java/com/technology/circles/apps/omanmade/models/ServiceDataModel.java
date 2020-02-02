package com.technology.circles.apps.omanmade.models;

import java.io.Serializable;
import java.util.List;

public class ServiceDataModel implements Serializable {

    private List<ServiceModel> services;

    public List<ServiceModel> getServices() {
        return services;
    }

    public static class ServiceModel implements Serializable
    {
        private int id;
        private String image;
        private String title;
        private String desc;

        public int getId() {
            return id;
        }

        public String getImage() {
            return image;
        }

        public String getTitle() {
            return title;
        }

        public String getDesc() {
            return desc;
        }
    }
}
