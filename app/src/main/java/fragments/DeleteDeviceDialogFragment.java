package fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import activities.DeviceActivity;
import adapters.AdapterSingleton;

/**
 * Created by Krzysztof on 26.04.2016.
 */
public class DeleteDeviceDialogFragment extends DialogFragment {
    final AdapterSingleton adapterSingleton = AdapterSingleton.getmInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Czy na pewno chcesz usunąć urządzenie " + ((DeviceActivity) getActivity()).deviceName + "?")
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int deviceId = ((DeviceActivity)getActivity()).deviceId;
                        // TODO Usuwanie urządzenia z bazy
                        ((DeviceActivity) getActivity()).dbHandler.deleteDevice(deviceId);
                        adapterSingleton.notifyCustomAdapters();
                        Toast.makeText(getActivity().getApplicationContext(), "Pomyślnie usunięto urządzenie: " + deviceId, Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    }
                })
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Anulowanie usuwania, zamknięcie okna
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
