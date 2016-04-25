package fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.locateandgetlocated.locategetlocated.R;

import activities.DevicesActivity;

/**
 * Created by Krzysztof on 24.04.2016.
 */
public class AddDeviceDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_adddevice, null))
                .setTitle("Dodawanie urządzenia")
                .setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int type = ((DevicesActivity)getActivity()).viewPager.getCurrentItem();
                        if(type == 0) { //dodajemy urządzenie lokalizowane
                            // TODO dodawanie lokalizowanego urządzenia do bazy
                        }
                        else { //dodajemy urządzenie lokalizujące
                            // TODO dodowanie lokalizującego urządzenia do bazy
                        }
                        // TODO odświeżanie widoku
                    }
                })
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Anulowanie dodawania, zamknięcie okna
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
