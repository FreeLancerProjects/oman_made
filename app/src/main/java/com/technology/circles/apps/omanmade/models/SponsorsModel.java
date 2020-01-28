package com.technology.circles.apps.omanmade.models;

import java.io.Serializable;
import java.util.List;

public class SponsorsModel implements Serializable {
    private List<Sponsors> sponsors;

    public List<Sponsors> getSponsors() {
        return sponsors;
    }

    public class Sponsors {
        private int id;
        private String logo;

        public int getId() {
            return id;
        }

        public String getLogo() {
            return logo;
        }
    }
}
