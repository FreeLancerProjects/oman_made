package com.technology.circles.apps.omanmade.models;

import java.io.Serializable;
import java.util.List;

public class PackageDataModel implements Serializable {

    private List<Packages> packages;

    public List<Packages> getPackages() {
        return packages;
    }

    public class Packages implements Serializable
    {
        private int id;
        private String title;
        private String package_desc;
        private String extra;
        private List<Features>features;

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getPackage_desc() {
            return package_desc;
        }

        public String getExtra() {
            return extra;
        }

        public List<Features> getFeatures() {
            return features;
        }

        public class Features implements Serializable
        {
            private int id;
            private int parent_id;
            private String title;
            private String desc;

            public int getId() {
                return id;
            }

            public int getParent_id() {
                return parent_id;
            }

            public String getTitle() {
                return title;
            }

            public String getDesc() {
                return desc;
            }
        }
    }


}
