package com.example.kevin.maptest;


import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.kevin.maptest.Model.HSManager;
import com.example.kevin.maptest.Model.HammockSite;

import com.example.kevin.maptest.Model.TreeAttributes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.UUID;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener{

    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private Location mLastKnownLocation;
    private CameraPosition mCameraPosition;
    private GoogleApiClient googleApiClient;

    private HSManager hsManager;
    private ArrayList<HammockSite>sites;

    //constants, St. Louis is default loc
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final int DEFAULT_ZOOM = 15;
    public static final int REQUEST_LOCATION_CODE = 99;
    private static final LatLng mDefaultLocation = new LatLng(38.6270,90.1994);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "In onCreate");
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        //get hammock site manager singleton for data management
        hsManager = HSManager.get(getApplicationContext());

        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "In onMapReady");

        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
        }


    }
    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint){
        Log.d(TAG, "In onConnected");

        //once connected, if we have loc permissions, get location
          if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
              mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

              if (mCameraPosition != null) {
                  mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
              } else if (mLastKnownLocation != null) {
                  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                          new LatLng(mLastKnownLocation.getLatitude(),
                                  mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
              } else {
                  Log.d(TAG, "Current location is null. Using defaults.");
                  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                  mMap.getUiSettings().setMyLocationButtonEnabled(false);
              }
          }
        //get saved hammock sites
        sites = hsManager.getSites();

        //place markers
        for(HammockSite site : sites){
            Marker marker = mMap.addMarker(new MarkerOptions().position(site.getLoc()));
            //associate site with its marker
            marker.setTag(site);
        }
        // Set a listener for marker clicks
        mMap.setOnMarkerClickListener(this);

    }
    //returns lat and long of device's current pos
    private LatLng getCurrentPosition(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }
        return new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
    }

    @Override
    public void onConnectionSuspended(int cause){
        Log.d(TAG, "Connection suspended. Cause id: " + cause);
    }
    @Override
    public void onConnectionFailed(ConnectionResult result){
        Log.d(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // checks for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_LOCATION_CODE );
                            }
                        })
                        .create().show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_CODE );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    //Not granted permission, can't use app
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                    //kill app
                    this.finishAffinity();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    //sets up options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    //handles menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.newSite){
            LatLng loc = getCurrentPosition();
            boolean tooClose = false;

            //check if too close to other sites
            for(HammockSite s : sites){
                if(hsManager.sitesTooClose(s.getLoc(), loc))
                    tooClose = true;
            }

            if(!tooClose) {
                HammockSite hs = new HammockSite(loc, "", "", new TreeAttributes(0, 0));
                hsManager.addSite(hs);


                Bundle bundle = new Bundle();
                bundle.putDouble("lat", loc.latitude);
                bundle.putDouble("long", loc.longitude);
                bundle.putSerializable("UUID", hs.getId());
                bundle.putString("description", hs.getDescription());
                bundle.putInt("treeWidth", hs.getTreeWidth());
                bundle.putInt("treeSpan", hs.getTreeDist());
                Intent i = new Intent(MapsActivity.this, EditActivity.class);
                i.putExtras(bundle);
                startActivityForResult(i,0);
            }else{
                Toast.makeText(this, "New site is too close to an existing one!", Toast.LENGTH_LONG).show();
            }
        }else if(item.getItemId() == R.id.listView){
            for(HammockSite site : sites){

            }
            Intent i = new Intent(MapsActivity.this, ListActivity.class);
            startActivity(i);
        }
        return true;
    }
    //handles marker clicks on map
    public boolean onMarkerClick(final Marker marker) {
        HammockSite site = (HammockSite)marker.getTag();
        LatLng loc = site.getLoc();

        if(loc != null) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("UUID", site.getId());
                bundle.putDouble("lat", loc.latitude);
                bundle.putDouble("long", loc.longitude);
                bundle.putString("title", site.getTitle());
                bundle.putString("description", site.getDescription());
                bundle.putInt("treeWidth", site.getTreeWidth());
                bundle.putInt("treeSpan", site.getTreeDist());
                Intent i = new Intent(MapsActivity.this, EditActivity.class);
                i.putExtras(bundle);
                startActivityForResult(i, 0);
            }

        return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null)
            return;

        //10 = delete post button pushed in EditActivity
        if(resultCode == 10){
            HammockSite site = hsManager.getHammockSite((UUID) data.getSerializableExtra("UUID"));
            hsManager.deleteSite(site);
            recreate();
        }else if(resultCode == RESULT_OK){
            //Toast.makeText(this, "Got results back", Toast.LENGTH_LONG).show();
            HammockSite site = hsManager.getHammockSite((UUID) data.getSerializableExtra("UUID"));
            site.setTitle(data.getStringExtra("title"));
            site.setDescription(data.getStringExtra("description"));
            site.setTreeWidth(data.getIntExtra("width", 0));
            site.setTreeDist(data.getIntExtra("span", 0));

            hsManager.saveSites();

            Log.d(TAG, "in onActivityResult UUID: " + site.getId().toString());
            recreate();
        }
    }
    @Override
    public void onStop(){
        super.onStop();
        hsManager.saveSites();

    }

}
