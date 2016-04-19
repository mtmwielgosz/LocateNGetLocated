package com.locateandgetlocated.locategetlocated;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class Test extends AppCompatActivity {


    Button addRequestBtn, removeRequestBtn;
    EditText testRequestIdEditText;
    ListView listView;
    ListAdapter listAdapter;
    String[] tab;

    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //   getApplicationContext().deleteDatabase("lngl.db");

        addRequestBtn = (Button) findViewById(R.id.testAddRequestBtn);
        removeRequestBtn = (Button) findViewById(R.id.testRemoveRequestBtn);
        testRequestIdEditText = (EditText) findViewById(R.id.testRequestIdEditText);
        listView = (ListView) findViewById(R.id.testListView);


        dbHandler = new DBHandler(this, null, null, 1);

        addRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                Request request = new Request(date, "5554");
                dbHandler.addRequest(request);
                refreshAdapter();
            }
        });

        removeRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHandler.deleteRequest(testRequestIdEditText.getText().toString());
                refreshAdapter();
            }
        });


        TextView textView = (TextView) findViewById(R.id.testTextView);


        /**
         Request tmp = dbHandler.getRequestsArrayList().get(dbHandler.getRequestsArrayList().size()-1);

         String tmpString =
         "id " + tmp.getId() + "\n"
         + "lat " + tmp.getLatitude() + "\n"
         + "lon " + tmp.getLongitude() + "\n"
         + "sen " + tmp.getSendDate() + "\n"
         + "rec " + tmp.getReceiveDate() + "\n"
         + "loc " + tmp.getLocalizationDate() + "\n";

         textView.setText(tmpString);

         **/

        refreshAdapter();
    }

    private void refreshAdapter() {
        // lvs.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dbHandler.dataBaseToArray()));
        listView.setAdapter(new ArrayAdapter<Request>(getApplicationContext(), android.R.layout.simple_list_item_1, dbHandler.getRequestsArray()) {
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
