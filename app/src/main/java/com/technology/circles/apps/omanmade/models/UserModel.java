package com.technology.circles.apps.omanmade.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private int id;
    private String name;
    private String email;
    private String mobile_code;
    private String mobile_number;
    private String avatar;
    private String city_id;
    private int is_blocked;
    private int confirmation_code;
    private int is_confirmed;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile_code() {
        return mobile_code;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getCity_id() {
        return city_id;
    }

    public int getIs_blocked() {
        return is_blocked;
    }

    public int getConfirmation_code() {
        return confirmation_code;
    }

    public int getIs_confirmed() {
        return is_confirmed;
    }
}
