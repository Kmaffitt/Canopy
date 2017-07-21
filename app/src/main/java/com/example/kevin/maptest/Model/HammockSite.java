package com.example.kevin.maptest.Model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.UUID;

public class HammockSite implements Serializable{

    private String title = "";
    private String description = "";
    private int treeWidth = 0;
    private int treeDist = 0;
    //private TreeAttributes ta;

    //private LatLng loc;
    private double lat;
    private double lng;
    private UUID id;

    private static final String JSON_ID = "id";
    private static final String JSON_LAT = "lat";
    private static final String JSON_LNG = "lng";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DESC = "description";
    private static final String JSON_TREE_WIDTH = "treeWidth";
    private static final String JSON_TREE_DISTANCE = "treeDist";


    public HammockSite(double lat, double lng, String title, String description, int treeWidth, int treeSpan){
        //this.loc = loc;
        this.lat = lat;
        this.lng = lng;
        id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        //this.ta = ta;
        this.treeWidth = treeWidth;
        this.treeDist = treeSpan;
    }

    public HammockSite(JSONObject json) throws JSONException{
        this.id = UUID.fromString(json.getString(JSON_ID));
        //this.loc = new LatLng(json.getDouble(JSON_LAT), json.getDouble(JSON_LNG));
        this.lat = json.getDouble(JSON_LAT);
        this.lng =  json.getDouble(JSON_LNG);
        try {
            this.title = json.getString(JSON_TITLE);
        }catch(Exception e){
            this.title = "";
        }
        try {
            this.description = json.getString(JSON_DESC);
        }catch(Exception e){
            this.description = "";
        }
        //this.ta = new TreeAttributes(json.getInt(JSON_TREE_WIDTH), json.getInt(JSON_TREE_DISTANCE));//
        this.treeWidth = json.getInt(JSON_TREE_WIDTH);
        this.treeDist = json.getInt(JSON_TREE_DISTANCE);
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID, id.toString());
        json.put(JSON_LAT, lat);
        json.put(JSON_LNG, lng);
        json.put(JSON_TITLE, title);
        json.put(JSON_DESC, description);
        json.put(JSON_TREE_WIDTH, treeWidth);//
        json.put(JSON_TREE_DISTANCE, treeDist);//
        return json;
    }

    //public LatLng getLoc() {
        //return loc;
    //}

    //public void setLoc(LatLng loc) {
        //this.loc = loc;
    //}

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTreeWidth() {
        return treeWidth;
    }

    public void setTreeWidth(int treeWidth) {
        this.treeWidth = treeWidth;
    }

    public int getTreeDist() {
        return treeDist;
    }

    public void setTreeDist(int treeDist) {
        this.treeDist = treeDist;
    }

    public boolean hasTitle(){
        if(this.title != null)
            return true;
        return false;
    }
    public boolean hasDescription(){
        if(this.description != null)
            return true;
        return false;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString(){
        return "Hammock Site " + id.toString() + " at Lat: " + lat + " Long: " + lng;
    }
}
