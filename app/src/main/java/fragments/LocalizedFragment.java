package fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.locateandgetlocated.locategetlocated.R;

import java.util.ArrayList;

import activities.DeviceActivity;
import activities.DevicesActivity;
import adapters.DevicesAdapter;
import database.Device;

/**
 * Created by Krzysztof on 15.03.2016.
 */
public class LocalizedFragment extends Fragment {

    public ListView localizedDevicesListView;
    public DevicesAdapter devicesAdapter;
    final AdapterSingleton adapterSingleton = AdapterSingleton.getmInstance();
    public ArrayList<Device> devices;

    public LocalizedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inputFragmentView = inflater.inflate(R.layout.fragment_localized, container, false);
        // Inflate the layout for this fragment

        localizedDevicesListView = (ListView) inputFragmentView.findViewById(R.id.localizedDevicesListView);

        localizedDevicesListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Device clickedDevice = (Device) adapterView.getItemAtPosition(i);
                        Intent intent = new Intent(getActivity().getApplicationContext(), DeviceActivity.class);
                        intent.putExtra("id", clickedDevice.id);
                        startActivity(intent);
                    }
                }
        );
        Context context = getActivity().getApplicationContext();
        devices = ((DevicesActivity) getActivity()).dbHandler.getDevicesArrayListByType(1);
        devicesAdapter = new DevicesAdapter(context, devices);
        adapterSingleton.setLocalizedCustomAdapter(devicesAdapter);
        adapterSingleton.setLocalizedDevices(devices);
        adapterSingleton.setDbHandler(((DevicesActivity) getActivity()).dbHandler);
        localizedDevicesListView.setAdapter(devicesAdapter);
        return inputFragmentView;
    }
}
