package database;

import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by kamil on 2016-04-08.
 */
public class Request {
    private int id;
    private double latitude;
    private double longitude;
    private Date sendDate;
    private Date receiveDate;
    private Date localizationDate;
    private String receiver;

    public Request(int id, double latitude, double longitude, Date sendDate, Date receiveDate, Date localizationDate, String receiver) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sendDate = sendDate;
        this.receiveDate = receiveDate;
        this.localizationDate = localizationDate;
        this.receiver = receiver;
    }

    public Request(Date sendDate, String receiver) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.sendDate = sendDate;
        this.receiveDate = receiveDate;
        this.localizationDate = localizationDate;
        this.receiver = receiver;
    }

    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public Date getLocalizationDate() {
        return localizationDate;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public void setLocalizationDate(Date localizationDate) {
        this.localizationDate = localizationDate;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                '}';
    }

    /**
    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", sendDate=" + sendDate +
                ", receiveDate=" + receiveDate +
                ", localizationDate=" + localizationDate +
                ", receiver='" + receiver + '\'' +
                '}';
    }
    **/


}

