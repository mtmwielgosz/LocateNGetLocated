package com.locateandgetlocated.locategetlocated;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.StringTokenizer;

public class Test extends AppCompatActivity {

    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        getApplicationContext().deleteDatabase("lngl.db");
        dbHandler = new DBHandler(this, null, null, 1);

        Date date = new Date();
        Request request = new Request(date, "5554");

        Toast.makeText(getApplicationContext(), String.valueOf(date.getTime()), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), (new Date(date.getTime())).toString(), Toast.LENGTH_LONG).show();

        dbHandler.addRequest(request);
        TextView textView = (TextView) findViewById(R.id.testTextView);

        Request tmp = dbHandler.getRequestsArrayList().get(dbHandler.getRequestsArrayList().size()-1);

        String tmpString =
                 "id " + tmp.getId() + "\n"
                + "lat " + tmp.getLatitude() + "\n"
                + "lon " + tmp.getLongitude() + "\n"
                + "sen " + tmp.getSendDate() + "\n"
                + "rec " + tmp.getReceiveDate() + "\n"
                + "loc " + tmp.getLocalizationDate() + "\n";

        textView.setText(tmpString);


    }
}
