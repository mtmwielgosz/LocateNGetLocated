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

import com.locateandgetlocated.locategetlocated.R;

import activities.DeviceActivity;
import adapters.AdapterSingleton;
import database.DBHandler;
import database.Device;

/**
 * Created by Krzysztof on 26.04.2016.
 */
public class EditDeviceDialogFragment extends DialogFragment {
    DBHandler dbHandler;
    final AdapterSingleton adapterSingleton = AdapterSingleton.getmInstance();
    EditText editTextDeviceName;
    EditText editTextPhoneNumber;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View alertFragmentView = inflater.inflate(R.layout.dialog_add_edit_device, null);
        dbHandler = ((DeviceActivity)getActivity()).dbHandler;
        editTextDeviceName = (EditText)alertFragmentView.findViewById(R.id.editTextDeviceName);
        editTextPhoneNumber = (EditText)alertFragmentView.findViewById(R.id.editTextPhoneNumber);
        editTextDeviceName.setText(((DeviceActivity)getActivity()).deviceName);
        editTextPhoneNumber.setText(((DeviceActivity)getActivity()).phoneNumber);

        builder.setView(alertFragmentView)
                .setTitle("Edycja danych urządzenia")
                .setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int deviceId = ((DeviceActivity)getActivity()).deviceId;
                        String deviceName = editTextDeviceName.getText().toString();
                        String phoneNumber = editTextPhoneNumber.getText().toString();
                        int deviceType = ((DeviceActivity)getActivity()).deviceType;
                        Device device = new Device(deviceId, phoneNumber, deviceName, deviceType);
                        //Edycja danych urządzenia w bazie
                        dbHandler.updateDevice(device);
                        adapterSingleton.notifyCustomAdapters();
                        //odświeżenie widoku
                        ((DeviceActivity)getActivity()).textViewDeviceName.setText(deviceName);
                        ((DeviceActivity)getActivity()).textViewPhoneNumber.setText(phoneNumber);
                        Toast.makeText(getActivity().getApplicationContext(), "Edytowano dane urządzenia", Toast.LENGTH_LONG).show();
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
