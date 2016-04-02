package com.locateandgetlocated.locategetlocated;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by Kamil on 2016-03-04.
 */
public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String message = "";
        Object[] pdus = (Object[]) bundle.get("pdus");
        msgs = new SmsMessage[pdus.length];
        for (int i = 0; i < msgs.length; i++) {
            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            message = msgs[i].getMessageBody() + " ZPI";

            LocationTracker locationTracker = new LocationTracker(context);
            if (locationTracker.canGetLocation()) {
                double latitude = locationTracker.getLatitude();
                double longitude = locationTracker.getLongitude();
                Toast.makeText(context, "lat: " + latitude + "; long: " + longitude, Toast.LENGTH_LONG).show();
            } else {
                locationTracker.showSettingsAlert();
                Toast.makeText(context, "tekst2", Toast.LENGTH_LONG).show();
            }

            // wiadomosc message moze byc obsluzona
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
}