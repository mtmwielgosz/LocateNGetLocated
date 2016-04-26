package fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import database.*;


import com.locateandgetlocated.locategetlocated.R;

import activities.DevicesActivity;

/**
 * Created by Krzysztof on 24.04.2016.
 */
public class AddDeviceDialogFragment extends DialogFragment {
    EditText editTextDeviceName, editTextPhoneNumber;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View alertFragmentView = inflater.inflate(R.layout.dialog_add_edit_device, null);

        builder.setView(alertFragmentView)
                .setTitle("Dodawanie urządzenia")
                .setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int type = ((DevicesActivity) getActivity()).viewPager.getCurrentItem();

                        EditText editTextDeviceName = (EditText)alertFragmentView.findViewById(R.id.editTextDeviceName);
                        EditText editTextPhoneNumber = (EditText)alertFragmentView.findViewById(R.id.editTextPhoneNumber);

                        if (type == 0) { //dodajemy urządzenie lokalizowane
                            Device device = new Device(editTextPhoneNumber.getText().toString(), editTextDeviceName.getText().toString(), 1);
                            ((DevicesActivity)getActivity()).dbHandler.addDevice(device);
                            Toast.makeText(getActivity().getApplicationContext(), "dodane urzadzenie lokalizowane", Toast.LENGTH_LONG).show();
                        } else { //dodajemy urządzenie lokalizujące

                            Device device = new Device(editTextPhoneNumber.getText().toString(), editTextDeviceName.getText().toString(), 2);
                            ((DevicesActivity)getActivity()).dbHandler.addDevice(device);
                            Toast.makeText(getActivity().getApplicationContext(), "dodane urzadzenie lokalizujace", Toast.LENGTH_LONG).show();
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
