package activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.locateandgetlocated.locategetlocated.R;

import java.util.ArrayList;

import database.DBHandler;
import database.Device;
import database.Request;
import database.Test;
import fragments.CustomRequestAdapter;
import sms.SMSSender;

public class LocalizationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ListView devicesListView;
    private CustomRequestAdapter customRequestAdapter;
    public DBHandler dbHandler;
    private ArrayList<Request> requestArrayList;

    @Override
    protected void onStart() { //Zmiana wybranej pozycji w menu głównym
        super.onStart();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localization);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        setTitle(R.string.title_activity_localization);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dbHandler = new DBHandler(this, null, null, 1);
        devicesListView = (ListView) findViewById(R.id.listView3);
        requestArrayList = dbHandler.getRequestsArrayList();
        customRequestAdapter = new CustomRequestAdapter(getApplicationContext(), requestArrayList);

        devicesListView.setAdapter(customRequestAdapter);


        //      toShow = dbHandler.getRequestsWithDevicesArray("'%'");


        devicesListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Request clickedRequest = requestArrayList.get(i);
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        String deviceName = clickedRequest.receiver;
                        intent.putExtra("name", deviceName);
                        intent.putExtra("nr", deviceName);
                        intent.putExtra("id", requestArrayList.get(i).id);
                        startActivity(intent);
                    }
                }
        );


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.localization, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_localization) {
            //do nothing
        } else if (id == R.id.nav_history) {
            intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_devices) {
            intent = new Intent(this, DevicesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
