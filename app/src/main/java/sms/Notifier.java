package sms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.locateandgetlocated.locategetlocated.R;

import java.net.ContentHandler;

import activities.HistoryActivity;
import activities.MapsActivity;
import database.Device;
import database.Request;

/**
 * Created by kamil on 2016-05-27.
 */
public class Notifier {
    private Context context;
    private Device device;
    private Request request;

    public Notifier(Context context, Device device, Request request) {
        this.context = context;
        this.device = device;
        this.request = request;
    }

    public void showNotification(String title, String message) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle(title);
        builder.setContentText(message);

        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra("deviceNumber", device.phoneNumber);
        intent.putExtra("deviceName", device.deviceName);
        intent.putExtra("id", request.id);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(HistoryActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());


    }
}
