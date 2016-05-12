package activities;

import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.locateandgetlocated.locategetlocated.R;
import extra.RangeSliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import extra.SpinnerActivity;
import localization.Place;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private String device_name="SAMSUNG";
    private Spinner mapViewSpinner;
    private Place lastPosition;
    private int current_indeks=22;
    private Button button_Normal;
    private Button button_Hybrid;
    private Button button_Terrain;
    private Button button_nextLocations;
    private Button button_prevLocations;
    private TextView dataTB;
    private TextView counter;
    private CameraPosition cameraPosition;
    static private GoogleMap mMap;
    private RangeSliderView timeline;
    Place[] locations;
    private List<Place> currentPlaces  = new ArrayList<Place>(); ;
    private HorizontalScrollView timelineSV;
    private LinearLayout timelineLayout;
    private Button[] dateButtons;
    private int buttonCurrentFocusIndex;

    protected void createTimeline(){
        buttonCurrentFocusIndex =locations.length-1;
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
                    dateButtons[buttonCurrentFocusIndex].setBackgroundColor(Color.rgb(128, 128, 128));
                    buttonCurrentFocusIndex =z;
                    dateButtons[z].setBackgroundColor(Color.rgb(0, 0, 0));
                    setCurrentPosition(locations[z].getCoordinates(), locations[z].getDate(), locations[z].getHour());
                }

            });

        }
        timelineSV.post(new Runnable() {
            public void run() {
                timelineSV.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });
        dateButtons[buttonCurrentFocusIndex].setBackgroundColor(Color.rgb(0, 0, 0));        //timelineSV.scrollTo(0, timelineSV.getPaddingEnd());
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        counter = (TextView) findViewById(R.id.counter);
        dataTB = (TextView) findViewById(R.id.dataTB);
        mapViewSpinner = (Spinner) findViewById(R.id.mapViewMode);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mapViewsArray, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mapViewSpinner.setAdapter(adapter);
        mapViewSpinner.setOnItemSelectedListener(new SpinnerActivity());
        setPlacesTMP();
        createTimeline();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

      /*  Bundle pos = getIntent().getExtras();
       if(!pos.isEmpty()){
            latitude = pos.getDouble("szerokosc");
            longitude = pos.getDouble("dlugosc");
            date = pos.getString("data");
            device_name = pos.getString("nazwa");
            hour = pos.getString("godzina");}
         NEW BUNDLE

// odbieranie danych z bundle
        if(!pos.isEmpty()){
           // ObjectInputStream we = new ObjectInputStream(new FileInputStream("pracownik.dat"));
           // locations = (Place[])we.readObject();
          //  device_name = pos.getString("nazwa");
           // current_indeks = pos.getInt("godzina");
        }*/
      //  mMap.addMarker(new MarkerOptions().position(locations[locations.length-1].getCoordinates()).title(device_name).snippet(locations[locations.length-1].getHour() + " - " + locations[locations.length-1].getDate()));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
        //dataTB.setText(current_indeks + 1 + "/" + locations.length + "\n" + locations[current_indeks].getHour() + " - " + locations[current_indeks].getDate());
        setCurrentPosition(locations[locations.length - 1].getCoordinates(), locations[locations.length - 1].getDate(), locations[locations.length - 1].getHour());
    }

    public void setOnMixed(View view)
    {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    public void setCurrentPosition(LatLng p, String d, String h){
        dataTB.setText(device_name);
        counter.setText((buttonCurrentFocusIndex + 1) + "/" + locations.length);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(p).title(device_name).snippet(h + " " + d));

        //Example values of min & max latlng values
        if(lastPosition==null){
            lastPosition=new Place(new LatLng(p.latitude,p.longitude),d,h);
        }
        else {
            mMap.addMarker(new MarkerOptions().position(lastPosition.getCoordinates()).title(device_name).snippet(lastPosition.getHour() + " " + lastPosition.getDate()));
        }

        double latitudeAVG = (p.latitude + lastPosition.getCoordinates().latitude)/2;
        double longitudeAVG =   (p.longitude + lastPosition.getCoordinates().longitude)/2;


        cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitudeAVG, longitudeAVG))// Sets the center of the map to Mountain View
                .zoom(8)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
      //  lastPosition=new Place(new LatLng(p.latitude,p.longitude),d,h);
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

    public void setPlacesTMP(){
        Random rand = new Random();
      locations=new Place[22];
        for(int i =0; i<locations.length; i++){
            locations[i]=new Place(new LatLng(51.10828596112606 + rand.nextDouble(),17.05601692199707 + rand.nextDouble()),(i+10)+".05.2016","10:"+(i+10));
       }
    }



    }


