package fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.locateandgetlocated.locategetlocated.R;

import java.util.ArrayList;

import database.Device;

/**
 * Created by kamil on 2016-05-10.
 */
public class CustomDeviceAdapter extends BaseAdapter {
    ArrayList<Device> devicesList = new ArrayList<>();
    LayoutInflater layoutInflater;
    Context context;

    public CustomDeviceAdapter(Context context, ArrayList<Device> devicesList){
        this.context=context;
        this.devicesList = devicesList;
        layoutInflater = LayoutInflater.from(this.context);
    }


    @Override
    public int getCount() {
        return devicesList.size();
    }

    @Override
    public Object getItem(int position) {
        return devicesList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        DeviceViewHolder deviceViewHolder;
        if (view ==null){
            view = layoutInflater.inflate(R.layout.custom_device_row, null);
            deviceViewHolder = new DeviceViewHolder();
            view.setTag(deviceViewHolder);
        }else {
            deviceViewHolder = (DeviceViewHolder) view.getTag();
        }

        deviceViewHolder.largeText = detail(view, R.id.largeTextView, devicesList.get(position).getDeviceName());
        deviceViewHolder.smallText = detail(view, R.id.smallTextView, devicesList.get(position).getPhoneNumber());

        return view;
    }

    private TextView detail(View view, int resId, String text){
        TextView textView = (TextView) view.findViewById(resId);
        textView.setText(text);
        return textView;
    }

    private class DeviceViewHolder{
        TextView largeText, smallText;
    }
}
