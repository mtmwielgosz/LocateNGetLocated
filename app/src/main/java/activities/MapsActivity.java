package activities;

import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
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

import lokalization.Place;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private String device_name="SAMSUNG";

    private int current_indeks=22;
    private Button button_Normal;
    private Button button_Hybrid;
    private Button button_Terrain;
    private Button button_DeviceLocation;
    private Button button_nextLocations;
    private Button button_prevLocations;
    private TextView dataTB;
    private TextView counter;
    private CameraPosition cameraPosition;
    private GoogleMap mMap;
    private RangeSliderView timeline;
   // private Map<String, Place> placesTMP = new HashMap<String, Place>();
  //  private TextView data;
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
        button_Normal = (Button) findViewById(R.id.buttonNormalny);
        button_Hybrid = (Button) findViewById(R.id.buttonSatelita);
        button_Terrain = (Button) findViewById(R.id.buttonTeren);
        button_DeviceLocation = (Button) findViewById(R.id.buttonLokalizacjaUrzadzenia);
        setPlacesTMP();
        createTimeline();

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
      /*  if(!pos.isEmpty()){
            latitude = pos.getDouble("szerokosc");
            longitude = pos.getDouble("dlugosc");
            date = pos.getString("data");
            device_name = pos.getString("nazwa");
            hour = pos.getString("godzina");}*/

        /* NEW BUNDLE*/

// odbieranie danych z bundle
        if(!pos.isEmpty()){
           // ObjectInputStream we = new ObjectInputStream(new FileInputStream("pracownik.dat"));
           // locations = (Place[])we.readObject();
          //  device_name = pos.getString("nazwa");
           // current_indeks = pos.getInt("godzina");
        }

        mMap.addMarker(new MarkerOptions().position(locations[current_indeks].getCoordinates()).title(device_name).snippet(locations[current_indeks].getHour() + " - " + locations[current_indeks].getDate()));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
        //dataTB.setText(current_indeks + 1 + "/" + locations.length + "\n" + locations[current_indeks].getHour() + " - " + locations[current_indeks].getDate());
        setCurrentPosition(locations[locations.length -1].getCoordinates(), locations[locations.length -1].getDate(), locations[locations.length -1].getHour());
    }

    public void setOnMixed(View view)
    {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    public void setCurrentPosition(LatLng p, String d, String h){
        dataTB.setText(device_name);
        counter.setText((buttonCurrentFocusIndex + 1) + "/" + locations.length );
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(p).title(device_name).snippet(h+ " " + d));
        button_DeviceLocation.setText(device_name);
        cameraPosition = new CameraPosition.Builder()
                .target(p)      // Sets the center of the map to Mountain View
                .zoom(13)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


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
      locations=new Place[21];
        for(int i =0; i<21; i++){
            locations[i]=new Place(new LatLng(51.10828596112606 + i,17.05601692199707 +i),(i+10)+".05.2016","10:"+(i+10));
       }



       /* placesTMP.put("0",new Place(new LatLng(51.10828596112606,17.05601692199707),"12.04.2016","10:23"));
        placesTMP.put("1",new Place(new LatLng(51.11284832391172,17.06634520785883),"12.04.2016", "13:22"));
        placesTMP.put("2",new Place(new LatLng(51.109399649873446,17.101707451511174),"12.04.2016", "15:03"));
        placesTMP.put("3",new Place(new LatLng(51.12828596112606,17.02601692199707),"12.04.2016","19:57"));
        placesTMP.put("4",new Place(new LatLng(51.10487287463323,17.034759514499456),"13.04.2016","9:23"));
        placesTMP.put("5",new Place(new LatLng(51.07942846371363,16.969528191257268),"13.04.2016","9:45"));
        placesTMP.put("6",new Place(new LatLng(51.08633002645643,16.989097588229924),"13.04.2016","22:16"));
        placesTMP.put("7",new Place(new LatLng(51.10268836779975,17.032299038255587),"14.04.2016","9:44"));
        placesTMP.put("8", new Place(new LatLng(51.10828596112606, 17.05601692199707), "14.04.2016", "13:32"));
      //  placesTMP.put("9", new Place(new LatLng(51.10279615840873, 17.042770382249728), "14.04.2016", "14:10"));*/



    }

    public void setOtherLocations(int currentLocation){
        currentPlaces.clear();

        if(locations.length>0){
            if(currentLocation != locations.length -1 ){
                //liczba lokacji które są na lewo/prawo od aktualnie wybranej
                int currentRight = locations.length -1 - currentLocation;
                int  currentLeft = currentLocation;


                // przechowuje aktualnie przerabiany indeks
               int i=0;
                if(currentLeft>0){
                //kopiowanie do tymczasowej tablicy lokacji z lewej strony
                    for(i= 0; i<currentLeft && i<4; i++){
                        currentPlaces.add(i, locations[currentLeft-4+i]);
                    }
                //wstawienie do tymczasowej tablicy aktualnej lokacji
                i++;}
                    currentPlaces.add(i,locations[currentLeft]);
                i++;
                if(currentRight>0) {
                    //kopiowanie do tymczasowej tablicy lokacji z prawej strony
                    for (int j = i; j < i + currentRight + 1 && j < locations.length - i; j++) {
                        currentPlaces.add(j,locations[i + currentRight - 4 + j]);
                    }
                }

                }

            }


        }

    }


