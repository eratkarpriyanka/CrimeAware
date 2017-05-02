package com.orionlabs.crimeaware.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class SearchedIncident implements ClusterItem{

    private final LatLng mPosition;
    private  int mColor=255;

    public SearchedIncident(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public SearchedIncident(LatLng mPosition) {
        this.mPosition = mPosition;
    }


    public SearchedIncident(LatLng mPosition,int color) {

        this.mPosition = mPosition;
        this.mColor = color;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public int getColor(){
        return mColor;
    }
}
