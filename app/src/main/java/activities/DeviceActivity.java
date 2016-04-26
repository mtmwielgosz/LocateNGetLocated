package activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.locateandgetlocated.locategetlocated.R;

import database.DBHandler;
import database.Device;
import fragments.AddDeviceDialogFragment;
import fragments.DeleteDeviceDialogFragment;
import fragments.EditDeviceDialogFragment;

public class DeviceActivity extends AppCompatActivity {
    public int deviceId;
    public String deviceName;
    public String phoneNumber;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO pobranie deviceID z extras i pozostałych danych z bazy
        dbHandler = new DBHandler(this, null, null, 1);

        int deviceId;
        if (getIntent().hasExtra("id")) {
            deviceId = getIntent().getIntExtra("id", -11);
        } else {
            throw new IllegalArgumentException("brak urzadzenia");
        }

        Device device = dbHandler.getDeviceById(deviceId);

        Toast.makeText(getApplicationContext(), " "+deviceId, Toast.LENGTH_LONG).show();
        //Tymczasowy kod
        deviceName = device.getDeviceName();
        phoneNumber = device.getPhoneNumber();
        //
        setContentView(R.layout.activity_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Dane urządzenia");
        TextView textViewDeviceName = (TextView) findViewById(R.id.textViewDeviceName);
        TextView textViewPhoneNumber = (TextView) findViewById(R.id.textViewPhoneNumber);
        textViewDeviceName.setText(deviceName);
        textViewPhoneNumber.setText(phoneNumber);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.device, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            //Kliknięty przycisk do usuwania urządzenia
            FragmentManager fragmentManager = getSupportFragmentManager();
            DeleteDeviceDialogFragment newFragment = new DeleteDeviceDialogFragment();
            newFragment.show(fragmentManager, "dialog");
        }
        else if (item.getItemId() == R.id.edit) {
            //Klikniety przycisk do edycji danych urządzenia
            FragmentManager fragmentManager = getSupportFragmentManager();
            EditDeviceDialogFragment newFragment = new EditDeviceDialogFragment();
            newFragment.show(fragmentManager, "dialog");
        }
        return super.onOptionsItemSelected(item);
    }


}
