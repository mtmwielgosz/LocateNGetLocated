package com.locateandgetlocated.locategetlocated;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kamil on 15.04.2016.
 */
public class Place {
    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public Place(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    LatLng coordinates;
    String date;
    String hour;

    public Place(LatLng coordinates, String date,  String hour) {
        this.hour = hour;
        this.coordinates = coordinates;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }




}
