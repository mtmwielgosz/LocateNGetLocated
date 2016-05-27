package activities;

import database.DBHandler;
import database.Request;
import adapters.CustomRequestAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.locateandgetlocated.locategetlocated.R;

import java.util.ArrayList;

public class DeviceHistoryActivity extends AppCompatActivity {
    private CustomRequestAdapter customRequestAdapter;
    public ListView devicesListView;
    public DBHandler dbHandler;
    public String[] toShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final String deviceName = getIntent().getStringExtra("name");
        final String deviceNumber = getIntent().getStringExtra("nr");

        setTitle(deviceName);
        dbHandler = new DBHandler(this, null, null, 1);


        final ArrayList<Request> requestArrayList = dbHandler.getRequestsArrayList();

        devicesListView = (ListView) findViewById(R.id.listView2);
        customRequestAdapter = new CustomRequestAdapter(getApplicationContext(), requestArrayList);
        devicesListView.setAdapter(customRequestAdapter);

        devicesListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        intent.putExtra("name", deviceNumber);
                        intent.putExtra("deviceName", deviceName);
                        intent.putExtra("id", requestArrayList.get(i).id);
                        startActivity(intent);
                    }
                }
        );


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
}
