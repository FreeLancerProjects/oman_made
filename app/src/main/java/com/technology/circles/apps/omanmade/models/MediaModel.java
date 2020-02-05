package com.technology.circles.apps.omanmade.models;

import java.io.Serializable;

public class MediaModel implements Serializable {

    private int id;
    private Guide guid;

    public int getId() {
        return id;
    }

    public Guide getGuid() {
        return guid;
    }

    public class Guide implements Serializable
    {
        private String rendered;

        public String getRendered() {
            return rendered;
        }
    }
}
