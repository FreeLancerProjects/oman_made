package com.technology.circles.apps.omanmade.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterLocation implements ClusterItem {

    private String address;
    private LatLng latLng;

    public ClusterLocation( String address, LatLng latLng) {
        this.address = address;
        this.latLng = latLng;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return address;
    }


    @Override
    public String getSnippet() {
        return address;
    }
}
