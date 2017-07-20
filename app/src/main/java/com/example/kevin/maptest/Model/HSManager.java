package com.example.kevin.maptest.Model;


import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class HSManager implements Serializable{
    private static final String TAG = "HSManager";
    private static final String FILENAME = "sites.json";

    private ArrayList<HammockSite> sites;
    private HS2JSON serializer;

    private static HSManager sHSManager;
    private Context appContext;

    public HSManager(Context c){
        appContext = c;
        serializer = new HS2JSON(appContext, FILENAME);

        try{
            sites = serializer.loadSites();
        }catch(Exception e){
            sites = new ArrayList<HammockSite>();
            Log.e(TAG, "Error loading sites: ", e);
        }

    }
    public static HSManager get(Context c){
        if(sHSManager == null){
            sHSManager = new HSManager(c.getApplicationContext());
        }
        return sHSManager;
    }

    public ArrayList<HammockSite> getSites() {
        return sites;
    }

    public void deleteSite(HammockSite s){
        sites.remove(s);
    }

    public HammockSite getHammockSite(UUID id){
        for(HammockSite site: sites){
            if(site.getId().equals(id))
                return site;
        }
        return null;
    }
    public void addSite(HammockSite s){
        sites.add(s);
    }

    public boolean saveSites(){
        try{
            serializer.saveHSites(sites);
            Log.d(TAG, "sites saved to file");
            return true;
        } catch (Exception e){
            Log.e(TAG, "Error saving sites: ", e);
            return false;
        }
    }
    //returns true if two posititions are within .0001 degree of lat and long
    public boolean sitesTooClose(LatLng a, LatLng b){
        if(Math.abs(a.longitude - b.longitude) <= .0001 && Math.abs(a.latitude - b.latitude) <= .0001){
            return true;
        }
        return false;
    }
}
