package com.orionlabs.crimeaware.utils.maputils;


import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.orionlabs.crimeaware.models.SearchedIncident;

public class IncidentItemRenderer extends DefaultClusterRenderer<SearchedIncident>{

    public IncidentItemRenderer(Context context, GoogleMap map, ClusterManager<SearchedIncident> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(SearchedIncident crimeIncident, MarkerOptions markerOptions) {
        // Customize the marker here

        markerOptions
                .position(crimeIncident.getPosition())
                .icon(BitmapDescriptorFactory.defaultMarker());

    }

    @Override
    protected void onBeforeClusterRendered(Cluster<SearchedIncident> cluster, MarkerOptions markerOptions) {

        markerOptions
                .icon(BitmapDescriptorFactory.defaultMarker());

    }
}
