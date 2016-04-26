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
import activities.DevicesActivity;
import database.Device;

/**
 * Created by Krzysztof on 26.04.2016.
 */
public class DeleteDeviceDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Czy na pewno chcesz usunąć urządzenie " + ((DeviceActivity) getActivity()).deviceName + "?")
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int deviceId = ((DeviceActivity)getActivity()).deviceId;
                        // TODO Usuwanie urządzenia z bazy
                        Toast.makeText(getActivity().getApplicationContext(), "Pomyślnie usunięto urządzenie", Toast.LENGTH_LONG).show();
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
