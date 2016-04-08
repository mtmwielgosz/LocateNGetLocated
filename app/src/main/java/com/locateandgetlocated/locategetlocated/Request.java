package com.locateandgetlocated.locategetlocated;

import java.util.Date;

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
    private String sender;
    private String receiver;

    public Request(int id, double latitude, double longitude, Date sendDate, Date receiveDate, Date localizationDate, String sender, String receiver) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sendDate = sendDate;
        this.receiveDate = receiveDate;
        this.localizationDate = localizationDate;
        this.sender = sender;
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

    public String getSender() {
        return sender;
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

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}

