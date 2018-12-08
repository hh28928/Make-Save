package com.example.hammadhanif.cs_477_final_project;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

public class CurrentLocationMap extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "CurrentLocationMap";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private boolean mlocationpermission = true;
    private static final int request_code = 1234;
    private GoogleMap mMap;
    double lat;
    double longitude;
    String lati;
    String longi;

    private FusedLocationProviderClient mFusedLocationProviderClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location_map);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Intent intent = getIntent();
        lat = intent.getDoubleExtra("Value1",0.0);
       longitude = intent.getDoubleExtra("Value2",0.0);
        getLocationPermission();

    }


    private void MoveCamera(LatLng latlng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(CurrentLocationMap.this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);
        }
        if (mlocationpermission) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

//            Intent intent = getIntent();
//            double lat = intent.getDoubleExtra("Value1",0.0);
//            double longitude = intent.getDoubleExtra("Value2",0.0);
            SharedPreferences prefs = getSharedPreferences(PaymentDetails.MY_PREFS_NAME, MODE_PRIVATE);
            lati = prefs.getString("lat", "0");//"No name defined" is the default value.
            longi  = prefs.getString("longitude", "0"); //0 is the default value.

            Toast.makeText(this, lat +" " + longi, Toast.LENGTH_SHORT).show();
            lat = Double.parseDouble(lati);
            longitude = Double.parseDouble(longi);
            // Add a marker in Sydney and move the camera
            LatLng address = new LatLng(lat, longitude);

            mMap.addMarker(new MarkerOptions().position(address).title("Apply"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(address,10f));

        }


    }
    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mlocationpermission){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            MoveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    15f);

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(CurrentLocationMap.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mlocationpermission= true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        request_code);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    request_code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mlocationpermission = false;

        switch(requestCode){
            case request_code:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mlocationpermission = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mlocationpermission = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

}
