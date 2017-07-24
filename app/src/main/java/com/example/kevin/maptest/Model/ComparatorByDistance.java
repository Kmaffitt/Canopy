package com.example.kevin.maptest.Model;


import java.util.Comparator;

public  class ComparatorByDistance implements Comparator<HammockSite>{
    private final double lat;
    private final double lng;
    private final double R = 3959.0;
    private final double C = R * Math.PI / 180.0;

    public ComparatorByDistance(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }

    public double distance(double lat1, double lng1, double lat2, double lng2){
        double dx = (lng2-lng1) * Math.cos(lat1 * Math.PI / 180.0);
        double dy = (lat2 - lat1);
        double d = C * Math.sqrt( dx * dx + dy * dy);
        return d;
    }

    @Override
    public int compare(HammockSite s1, HammockSite s2){
        double d1 = distance(lat, lng, s1.getLat(), s1.getLng());
        double d2 = distance(lat, lng, s2.getLat(), s2.getLng());
        if(d1 < d2)
            return -1;
        if(d1 > d2)
            return 1;
        else
            return 0;
    }
}
