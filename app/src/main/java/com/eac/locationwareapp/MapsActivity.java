package com.eac.locationwareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private double latitude = 0.316667;
    private double longitude = 32.583611;
    private String addressname="None";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        this.setTitle("Maps Activity");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //
        String lat = getIntent().getStringExtra("lat");
        String lngt = getIntent().getStringExtra("longt");
        addressname = getIntent().getStringExtra("address");

        //convert type casting
        latitude = Double.parseDouble(lat);
        longitude = Double.parseDouble(lngt);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //add logic
        //show the actual - lat longitude
        //create an object LatLng
        LatLng mycoordinates = new LatLng(latitude,longitude);
        //add marker
       // map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.addMarker(new MarkerOptions()
        .position(mycoordinates)
        .title(addressname));
        map.moveCamera(CameraUpdateFactory.newLatLng(mycoordinates));

       // map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(mycoordinates )      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



    }
}