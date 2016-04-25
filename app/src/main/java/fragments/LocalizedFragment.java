package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.locateandgetlocated.locategetlocated.R;

import activities.DeviceActivity;

/**
 * Created by Krzysztof on 15.03.2016.
 */
public class LocalizedFragment extends Fragment {

    public LocalizedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inputFragmentView =  inflater.inflate(R.layout.fragment_localized, container, false);
        // Inflate the layout for this fragment
        TextView temp = (TextView) inputFragmentView.findViewById(R.id.textView);
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DeviceActivity.class);
                startActivity(intent);

            }
        });
        return inputFragmentView;
    }

}
