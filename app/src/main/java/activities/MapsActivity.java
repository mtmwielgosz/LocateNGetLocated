package activities;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import lokalization.Place;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
   // private Double latitude=0.;
   // private Double longitude=0.;
 //   private String date="Brak danych";
    private String device_name="Brak danych";
    //private String hour="Brak danych";

    private int current_indeks=22;
    private Button button_Normal;
    private Button button_Hybrid;
    private Button button_Terrain;
    private Button button_DeviceLocation;
    private Button button_nextLocations;
    private Button button_prevLocations;
    private TextView dataTB;
    private CameraPosition cameraPosition;
    private GoogleMap mMap;
    private RangeSliderView timeline;
    private Map<String, Place> placesTMP = new HashMap<String, Place>();
    private TextView data;
    Place[] locations;
    private List<Place> currentPlaces  = new ArrayList<Place>(); ;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dataTB = (TextView) findViewById(R.id.dataTB);
        button_Normal = (Button) findViewById(R.id.buttonNormalny);
        button_prevLocations = (Button) findViewById(R.id.prevLocationsBTN);
        button_nextLocations = (Button) findViewById(R.id.nextlocationsBTN);
        button_Hybrid = (Button) findViewById(R.id.buttonSatelita);
        button_Terrain = (Button) findViewById(R.id.buttonTeren);
        button_DeviceLocation = (Button) findViewById(R.id.buttonLokalizacjaUrzadzenia);
      //  data = (TextView) findViewById(R.id.data);
        setPlacesTMP();
        timeline = (RangeSliderView) findViewById(R.id.timelineSlider);
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
        button_nextLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_indeks < locations.length -1) {
                    current_indeks++;
                    newPosition(locations[current_indeks].getCoordinates(),locations[current_indeks].getHour(),locations[current_indeks].getDate());
                    dataTB.setText(current_indeks + 1 + "/" + locations.length + "\n" + locations[current_indeks].getHour() + " - " + locations[current_indeks].getDate());
                    if( current_indeks >= 13 ){ timeline.setInitialIndex(current_indeks-13);}
                }

            }
        });

        button_prevLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_indeks >= 1) {
                    current_indeks--;
                    newPosition(locations[current_indeks].getCoordinates(),locations[current_indeks].getHour(),locations[current_indeks].getDate());
                    dataTB.setText(current_indeks + 1 + "/" + locations.length + "\n" + locations[current_indeks].getHour() + " - " + locations[current_indeks].getDate());
                    if(current_indeks -13 >=0){timeline.setInitialIndex(current_indeks-13);}
                }

            }
        });




        final RangeSliderView.OnSlideListener listenerTimeline = new RangeSliderView.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                current_indeks=locations.length -10 + index;
                Place tmp = locations[current_indeks];
                newPosition(locations[current_indeks].getCoordinates(), locations[current_indeks].getDate(), locations[current_indeks].getHour());
                dataTB.setText(current_indeks + 1 + "/" + locations.length + "\n" + locations[current_indeks].getHour() + " - " + locations[current_indeks].getDate());
            }
        };
        timeline.setOnSlideListener(listenerTimeline);
        timeline.setInitialIndex(9);



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
            dataTB.setText(current_indeks + 1 + "/" + locations.length + "\n" + locations[current_indeks].getHour() + " - " + locations[current_indeks].getDate());
        //data.setText(locations[current_indeks].getHour() + " - " + locations[current_indeks].getDate());
        newPosition(locations[current_indeks].getCoordinates(), locations[current_indeks].getDate(), locations[current_indeks].getHour());
       // newPosition(placesTMP.get(8 + "").getCoordinates(), placesTMP.get(8 + "").getDate(), placesTMP.get(8 + "").getHour());

       /* LatLng place = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(place).title(device_name).snippet(hour + " " + date));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
        data.setText(locations[current_indeks].getDate());
        newPosition(placesTMP.get(8 + "").getCoordinates(), placesTMP.get(8 + "").getDate(), placesTMP.get(8 + "").getHour());*/
        /*button_DeviceLocation.setText(device_name);
        cameraPosition = new CameraPosition.Builder()
                .target(place)      // Sets the center of the map to Mountain View
                .zoom(13)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
    }

    public void setOnMixed(View view)
    {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    public void newPosition(LatLng p, String d, String h){

      // data.setText(d + " - " + h);
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
      locations=new Place[23];
        for(int i =0; i<23; i++){
            locations[i]=new Place(new LatLng(51.10828596112606 + i,17.05601692199707 +i),i+".04.2016","10:"+i);
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


