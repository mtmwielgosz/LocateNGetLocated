package database;

/**
 * Created by kamil on 2016-04-15.
 */
public class Device {
    public int id;
    public String phoneNumber;
    public String deviceName;
    public int deviceType;

    /**
     * 1 - lokalizowany
     * 2 - lokalizujacy
     **/

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

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", deviceName='" + deviceName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", deviceType=" + deviceType +
                '}';
    }
}
