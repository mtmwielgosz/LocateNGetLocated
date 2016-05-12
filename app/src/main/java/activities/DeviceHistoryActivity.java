package activities;
import database.DBHandler;
import database.Device;
import database.Request;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.locateandgetlocated.locategetlocated.R;

import java.util.Date;

public class DeviceHistoryActivity extends AppCompatActivity {

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

        setTitle(deviceName);
        dbHandler = new DBHandler(this, null, null, 1);

        final Request[] requests = dbHandler.getRequestsArrayByDevice(deviceName);
        String[] dates = new String[requests.length];
        String[] times = new String[requests.length];
        toShow = new String[requests.length];


        for(int i = 0; i < requests.length; i++)
        {
            dates[i] = requests[i].localizationDate.toString();
            times[i] = requests[i].localizationDate.getHours() + ":" + requests[i].localizationDate.getMinutes();
            toShow[i] = dates[i] + " " + times[i];

        }

        devicesListView = (ListView) findViewById(R.id.listView2);

        devicesListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        intent.putExtra("name", deviceName);
                        intent.putExtra("id", requests[i].id);
                        startActivity(intent);
                    }
                }
        );


        refreshAdapter();

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

    private void refreshAdapter() {
        devicesListView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, toShow) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        });
    }

}
