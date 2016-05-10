package fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.locateandgetlocated.locategetlocated.R;

import activities.DeviceActivity;
import activities.DevicesActivity;
import database.Device;

/**
 * Created by Krzysztof on 15.03.2016.
 */
public class LocalizedFragment extends Fragment {

    ListView localizedDevicesListView;

    public LocalizedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inputFragmentView = inflater.inflate(R.layout.fragment_localized, container, false);
        // Inflate the layout for this fragment

        localizedDevicesListView = (ListView) inputFragmentView.findViewById(R.id.localizedDevicesListView);

        localizedDevicesListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Device clickedDevice = (Device) adapterView.getItemAtPosition(i);
                        Intent intent = new Intent(getActivity().getApplicationContext(), DeviceActivity.class);
                        intent.putExtra("id", clickedDevice.getId());
                        startActivity(intent);
                    }
                }
        );


        refreshAdapter();
        return inputFragmentView;
    }

    private void refreshAdapter() {
        localizedDevicesListView.setAdapter(new ArrayAdapter<Device>(((DevicesActivity)getActivity()).getApplicationContext(), android.R.layout.simple_list_item_1, ((DevicesActivity)getActivity()).dbHandler.getDevicesArrayByType(1)) {
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
