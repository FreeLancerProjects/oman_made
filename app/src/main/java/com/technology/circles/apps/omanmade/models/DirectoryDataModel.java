package com.technology.circles.apps.omanmade.models;

import java.io.Serializable;
import java.util.List;

public class DirectoryDataModel implements Serializable {

    private List<DirectoryModel> dirAds;

    public List<DirectoryModel> getDirAds() {
        return dirAds;
    }

    public class DirectoryModel {
        private int id;
        private String image;
        private int web_id;
        private String title;

        public int getId() {
            return id;
        }

        public String getImage() {
            return image;
        }

        public int getWeb_id() {
            return web_id;
        }

        public String getTitle() {
            return title;
        }
    }
}
