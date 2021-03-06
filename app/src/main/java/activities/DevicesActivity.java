package activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.app.SearchManager;

import adapters.DevicesAdapter;
import database.DBHandler;
import database.Device;
import fragments.AddDeviceDialogFragment;
import fragments.LocalizedFragment;
import fragments.LocatingFragment;

import com.locateandgetlocated.locategetlocated.R;

import java.util.ArrayList;

import adapters.ViewPagerAdapter;

public class DevicesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    public ViewPager viewPager;
    private ViewPagerAdapter adapter;

    public LocalizedFragment localizedFragment;
    public LocatingFragment locatingFragment;


    public DBHandler dbHandler;

    @Override
    protected void onStart() {
        super.onStart();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //Zmiana wybranej pozycji w menu głównym
        navigationView.getMenu().getItem(2).setChecked(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dbHandler = new DBHandler(this, null, null, 1);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Wyświetlenie fragmentu do dodawania urządzeń
                FragmentManager fragmentManager = getSupportFragmentManager();
                AddDeviceDialogFragment newFragment = new AddDeviceDialogFragment();
                newFragment.show(fragmentManager, "dialog");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.devices, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Szukaj...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterDevices(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterDevices(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            return true;
        }
        else if(id == R.id.copyDevices) {
            int type = viewPager.getCurrentItem();
            Intent contactsIntent = new Intent(getApplicationContext(), ContactsActivity.class);
            contactsIntent.putExtra("type", type);
            startActivity(contactsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_localization) {
            intent = new Intent(this, LocalizationActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_history) {
            intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_devices) {
            //do nothing
         /*} else if (id == R.id.nav_settings) {
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);*/
        } else if (id == R.id.nav_about) {
            intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        localizedFragment = new LocalizedFragment();
        locatingFragment = new LocatingFragment();

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(localizedFragment, "Lokalizowane");
        adapter.addFragment(locatingFragment, "Lokalizujące");
        viewPager.setAdapter(adapter);
    }

    private void filterDevices(String query) {
        //Filtrowanie lokalizowanych
        ArrayList<Device> localizedDevices = localizedFragment.devices;
        ArrayList<Device> showedLocalizedDevices = new ArrayList<Device>();
        for(Device device : localizedDevices) {
            if(device.deviceName.toLowerCase().contains(query.toLowerCase()))
                showedLocalizedDevices.add(device);
        }
        localizedFragment.localizedDevicesListView.setAdapter(new DevicesAdapter(getApplicationContext(), showedLocalizedDevices));

        //Filtrowanie lokalizujących
        ArrayList<Device> locatingDevices = locatingFragment.devices;
        ArrayList<Device> showedLocatingDevices = new ArrayList<Device>();
        for(Device device : locatingDevices) {
            if(device.deviceName.toLowerCase().contains(query.toLowerCase()))
                showedLocatingDevices.add(device);
        }
        locatingFragment.locatingDevicesListView.setAdapter(new DevicesAdapter(getApplicationContext(), showedLocatingDevices));
    }


}
