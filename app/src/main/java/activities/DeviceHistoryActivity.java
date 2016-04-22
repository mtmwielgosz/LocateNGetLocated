package activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.locateandgetlocated.locategetlocated.R;

public class DeviceHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Samsung");



        final Bundle data = new Bundle();

        data.putString("nazwa", "Samsung");
        data.putDoubleArray("szerokosc", new double[]{51.101741, 53.101741, 52.101741, 54.101741});
        data.putDoubleArray("dlugosc", new double[] {17.052048, 17.042048, 17.052048, 16.052048});
        data.putStringArray("data", new String[]{"12-01-2016", "05-03-2016", "06-03-2016", "12-04-2016"});
        data.putStringArray("godzina", new String[]{"21:25", "22:13", "13:12", "04:11"});

        TextView temp1 = (TextView) findViewById(R.id.textView);
        temp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                data.putInt("indeks", 0);
                intent.putExtras(data);
                startActivity(intent);
            }
        });

        TextView temp2 = (TextView) findViewById(R.id.textView1);
        temp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                data.putInt("indeks", 1);
                intent.putExtras(data);
                startActivity(intent);
            }
        });

        TextView temp3 = (TextView) findViewById(R.id.textView2);
        temp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                data.putInt("indeks", 2);
                intent.putExtras(data);
                startActivity(intent);
            }
        });

        TextView temp4 = (TextView) findViewById(R.id.textView3);
        temp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                data.putInt("indeks", 3);
                intent.putExtras(data);
                startActivity(intent);
            }
        });

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
