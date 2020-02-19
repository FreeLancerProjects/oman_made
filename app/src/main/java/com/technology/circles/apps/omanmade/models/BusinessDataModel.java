package com.technology.circles.apps.omanmade.models;

import java.io.Serializable;
import java.util.Map;

public class BusinessDataModel implements Serializable {
    private int id;
    private Title title;
    private int featured_media;
    private Content content;
    private String imagePath;
    private Cmb2 cmb2;


    public int getId() {
        return id;
    }

    public Title getTitle() {
        return title;
    }

    public Content getContent() {
        return content;
    }

    public int getFeatured_media() {
        return featured_media;
    }

    public Cmb2 getCmb2() {
        return cmb2;
    }

    public static class Title implements Serializable
    {
        private String rendered;

        public String getRendered() {
            return rendered;
        }
    }

    public static class Content implements Serializable
    {
        private String rendered;

        public String getRendered() {
            return rendered;
        }
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public class Cmb2 implements Serializable
    {
        private Listing_Business_General listing_business_general;

        private Opening_Hour listing_business_opening_hours;

        private Listing_Business_Contact listing_business_contact;

        private Listing_Business_Social listing_business_social;

        private Listing_Business_Location listing_business_location;

        private Listing_Business_Branding listing_business_branding;

        private Listing_Gallery listing_gallery;

        public Listing_Business_General getListing_business_general() {
            return listing_business_general;
        }

        public Listing_Gallery getListing_gallery() {
            return listing_gallery;
        }

        public Opening_Hour getListing_business_opening_hours() {
            return listing_business_opening_hours;
        }

        public Listing_Business_Contact getListing_business_contact() {
            return listing_business_contact;
        }

        public Listing_Business_Social getListing_business_social() {
            return listing_business_social;
        }

        public Listing_Business_Location getListing_business_location() {
            return listing_business_location;
        }

        public Listing_Business_Branding getListing_business_branding() {
            return listing_business_branding;
        }
    }
    public static class Opening_Hour implements Serializable
    {
        private Object listing_opening_hours;

        public Object getListing_opening_hours() {
            return listing_opening_hours;
        }
    }

    public static class OpeningHourList implements Serializable
    {
        private String listing_day;
        private String listing_time_from;
        private String listing_time_to;
        private String listing_custom;

        public OpeningHourList() {
        }

        public OpeningHourList(String listing_day, String listing_time_from, String listing_time_to, String listing_custom) {
            this.listing_day = listing_day;
            this.listing_time_from = listing_time_from;
            this.listing_time_to = listing_time_to;
            this.listing_custom = listing_custom;
        }

        public void setListing_day(String listing_day) {
            this.listing_day = listing_day;
        }

        public void setListing_time_from(String listing_time_from) {
            this.listing_time_from = listing_time_from;
        }

        public void setListing_time_to(String listing_time_to) {
            this.listing_time_to = listing_time_to;
        }

        public void setListing_custom(String listing_custom) {
            this.listing_custom = listing_custom;
        }

        public String getListing_day() {
            return listing_day;
        }

        public String getListing_time_from() {
            return listing_time_from;
        }

        public String getListing_time_to() {
            return listing_time_to;
        }

        public String getListing_custom() {
            return listing_custom;
        }
    }

    public static class Listing_Business_Contact implements Serializable
    {
        private String listing_email;
        private String listing_phone;
        private String listing_website;
        private String listing_person;
        private String listing_address;

        public String getListing_email() {
            return listing_email;
        }

        public String getListing_phone() {
            return listing_phone;
        }

        public String getListing_website() {
            return listing_website;
        }

        public String getListing_person() {
            return listing_person;
        }

        public String getListing_address() {
            return listing_address;
        }
    }

    public class Listing_Business_Social implements Serializable
    {
        private String listing_facebook;
        private String listing_twitter;
        private String listing_google;
        private String listing_instagram;
        private String listing_vimeo;
        private String listing_youtube;
        private String listing_linkedin;
        private String listing_dribbble;
        private String listing_skype;
        private String listing_foursquare;
        private String listing_behance;


        public String getListing_facebook() {
            return listing_facebook;
        }

        public String getListing_twitter() {
            return listing_twitter;
        }

        public String getListing_google() {
            return listing_google;
        }

        public String getListing_instagram() {
            return listing_instagram;
        }

        public String getListing_vimeo() {
            return listing_vimeo;
        }

        public String getListing_youtube() {
            return listing_youtube;
        }

        public String getListing_linkedin() {
            return listing_linkedin;
        }

        public String getListing_dribbble() {
            return listing_dribbble;
        }

        public String getListing_skype() {
            return listing_skype;
        }

        public String getListing_foursquare() {
            return listing_foursquare;
        }

        public String getListing_behance() {
            return listing_behance;
        }
    }

    public static class Listing_Business_Location implements Serializable
    {
        private Object listing_locations;
        private Listing_Map_Location listing_map_location;
        public Object getListing_locations() {
            return listing_locations;
        }

        public Listing_Map_Location getListing_map_location() {
            return listing_map_location;
        }
    }

    public static class Listing_Map_Location implements Serializable
    {
        private String address;
        private String latitude;
        private String longitude;

        public String getAddress() {
            return address;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }
    }

    public static class Listing_Business_General implements Serializable
    {
        private String listing_title;
        private String listing_featured_image;
        private String listing_description;

        public String getListing_title() {
            return listing_title;
        }

        public String getListing_description() {
            return listing_description;
        }

        public String getListing_featured_image() {
            return listing_featured_image;
        }
    }

    public static class Listing_Business_Branding implements Serializable
    {
        private String listing_slogan;
        private String listing_brand_color;
        private String listing_logo;

        public String getListing_slogan() {
            return listing_slogan;
        }

        public String getListing_brand_color() {
            return listing_brand_color;
        }

        public String getListing_logo() {
            return listing_logo;
        }
    }
    public static class Listing_Gallery implements Serializable
    {
        private Map<String,String> map;

        public Map<String, String> getMap() {
            return map;
        }
    }
}
