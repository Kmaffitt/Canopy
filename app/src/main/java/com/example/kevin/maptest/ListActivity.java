package com.example.kevin.maptest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kevin.maptest.Model.ComparatorByDistance;
import com.example.kevin.maptest.Model.HammockSite;
import com.example.kevin.maptest.Model.SiteAdapter;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Array;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private Context appContext = this;
    private ArrayList<HammockSite> sites;
    public double currentLat;
    public double currentLng;
    private SiteAdapter siteAdapter;
    public static final int MAPS_ACTIVITY_REQ_CODE = 1;
    public static final int LIST_ACTIVITY_REQ_CODE = 9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_container);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null) {
            sites = (ArrayList<HammockSite>) bundle.getSerializable("sites");
            this.currentLat = bundle.getDouble("lat");
            this.currentLng = bundle.getDouble("long");
        }
        ComparatorByDistance comp = new ComparatorByDistance(currentLat, currentLng);
        //noinspection Since15
        sites.sort(comp);
        //Toast.makeText(this, sites.get(0).toString(), Toast.LENGTH_LONG).show();
        siteAdapter = new SiteAdapter(this, sites, currentLat, currentLng);

        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(siteAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HammockSite site = (HammockSite) listView.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("site", site);

                Intent i = new Intent(ListActivity.this, EditActivity.class);
                i.putExtras(bundle);
                startActivityForResult(i, 1);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;


        HammockSite site = (HammockSite) data.getSerializableExtra("site");
        Bundle bundle = new Bundle();
        bundle.putSerializable("site", site);

        Intent i = new Intent();
        i.putExtras(bundle);

        //10 = delete post button pushed in EditActivity
        if (resultCode == 10) {
            setResult(resultCode, i);
            finish();

        }else if(resultCode == RESULT_OK){
            setResult(resultCode, i);
            finish();
        }
    }
    //sets up options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.mapView){
            Bundle bundle = new Bundle();
            bundle.putSerializable("sites", sites);
            Intent i = new Intent(ListActivity.this, MapsActivity.class);
            i.putExtras(bundle);
            startActivityForResult(i, 1);
        }
        return true;
    }


}
