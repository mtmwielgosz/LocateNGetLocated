package adapters;

import android.widget.BaseAdapter;

import java.util.ArrayList;

import database.DBHandler;
import database.Device;
import database.Request;

/**
 * Created by kamil on 2016-05-11.
 */
public class AdapterSingleton {
    private static AdapterSingleton mInstance = null;
    DBHandler dbHandler;
    BaseAdapter[] adapters = new BaseAdapter[2];
    ArrayList<Device> localizedDevices;
    ArrayList<Device> locatingDevices;
    ArrayList<Request> userRequests;

    protected AdapterSingleton() {
    }

    public static synchronized AdapterSingleton getmInstance() {
        if (mInstance == null) {
            mInstance = new AdapterSingleton();
        }
        return mInstance;
    }

    public void setLocalizedCustomAdapter(DevicesAdapter devicesAdapter) {
        adapters[0] = devicesAdapter;
    }

    public void setLocatingCustomAdapter(DevicesAdapter devicesAdapter) {
        adapters[1] = devicesAdapter;
    }

//    public void setUserRequestAdapter(CustomRequestAdapter requestAdapter){
//        adapters[2] = requestAdapter;
//    }

    public void setLocalizedDevices(ArrayList<Device> localizedDevices) {
        this.localizedDevices = localizedDevices;
    }

    public void setLocatingDevices(ArrayList<Device> locatingDevices) {
        this.locatingDevices = locatingDevices;
    }

    public void setUserRequests(ArrayList<Request> userRequests){
        this.userRequests = userRequests;
    }

    public void setDbHandler(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public void notifyCustomAdapters() {
        localizedDevices.clear();
        localizedDevices.addAll(dbHandler.getDevicesArrayListByType(1));

        locatingDevices.clear();
        locatingDevices.addAll(dbHandler.getDevicesArrayListByType(2));

//        userRequests.clear();
//        userRequests.addAll(dbHandler.getRequestsArrayList());


        for (int i = 0; i < adapters.length; i++) {
            adapters[i].notifyDataSetChanged();
        }
    }
}
