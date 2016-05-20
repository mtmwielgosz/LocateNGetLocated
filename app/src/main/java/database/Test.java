package database;

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

import com.locateandgetlocated.locategetlocated.R;

import java.util.Date;

import sms.*;

public class Test extends AppCompatActivity {
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // czyszczenie bazy
        //   getApplicationContext().deleteDatabase("lngl.db");
        dbHandler = new DBHandler(this, null, null, 1);

    }
}
