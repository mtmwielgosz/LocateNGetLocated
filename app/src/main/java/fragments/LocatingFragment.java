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
import adapters.AdapterSingleton;
import adapters.DevicesAdapter;
import database.Device;

/**
 * Created by Krzysztof on 15.03.2016.
 */
public class LocatingFragment extends Fragment {

    public ListView locatingDevicesListView;
    DevicesAdapter devicesAdapter;
    final AdapterSingleton adapterSingleton = AdapterSingleton.getmInstance();
    public ArrayList<Device> devices;

    public LocatingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inputFragmentView =  inflater.inflate(R.layout.fragment_locating, container, false);
        // Inflate the layout for this fragment

        locatingDevicesListView = (ListView) inputFragmentView.findViewById(R.id.locatingDevicesListView);

        locatingDevicesListView.setOnItemClickListener(
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

        Context context = getActivity().getApplicationContext();//((DevicesActivity) getActivity()).getApplicationContext();
        devices = ((DevicesActivity) getActivity()).dbHandler.getDevicesArrayListByType(2);
        devicesAdapter = new DevicesAdapter(context, devices);
        adapterSingleton.setLocatingCustomAdapter(devicesAdapter);
        adapterSingleton.setLocatingDevices(devices);
        adapterSingleton.setDbHandler(((DevicesActivity) getActivity()).dbHandler);
        locatingDevicesListView.setAdapter(devicesAdapter);
        return inputFragmentView;
    }
}
