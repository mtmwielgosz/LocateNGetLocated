package com.locateandgetlocated.locategetlocated;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private Double latitude=0.;
    private Double longitude=0.;
    private String date="Brak danych";
    private String device_name="Brak danych";
    private String hour="Brak danych";
    private Button button_Normal;
    private Button button_Hybrid;
    private Button button_Terrain;
    private Button button_DeviceLocation;
    private CameraPosition cameraPosition;
    private GoogleMap mMap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        button_Normal = (Button) findViewById(R.id.buttonNormalny);
        button_Hybrid = (Button) findViewById(R.id.buttonSatelita);
        button_Terrain = (Button) findViewById(R.id.buttonTeren);
        button_DeviceLocation = (Button) findViewById(R.id.buttonLokalizacjaUrzadzenia);

        button_DeviceLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnLocation(v);
            }
        });
        button_Normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnSatelite(v);
            }
        });
        button_Terrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnTerrain(v);
            }
        });
        button_Hybrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnMixed(v);
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Bundle pos = getIntent().getExtras();
        if(!pos.isEmpty()){
            latitude = pos.getDouble("szerokosc");
            longitude = pos.getDouble("dlugosc");
            date = pos.getString("data");
            device_name = pos.getString("nazwa");
            hour = pos.getString("godzina");}

        LatLng place = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(place).title(device_name).snippet(hour + " " + date));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);

        cameraPosition = new CameraPosition.Builder()
                .target(place)      // Sets the center of the map to Mountain View
                .zoom(13)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void setOnMixed(View view)
    {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


    }

    public void setOnSatelite(View view)
    {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


    }

    public void setOnTerrain(View view)
    {
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

    }

    public void setOnLocation(View view)
    {
        mMap.setMyLocationEnabled(true);
        Location myLocation = mMap.getMyLocation();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }
}
