package com.example.kevin.maptest.Model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class HS2JSON {
    private Context mContext;
    private String filename;

    public HS2JSON(Context c, String f){
        mContext = c;
        filename = f;
    }

    public ArrayList<HammockSite> loadSites() throws IOException, JSONException{
        ArrayList<HammockSite> sites = new ArrayList<HammockSite>();
        BufferedReader reader= null;
        try{
            //Open and read the file into a StringBuilder
            InputStream in = mContext.openFileInput(filename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                jsonString.append(line);
            }
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for(int i = 0; i < array.length(); i++){
                sites.add(new HammockSite(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e){
            //ignore, happens for first time use
        } finally {
            if (reader != null)
                reader.close();
        }
        return sites;
    }

    public void saveHSites(ArrayList<HammockSite> sites) throws IOException, JSONException {
        JSONArray array = new JSONArray();
        for(HammockSite s : sites){
            array.put(s.toJSON());
        }

        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if(writer != null){
                writer.close();
            }
        }
    }
}
