package com.example.kevin.maptest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class ListActivity extends android.app.ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] values = new String[] {"Test1","Test2","Test3","Test4","Test5","Test6","Test7","Test8"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_list, R.id.listItem, values);
        setListAdapter(adapter);
    }
}
