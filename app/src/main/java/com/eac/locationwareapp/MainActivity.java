package com.eac.locationwareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Context context;
    private FusedLocationProviderClient fusedLocationClient;
    TextView txtLocation,txtAddress;
    private String latitude ,longitude ,address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        checkLocationPermission();

         txtLocation = findViewById(R.id.txtLocation);
        txtAddress = findViewById(R.id.txtAddress);
        Button btnGetLocation = findViewById(R.id.btnGetLocation);
        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationPermission();
            }
        });

        Button btnShowMap = findViewById(R.id.btnShowMap);
        btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open maps activity
                //intent
                //pass the following - lat longitude
                Intent intent = new Intent(context,MapsActivity.class);
                intent.putExtra("lat",latitude);
                intent.putExtra("longt",longitude);
                intent.putExtra("address",address);
                startActivity(intent);
            }
        });
    }

    //capture - user location permission
    //runtime

    public void checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            getMyLocation();
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},1111);
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void getMyLocation(){
        //coordinates latitude and longitude
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location!=null){
                            //location success
                            //latitude longitude - coordinates
                            txtLocation.setText("Latitude "+location.getLatitude()+" \nLongitude "+location.getLongitude());
                            getAddress(location.getLatitude(),location.getLongitude());
                        }else{
                            showToast("No location data");
                        }
                      }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed
                        showToast("Something went wrong. Try again later "+e.getMessage());
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1111){
            if (grantResults.length>0){
                //
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                    //permission granted
                    getMyLocation();
                    showToast("Permission granted");
                }else{
                    showToast("Permission denied.You will not be able access location");
                }
            }else{
                showToast("Something went wrong");
            }
        }

    }

    //get address
    public void getAddress(double lat,double longt){
        //code -
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addressList = geocoder.getFromLocation(lat,longt,1);
            if (addressList.size()>0){
                //go ahead -
                String countryName = addressList.get(0).getCountryName();
                String admin = addressList.get(0).getAdminArea();
                String landmark = addressList.get(0).getFeatureName();
                txtAddress.setText("CountryName: "+countryName+"\nCity "+admin+"\nFeatureName "+landmark);
                address = admin+""+landmark;
                latitude = ""+lat;
                longitude = ""+longt;
            }
        } catch (IOException e) {
            e.printStackTrace();
            showToast("Something went wrong "+e.getMessage());
        }
    }

    public void showToast(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}