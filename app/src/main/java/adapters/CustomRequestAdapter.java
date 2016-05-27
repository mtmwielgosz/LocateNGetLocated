package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.locateandgetlocated.locategetlocated.R;

import java.util.ArrayList;

import database.DBHandler;
import database.Device;
import database.Request;

/**
 * Created by kamil on 2016-05-10.
 */
public class CustomRequestAdapter extends BaseAdapter {
    ArrayList<Request> requestsList = new ArrayList<>();
    LayoutInflater layoutInflater;
    Context context;
    DBHandler dbHandler;

    public CustomRequestAdapter(Context context, ArrayList<Request> requestsList) {
        this.context = context;
        this.requestsList = requestsList;
        layoutInflater = LayoutInflater.from(this.context);
    }


    @Override
    public int getCount() {
        return requestsList.size();
    }

    @Override
    public Object getItem(int position) {
        return requestsList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        RequestViewHolder requestViewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.custom_request_row, null);
            requestViewHolder = new RequestViewHolder();
            view.setTag(requestViewHolder);
        } else {
            requestViewHolder = (RequestViewHolder) view.getTag();
        }

        Request request = requestsList.get(position);


        requestViewHolder.latitude = detail(view, R.id.latitudeTextView, request.latitude);
        requestViewHolder.longitude = detail(view, R.id.longitudeTextView, request.longitude);
        requestViewHolder.deviceName = detail(view, R.id.deviceName, request.receiver);
        return view;
    }


    private TextView detail(View view, int resId, String phoneNumber){
        dbHandler = new DBHandler(context, null, null, 1);
        Device device = dbHandler.getDeviceByDeviceNumber(phoneNumber);
        TextView textView = (TextView) view.findViewById(resId);
        textView.setText(device.deviceName);
        return textView;
    }

    private TextView detail(View view, int resId, double val) {
        TextView textView = (TextView) view.findViewById(resId);
        textView.setText(Double.toString(val));
        return textView;
    }

    private class RequestViewHolder {
        TextView deviceName, latitude, longitude;
    }
}
