package com.example.kevin.maptest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kevin.maptest.Model.ComparatorByDistance;
import com.example.kevin.maptest.Model.HammockSite;

import java.sql.Array;
import java.util.ArrayList;

public class ListActivity extends android.app.ListActivity {
    private ArrayList<HammockSite> sites;
    private double currentLat;
    private double currentLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] values = new String[] {"Test1","Test2","Test3","Test4","Test5","Test6","Test7","Test8"};
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

        ArrayAdapter<HammockSite> adapter = new ArrayAdapter<HammockSite>(this, R.layout.activity_list, R.id.listItem, sites);
        setListAdapter(adapter);
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        HammockSite site = (HammockSite)(getListAdapter().getItem(position));
        Bundle bundle = new Bundle();
        bundle.putSerializable("site", site);

        Intent i = new Intent(ListActivity.this, EditActivity.class);
        i.putExtras(bundle);
        startActivityForResult(i, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;

        //10 = delete post button pushed in EditActivity
        if (resultCode == 10) {
            HammockSite site = (HammockSite) data.getSerializableExtra("site");
            Bundle bundle = new Bundle();
            bundle.putSerializable("site", site);

            Intent i = new Intent();
            i.putExtras(bundle);
            setResult(resultCode, i);
            finish();

        }
    }
    //sets up options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }


}
