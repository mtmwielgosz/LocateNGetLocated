package com.locateandgetlocated.locategetlocated;

import android.telephony.SmsManager;

/**
 * Created by Kamil on 2016-03-04.
 */
public class SMSSender {
    SmsManager smsManager = null;

    public SMSSender() {
    }

    public void sendRequest(String receiver, String message) {
        smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(receiver, null, message, null, null);
    }
}
