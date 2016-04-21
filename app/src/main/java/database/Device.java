package database;

/**
 * Created by kamil on 2016-04-15.
 */
public class Device {
    private int id;
    String phoneNumber;
    String deviceName;
    int deviceType;

    public Device(int id, String phoneNumber, String deviceName, int deviceType) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
    }

    public Device(String phoneNumber, String deviceName, int deviceType) {
        this.phoneNumber = phoneNumber;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }
}
