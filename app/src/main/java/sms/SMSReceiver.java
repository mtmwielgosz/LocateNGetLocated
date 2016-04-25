package sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import lokalization.LocationTracker;

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
            message = msgs[i].getMessageBody();


            String HOST_PREFIX = "#h#";
            String CLIENT_PREFIX = "#c#";

            if (message.startsWith(HOST_PREFIX)) {
                Toast.makeText(context, "zapytanie od numeru: " + msgs[i].getOriginatingAddress(), Toast.LENGTH_LONG).show();
                LocationTracker locationTracker = new LocationTracker(context);
                if (locationTracker.canGetLocation()) {
                    double latitude = locationTracker.getLatitude();
                    double longitude = locationTracker.getLongitude();

                    SMSSender smsSender = new SMSSender();
                    smsSender.sendSMS(msgs[i].getOriginatingAddress(), CLIENT_PREFIX + latitude + "#" + longitude);
                } else {
                    locationTracker.showSettingsAlert();
                }
            }


            if (message.startsWith(CLIENT_PREFIX)) {
                Toast.makeText(context, "client prefix", Toast.LENGTH_LONG ).show();
                String latitude = message.split("#")[2];
                String longitude = message.split("#")[3];
                Toast.makeText(context, "wyswietlanie na mapie, dodanie do bazy, otrzymane wspolrzedne: " + latitude + "; " + longitude, Toast.LENGTH_LONG).show();
            }
        }
    }
}