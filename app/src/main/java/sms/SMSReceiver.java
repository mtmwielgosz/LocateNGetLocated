package sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Date;

import database.DBHandler;
import database.Request;
import localization.LocationTracker;

/**
 * Created by Kamil on 2016-03-04.
 */
public class SMSReceiver extends BroadcastReceiver {
    DBHandler dbHandler;
    String HOST_PREFIX = "#h#";
    String CLIENT_PREFIX = "#c#";
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String message;
        Object[] pdus = (Object[]) bundle.get("pdus");
        msgs = new SmsMessage[pdus.length];
        for (int i = 0; i < msgs.length; i++) {
            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            message = msgs[i].getMessageBody();

            if (message.startsWith(HOST_PREFIX)) {
                String phoneNumber = msgs[i].getOriginatingAddress();
                sendLocalization(message, phoneNumber);
            }

            if (message.startsWith(CLIENT_PREFIX)) {
                handleLocalization(message);
            }
        }
    }

    private void handleLocalization(String message) {
        Toast.makeText(context, "client prefix", Toast.LENGTH_LONG).show();

        Request responseRequest = new Request();

        responseRequest.sendDate = new Date(Long.parseLong(message.split("#")[2]));
        responseRequest.localizationDate = new Date(Long.parseLong(message.split("#")[3]));
        responseRequest.latitude = Double.parseDouble(message.split("#")[4]);
        responseRequest.longitude = Double.parseDouble(message.split("#")[5]);
        responseRequest.receiveDate = new Date();

        dbHandler = new DBHandler(context, null, null, 1);
        dbHandler.updateRequest(responseRequest);


        Toast.makeText(context, "handle localization", Toast.LENGTH_LONG).show();
    }

    private void sendLocalization(String message, String phoneNumber) {
        LocationTracker locationTracker = new LocationTracker(context);
        if (locationTracker.canGetLocation()) {
            double latitude = locationTracker.getLatitude();
            double longitude = locationTracker.getLongitude();

            String response =  createResponse(message, latitude, longitude);

            SMSSender smsSender = new SMSSender();
            smsSender.sendSMS(phoneNumber, response);
        } else {
            locationTracker.showSettingsAlert();
        }
        Toast.makeText(context, "sendLocalization ", Toast.LENGTH_LONG).show();
    }

    private String createResponse(String message, double latitude, double longitude) {
Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        Request responseRequest = new Request();

        responseRequest.sendDate = new Date(Long.parseLong(message.split("#")[2]));
        responseRequest.localizationDate = new Date();
        responseRequest.latitude = latitude;
        responseRequest.longitude = longitude;

        return CLIENT_PREFIX + responseRequest.sendDate.getTime() + "#" + responseRequest.localizationDate.getTime() + "#" + latitude + "#" + longitude;
    }
}