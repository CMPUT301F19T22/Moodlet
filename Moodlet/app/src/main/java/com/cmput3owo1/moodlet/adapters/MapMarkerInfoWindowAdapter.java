package com.cmput3owo1.moodlet.adapters;

import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MapMarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    /**
     * This function customizes what is displayed in the map marker's info window
     * @param marker
     * @return
     */
    @Override
    public View getInfoWindow(Marker marker) {
        // TODO
        return null;
    }

    /**
     * This function customizes what is displayed in the map marker's info window but also
     * keeps the default info window frame and background
     * @param marker
     * @return
     */
    @Override
    public View getInfoContents(Marker marker) {
        // TODO
        return null;
    }
}
