package activities;


import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.locateandgetlocated.locategetlocated.R;

import org.w3c.dom.Text;

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
    private Geocoder geoCoder=null;
    private TextView dataTB;
    private TextView distanceTB;
    private TextView counter;
    static private TextView adressTB;
    static private TextView adressMediumTB;
    private CameraPosition cameraPosition;
    static private GoogleMap mMap;
    private int[] timePeriodIndex = new int[2];

    Place[] locations;
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

        initializeInterface();
        //pobiera lokacje z bazy danych
        setPlaces();

        //wstawia przykładowe dane z lokacjami
        //setPlacesTMP();
        createTimeline();
    }


    public void initializeInterface(){

        geoCoder = new Geocoder(this);
        deviceNumber = getIntent().getStringExtra("deviceNumber");

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

    }
    public void setAdress(LatLng d){


        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    final LatLng l =d;
        Boolean present=geoCoder.isPresent();
        if (mWifi.isConnected()) {
            if(present!=null && present){
            adressMediumTB.post(new Runnable() {
                @Override
                public void run() {
                    try {

                        String adress = "";
                        List<Address> locations = geoCoder.getFromLocation(l.latitude, l.longitude, 1);
                        if (locations.size() > 0) {
                            for (int i = 1; i < locations.get(0).getMaxAddressLineIndex() + 1; i++) {
                                adress = adress + locations.get(0).getAddressLine(i) + ", ";
                            }
                            setAdressTextbox(adress,locations.get(0).getAddressLine(0) + "");
                        }
                    }
                    catch(IOException e) {
                        e.printStackTrace();}
                }});
         }}else{
            adressTB.setText("Translacja adresu niedostępna");
            adressMediumTB.setText("Wymagane połączenie z siecią WiFi");}
        }


        @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
        if(locations!=null && locations.length>0){
            setSelectedPosition(locations[selectedIndex].getCoordinates(), locations[selectedIndex].getDate(), locations[selectedIndex].getHour());
        setAdress(locations[selectedIndex].getCoordinates());
        }}

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
            mMap.addMarker(new MarkerOptions().position(l).title("Tu jesteś!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
        return l;
    }


    public void setSelectedPosition(LatLng p, String d, String h){
        dataTB.setText(deviceName);
        counter.setText((selectedIndex + 1) + "/" + locations.length);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(p).title(deviceName).snippet(h + " " + d));


//        double latitudeAVG = (p.latitude + lastPosition.getCoordinates().latitude)/2;
      //  double longitudeAVG =   (p.longitude + lastPosition.getCoordinates().longitude)/2;




       /* cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitudeAVG, longitudeAVG))// Sets the center of the map to Mountain View
                .zoom(4)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

        LatLngBounds.Builder b = new LatLngBounds.Builder();
        if(lastPosition==null){
            lastPosition=new Place(new LatLng(locations[locations.length -1].getCoordinates().latitude,locations[locations.length -1].getCoordinates().longitude),locations[locations.length -1].getDate(),locations[locations.length -1].getHour());}


        b.include(lastPosition.getCoordinates());
        b.include(p);
        setLastPosition( p,d, h);
        int distance =   (int) calculateDistance(p,lastPosition.getCoordinates())/1000;
        final LatLng x = p;
        LatLngBounds bounds = b.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 160);
        //mMap.animateCamera(cu);

            mMap.animateCamera(cu, new GoogleMap.CancelableCallback() {
                public void onCancel() {
                }

                public void onFinish() {
                    cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(x.latitude, x.longitude))// Sets the center of the map to Mountain View
                            .zoom(14)                   // Sets the zoom
                            .build();                   // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });



       /* cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(p.latitude, p.longitude))// Sets the center of the map to Mountain View
                .zoom(12)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

        distance = 0;
        LatLng currentPosition = getCurrentPosition();
        if( currentPosition!=null){
            distance = (int) calculateDistance(p, currentPosition);
            if(distance<1000) {
                distanceTB.setText( distance + " m");
            }else{
                distanceTB.setText(distance / 1000 + " km");}
        }else{distanceTB.setText( " ");}

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

        locations = new Place[10];
        locations[0] = new Place(new LatLng(51.197282, 15.397279 ),"12-03-2016","11:01");
        locations[1] = new Place(new LatLng(51.297282, 15.357279 ),"12-03-2016","14:21");
        locations[2] = new Place(new LatLng( 51.147282, 15.327279),"13-03-2016","11:13");
        locations[3] = new Place(new LatLng( 51.157282, 15.497279),"15-03-2016","16:07");
        locations[4] = new Place(new LatLng(51.177282, 15.197279 ),"15-03-2016","21:29");
        locations[5] = new Place(new LatLng(51.497282, 15.394279 ),"15-03-2016","22:04");
        locations[6] = new Place(new LatLng(51.187282, 15.399279 ),"16-03-2016","01:47");
        locations[7] = new Place(new LatLng(51.397282, 15.395279 ),"17-03-2016","07:55");
        locations[8] = new Place(new LatLng(51.187282, 14.997279 ),"17-03-2016","14:45");
        locations[9] = new Place(new LatLng(51.297282, 15.387232 ),"17-03-2016","19:58");
        selectedIndex=9;
    }
    public void setPlaces(){
        dbHandler = new DBHandler(this, null, null, 1);
        //dbHandler.addRequest(new
        // (10,53.1,17.3,null,null,null,"k"));
        int reqestId = getIntent().getIntExtra("id", 1);

        Request[] requests = dbHandler.getRequestsArray();

        List<Place> places = new ArrayList<Place>();

        for(int i = 0; i < requests.length; i++) {
            Log.d("NIE_WCHODZI", requests[i].receiver + "==" + deviceNumber);
            if (requests[i].receiver.equals(deviceNumber) && !(new java.sql.Date(requests[i].localizationDate.getTime())+"").equals("1970-01-01")) {
                Log.d("WCHODZ", requests[i].receiver + "==" + deviceNumber);

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

    protected void setLastPosition(LatLng l,String d, String h){
        if(lastPosition==null){

            lastPosition=new Place(new LatLng(locations[locations.length -1].getCoordinates().latitude,locations[locations.length -1].getCoordinates().longitude),locations[locations.length -1].getDate(),locations[locations.length -1].getHour());}
        else {

            mMap.addMarker(new MarkerOptions().position(lastPosition.getCoordinates()).title(deviceName).snippet(lastPosition.getHour() + " " + lastPosition.getDate()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
            lastPosition = new Place(new LatLng(l.latitude, l.longitude),d,h);
        }
}

    protected void setMarkers() {
        mMap.clear();
        if(!locations[timePeriodIndex[0]].getDate().equals(locations[timePeriodIndex[1]].getDate())) {
            adressMediumTB.setText("od  "  + locations[timePeriodIndex[0]].getDate() + "  do  " + locations[timePeriodIndex[1]].getDate());
        }else{            adressMediumTB.setText( locations[timePeriodIndex[0]].getDate());
        }
            adressTB.setText("Wybrane lokalizacje");

        LatLngBounds.Builder b = new LatLngBounds.Builder();
        //double distance = calculateDistance(locations[timePeriodIndex[0]].getCoordinates(), locations[1].getCoordinates());
        for (int i = timePeriodIndex[0]; i < timePeriodIndex[1] + 1; i++) {
            b.include(locations[i].getCoordinates());
           // mMap.addMarker(new MarkerOptions().position(locations[i].getCoordinates()).title(locations[i].getHour()).snippet(locations[i].getDate()).icon(BitmapDescriptorFactory.fromResource(R.drawable.dot)));
            mMap.addMarker(new MarkerOptions().position(locations[i].getCoordinates()).title(locations[i].getHour()).snippet(locations[i].getDate()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }
        LatLngBounds bounds = b.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 160);
        mMap.animateCamera(cu);
    }

    protected void markTimePeriodOnTimeline(int start, int stop, boolean makeClear){
        if(!makeClear) {
            for (int i = start; i < stop; i++) {
                dateButtons[i].setBackgroundColor(Color.rgb(128, 128, 128));
            }
        }else{for (int i = start; i < stop; i++) {
            dateButtons[i].setBackgroundColor(Color.rgb(255, 204, 102));
        }}
    }
    protected void createTimeline(){

        timelineSV = (HorizontalScrollView ) findViewById(R.id.timelineSV);
        timelineSV.setBackgroundColor(Color.rgb(128, 128, 128));
        dateButtons=new Button[locations.length];
        timelineLayout = (LinearLayout) findViewById(R.id.timelineLayout);
        timelineLayout.setBackgroundColor(Color.rgb(128, 128, 128));

        for(int i=0; i<locations.length; i++){
            final int z =i;
            dateButtons[i] = new Button(this);
            dateButtons[i].setText(locations[i].getHour() + "\n" + locations[i].getDate());
            dateButtons[i].setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
            dateButtons[i].setTextColor(Color.rgb(255, 255, 255));
            dateButtons[i].setBackgroundColor(Color.rgb(128, 128, 128));
            dateButtons[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            timelineLayout.addView(dateButtons[i]);
            dateButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dateButtons[selectedIndex].setBackgroundColor(Color.rgb(128, 128, 128));
                    //wyczysc
                    markTimePeriodOnTimeline(0,dateButtons.length,false);
                    selectedIndex = z;
                    dateButtons[z].setBackgroundColor(Color.rgb(0, 0, 0));
                    setSelectedPosition(locations[z].getCoordinates(), locations[z].getDate(), locations[z].getHour());
                    setAdress(locations[z].getCoordinates());

                }

            });
            dateButtons[i].setOnLongClickListener(new View.OnLongClickListener() {
                                                      @Override
                                                      public boolean onLongClick(View v) {
                                                          //wyczysc wszystko
                                                          markTimePeriodOnTimeline(0,dateButtons.length,false);

                                                          if(timePeriodIndex[0]==-1 && timePeriodIndex[1]==-1){
                                                              timePeriodIndex[0]=z;}
                                                          else if(timePeriodIndex[0]!= -1 && timePeriodIndex[1]== -1) {
                                                              if (timePeriodIndex[0] > z) {
                                                                  timePeriodIndex[1] = timePeriodIndex[0];
                                                                  timePeriodIndex[0] = z;

                                                                  //ustaw
                                                                  markTimePeriodOnTimeline(timePeriodIndex[0],timePeriodIndex[1] + 1,true);

                                                              } else {
                                                                  timePeriodIndex[1] = z;
                                                                  //ustaw
                                                                  markTimePeriodOnTimeline(timePeriodIndex[0],timePeriodIndex[1] + 1,true);

                                                              }
                                                              setMarkers();

                                                          }
                                                          else if (timePeriodIndex[0] != -1 && timePeriodIndex[1] != -1) {
                                                              //mMap.clear();
                                                              //wyczysc
                                                              markTimePeriodOnTimeline(timePeriodIndex[0],timePeriodIndex[1] + 1,false);

                                                              timePeriodIndex[0]=z;
                                                              timePeriodIndex[1]=-1;
                                                              dateButtons[z].setBackgroundColor(Color.rgb(255, 204, 102));
                                                          }
                                                          return true;
                                                      }


                                                  }
            );

        }
        timelineSV.post(new Runnable() {
            public void run() {
                timelineSV.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });
        if(locations.length>0) {
            dateButtons[selectedIndex].setBackgroundColor(Color.rgb(0, 0, 0));        //timelineSV.scrollTo(0, timelineSV.getPaddingEnd());
        }}


    public void setAdressTextbox(String s1, String s2){
        adressTB.setText(s1);
        adressMediumTB.setText(s2);
    }

    }

 /*class GeoCoding extends Thread  {

    List<Address> locations = null;
    String adress = "";
    Geocoder geocoder;
    String text="-";
    String textMedium="-";
    LatLng l;

    public GeoCoding(Geocoder g, LatLng l) {
       geocoder=g;
        this.l=l;
    }
=======
}
>>>>>>> origin/master

    @Override
    public void run() {

          //  try {
                try

                {
                    locations = geocoder.getFromLocation(l.latitude, l.longitude, 1);
                    if (locations.size() > 0) {
                        for (int i = 1; i < locations.get(0).getMaxAddressLineIndex() + 1; i++) {

                            adress = adress + locations.get(0).getAddressLine(i) + ", ";
                        }
                       text=adress;
                       textMedium=locations.get(0).getAddressLine(0) + "";


                    }
                }

                catch(
                        IOException e
                        )

                {
                    e.printStackTrace();
                }

            } //catch (InterruptedException e) {
            //    e.printStackTrace();
         //   }
        }

*/





