package com.example.kevin.maptest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;


import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "EditActivity";

    private Bundle bundle;

    //may be null
    private String title;
    private String desc;
    private int diam;
    private int span;
    private UUID id;

    //never null
    private double lat;
    private double lng;

    //created internally
    private LatLng loc;

    private EditText mTitleText, mDescText;
    private Spinner mTreeWidthSpinner, mTreeSpanSpinner;
    private Button mSubmitButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //get lat long of site
        Bundle bundle = this.getIntent().getExtras();

        this.lat = bundle.getDouble("lat");
        this.lng = bundle.getDouble("long");
        this.loc = new LatLng(lat, lng);

        this.title = bundle.getString("title");
        this.desc = bundle.getString("description");
        this.id = (UUID) bundle.getSerializable("UUID");
        this.diam = bundle.getInt("treeWidth");
        this.span = bundle.getInt("treeSpan");

        Log.d(TAG, "In onCreate UUID: " + id.toString() );

        //wire up UI widgets
        mTitleText = (EditText) findViewById(R.id.titleBox);
        mDescText = (EditText) findViewById(R.id.descBox);
        mTreeWidthSpinner = (Spinner) findViewById(R.id.treeDiameter);
        mTreeSpanSpinner = (Spinner) findViewById(R.id.treeSpan);
        mSubmitButton = (Button) findViewById(R.id.submitButton);

        // populate spinners
        ArrayAdapter<CharSequence> widthadapater = ArrayAdapter.createFromResource(this, R.array.tree_diameter_array, android.R.layout.simple_spinner_item);
        widthadapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTreeWidthSpinner.setAdapter(widthadapater);

        ArrayAdapter<CharSequence> spanadapter = ArrayAdapter.createFromResource(this, R.array.tree_span_array, android.R.layout.simple_spinner_item);
        spanadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTreeSpanSpinner.setAdapter(spanadapter);

        //populate text fields if not null or empty
        if(this.title != null) {
            if (!this.title.equals(""))
                mTitleText.setText(this.title);
        }

        if(this.desc != null) {
            if(!this.desc.equals(""))
                mDescText.setText(this.desc);
        }
        //set spinners to saved index val
        mTreeWidthSpinner.setSelection(this.diam);
        mTreeSpanSpinner.setSelection(this.span);

        //button listeners

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i = putData();
                setResult(RESULT_OK, i);
                finish();
            }
        });

    }
    public Intent putData(){
        Bundle bundle = new Bundle();
        //get updated text
        title  = mTitleText.getText().toString();
        desc = mDescText.getText().toString();

        //get updated spinner selections
        diam = mTreeWidthSpinner.getSelectedItemPosition();
        span = mTreeSpanSpinner.getSelectedItemPosition();

        bundle.putSerializable("UUID", id);
        bundle.putDouble("lat", lat);
        bundle.putDouble("lng", lng);
        bundle.putString("title", title);
        bundle.putString("description", desc);
        bundle.putInt("width", diam);
        bundle.putInt("span", span);

        Intent intent = new Intent();
        intent.putExtras(bundle);
        return intent;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete){
            Intent i = putData();
            setResult(10, i);
            finish();
        }
        return true;
    }




}
