package adapters;

import java.util.ArrayList;

import database.DBHandler;
import database.Device;

/**
 * Created by kamil on 2016-05-11.
 */
public class AdapterSingleton {
    private static AdapterSingleton mInstance = null;
    DBHandler dbHandler;
    DevicesAdapter[] devicesAdapters = new DevicesAdapter[2];
    ArrayList<Device> localizedDevices;
    ArrayList<Device> locatingDevices;

    protected AdapterSingleton() {
    }

    public static synchronized AdapterSingleton getmInstance() {
        if (mInstance == null) {
            mInstance = new AdapterSingleton();
        }
        return mInstance;
    }

    public void setLocalizedCustomAdapter(DevicesAdapter devicesAdapter) {
        devicesAdapters[0] = devicesAdapter;
    }

    public void setLocatingCustomAdapter(DevicesAdapter devicesAdapter) {
        devicesAdapters[1] = devicesAdapter;
    }

    public void setLocalizedDevices(ArrayList<Device> localizedDevices) {
        this.localizedDevices = localizedDevices;
    }

    public void setLocatingDevices(ArrayList<Device> locatingDevices) {
        this.locatingDevices = locatingDevices;
    }

    public void setDbHandler(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public void notifyCustomAdapters() {
        localizedDevices.clear();
        localizedDevices.addAll(dbHandler.getDevicesArrayListByType(1));

        locatingDevices.clear();
        locatingDevices.addAll(dbHandler.getDevicesArrayListByType(2));

        for (int i = 0; i < devicesAdapters.length; i++) {
            devicesAdapters[i].notifyDataSetChanged();
        }
    }
}
