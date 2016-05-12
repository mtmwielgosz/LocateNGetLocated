package extra;

import android.app.Activity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.View;
import android.widget.AdapterView;

import activities.MapsActivity;

/**
 * Created by kamil on 12.05.2016.
 */
public class SpinnerActivity  implements AdapterView.OnItemSelectedListener {
    MapsActivity mp =new MapsActivity();


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(position){
            case 0:
                mp.setOnSatelite(view);
                break;
            case 1:
                mp.setOnTerrain(view);
                break;
            case 2:
                mp.setOnMixed(view);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
