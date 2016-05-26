package activities;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.locateandgetlocated.locategetlocated.R;

import database.DBHandler;
import database.Request;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import extra.SpinnerActivity;
import localization.Place;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private String deviceNumber;
    private String deviceName;
    private Spinner mapViewSpinner;
    private LocationManager locationManager;
    private Place lastPosition;
    private int current_indeks=22;
    private Geocoder geoCoder = null;
    private TextView dataTB;
    private TextView distanceTB;
    private TextView counter;
    private TextView adressTB;
    private TextView adressMediumTB;
    private CameraPosition cameraPosition;
    static private GoogleMap mMap;

    Place[] locations;
    private List<Place> currentPlaces  = new ArrayList<Place>(); ;
    private HorizontalScrollView timelineSV;
    private LinearLayout timelineLayout;
    private Button[] dateButtons;
    private int selectedIndex;
    private DBHandler dbHandler;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        deviceNumber = getIntent().getStringExtra("name");
        deviceName = getIntent().getStringExtra("deviceName");
        counter = (TextView) findViewById(R.id.counter);
        dataTB = (TextView) findViewById(R.id.dataTB);
        adressTB = (TextView) findViewById(R.id.adressTB);
        adressMediumTB = (TextView) findViewById(R.id.adressMediumTB);
        distanceTB = (TextView) findViewById(R.id.distanceTB);
        mapViewSpinner = (Spinner) findViewById(R.id.mapViewMode);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mapViewsArray, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mapViewSpinner.setAdapter(adapter);
        mapViewSpinner.setOnItemSelectedListener(new SpinnerActivity());
        setPlacesTMP();
        createTimeline();

    }



    public void setAdress(LatLng l){
        List<Address> locations = null;
        String adress = "";
        geoCoder = new Geocoder(this);
        try {
            locations= geoCoder.getFromLocation(l.latitude,l.longitude,4);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(locations.size() > 0) {
            for (int i = 1; i < locations.get(0).getMaxAddressLineIndex() + 1; i++) {

                adress = adress + locations.get(0).getAddressLine(i) + ", ";
            }
            adressTB.setText(adress);
            adressMediumTB.setText(locations.get(0).getAddressLine(0) + "");
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
        setSelectedPosition(locations[locations.length - 1].getCoordinates(), locations[locations.length - 1].getDate(), locations[locations.length - 1].getHour());
    }

    public float calculateDistance(LatLng loc1, LatLng loc2){
        float[] results = new float[1];
        Location.distanceBetween(loc1.latitude, loc1.longitude, loc2.latitude, loc2.longitude, results);
        return results[0];
    }



    public LatLng getCurrentPosition(){
        LatLng l = null;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location lok = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(lok!=null) {
            l = new LatLng(lok.getLatitude(), lok.getLongitude());
           // mMap.addMarker(new MarkerOptions().position(l).title("Tu jesteś!").icon(BitmapDescriptorFactory.fromResource(R.drawable.pin1)));
            mMap.addMarker(new MarkerOptions().position(l).title("Tu jesteś!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
            return l;
    }



    public void setSelectedPosition(LatLng p, String d, String h){
        dataTB.setText(deviceNumber);
        counter.setText((selectedIndex + 1) + "/" + locations.length);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(p).title(deviceNumber).snippet(h + " " + d));

        if(lastPosition==null){
            lastPosition=new Place(new LatLng(p.latitude,p.longitude),d,h);
        }
        else {
            mMap.addMarker(new MarkerOptions().position(lastPosition.getCoordinates()).title(deviceNumber).snippet(lastPosition.getHour() + " " + lastPosition.getDate()));
        }

        double latitudeAVG = (p.latitude + lastPosition.getCoordinates().latitude)/2;
        double longitudeAVG =   (p.longitude + lastPosition.getCoordinates().longitude)/2;

        int distance =  ((int) calculateDistance(p,locations[locations.length-1].getCoordinates()))/1000;


        cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitudeAVG, longitudeAVG))// Sets the center of the map to Mountain View
                .zoom(distance/200)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
       setAdress(p);

        distance = 0;

        if( getCurrentPosition()!=null){
            distance = (int) calculateDistance(p, getCurrentPosition());
        }
       // float distance = calculateDistance(p, new LatLng(51.0, 17.0));
        if(distance<1000) {
            distanceTB.setText( distance + " m");
        }else{
            distanceTB.setText( distance/1000 + " km");
        }

    }




    public void setOnSatelite(View view)
    {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
    public void setOnTerrain(View view)
    {
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }
    public void setOnMixed(View view)
    {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    public void setPlacesTMP(){
        dbHandler = new DBHandler(this, null, null, 1);
        //dbHandler.addRequest(new Request(10,53.1,17.3,null,null,null,"k"));
        int reqestId = getIntent().getIntExtra("id", 1);


        Request[] requests = dbHandler.getRequestsArray();

        List<Place> places = new ArrayList<Place>();

        for(int i = 0; i < requests.length; i++) {
            Log.d("NIE_WCHODZI", requests[i].receiver + "==" + deviceNumber);
            if (requests[i].receiver.equals(deviceNumber) && requests[i].localizationDate!=null) {
                Log.d("WCHODZ", requests[i].receiver + "==" + deviceNumber);

//                locations[i]=new Place(new LatLng(51.10828596112606 + rand.nextDouble(),17.05601692199707 + rand.nextDouble()),(i+10)+".05.2016","10:"+(i+10));
                places.add(new Place(new LatLng(requests[i].latitude, requests[i].longitude),new java.sql.Date(requests[i].localizationDate.getTime())+"", requests[i].localizationDate.getHours()+ ":" +requests[i].localizationDate.getMinutes() ));

                if(reqestId==requests[i].id){
                    selectedIndex = places.size()-1;
                }
            }
        }

        locations = new Place[places.size()];
        for(int i = 0; i < places.size(); i++)
        {
            locations[i] = places.get(i);
        }
    }

    protected void createTimeline(){

        timelineSV = (HorizontalScrollView ) findViewById(R.id.timelineSV);
        dateButtons=new Button[locations.length];
        timelineLayout = (LinearLayout) findViewById(R.id.timelineLayout);

        for(int i=0; i<locations.length; i++){
            final int z =i;
            dateButtons[i] = new Button(this);
            dateButtons[i].setText(locations[i].getHour() + "\n" + locations[i].getDate());
            dateButtons[i].setTextColor(Color.rgb(255, 255, 255));
            dateButtons[i].setBackgroundColor(Color.rgb(128, 128, 128));
            dateButtons[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            timelineLayout.addView(dateButtons[i]);
            dateButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dateButtons[selectedIndex].setBackgroundColor(Color.rgb(128, 128, 128));
                    selectedIndex = z;
                    dateButtons[z].setBackgroundColor(Color.rgb(0, 0, 0));
                    setSelectedPosition(locations[z].getCoordinates(), locations[z].getDate(), locations[z].getHour());
                }

            });

        }
        timelineSV.post(new Runnable() {
            public void run() {
                timelineSV.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });
        dateButtons[selectedIndex].setBackgroundColor(Color.rgb(0, 0, 0));        //timelineSV.scrollTo(0, timelineSV.getPaddingEnd());
    }
    }


