package com.example.kevin.maptest.Model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kevin.maptest.R;

import java.util.ArrayList;


public class SiteAdapter extends BaseAdapter {

    private static final String LOG_TAG = SiteAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<HammockSite> sites;
    //using for distance method
    private ComparatorByDistance comp = new ComparatorByDistance(0, 0);
    private double curLat;
    private double curLong;
    private String title;

    public SiteAdapter(Context context, ArrayList<HammockSite> sites, double curLat, double curLong) {
        this.context = context;
        this.sites = sites;
        this.curLat = curLat;
        this.curLong = curLong;
    }

    @Override
    public int getCount() {
        return sites.size();
    }

    @Override
    public Object getItem(int position) {
        return sites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.activity_list, null);
        }

        TextView mTitleBox = (TextView) convertView.findViewById(R.id.listItem);
        TextView mDistanceBox = (TextView) convertView.findViewById(R.id.listItemDistance);


        HammockSite site = sites.get(position);
        double distance = comp.distance(site.getLat(), site.getLng(), this.curLat, this.curLong);


        title = site.getTitle();
        if(title.length() >= 24)
            title = title.substring(0, 21) + "...";


        mTitleBox.setText(title);
        mDistanceBox.setText(String.format("%.1f", distance) + " miles away");

        return convertView;
    }
}

