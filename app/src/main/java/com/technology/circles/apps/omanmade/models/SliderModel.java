package com.technology.circles.apps.omanmade.models;

import java.io.Serializable;
import java.util.List;

public class SliderModel implements Serializable {
    private List<Slides> slides;

    public List<Slides> getSlides() {
        return slides;
    }

    public  class  Slides implements Serializable
    {
        private int id;
           private String image;
                   private String web_id;

        public int getId() {
            return id;
        }

        public String getImage() {
            return image;
        }

        public String getWeb_id() {
            return web_id;
        }
    }
}
