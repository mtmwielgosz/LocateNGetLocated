package sms;
import database.*;
import android.telephony.SmsManager;

/**
 * Created by Kamil on 2016-03-04.
 */
public class SMSSender {
    SmsManager smsManager = null;

    public SMSSender() {
    }

    public void sendSMS(String receiver, String message) {
        smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(receiver, null, message, null, null);
    }

    public void sendRequest(Request request, String message){
        smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage(request.receiver, null, message, null, null);
    }

}
