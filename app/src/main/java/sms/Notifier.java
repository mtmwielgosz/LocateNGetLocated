package sms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.net.ContentHandler;

/**
 * Created by kamil on 2016-05-27.
 */
public class Notifier {
    private Context context;

    public Notifier(Context context) {
        this.context = context;
    }

    public void makeNotification(String title, String message) {
        Toast.makeText(context, "1", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Toast.makeText(context, "2", Toast.LENGTH_LONG).show();

        Notification notification = new Notification.Builder(context)
                .setTicker("cos")
                .setContentTitle(title)
                .setContentText(message)
//                .setSmallIcon(android.support.design.R.drawable.ic_)
                .setContentIntent(pendingIntent).getNotification();
        Toast.makeText(context, "3", Toast.LENGTH_LONG).show();

        notification.flags = Notification.FLAG_AUTO_CANCEL;
        Toast.makeText(context, "4", Toast.LENGTH_LONG).show();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Toast.makeText(context, "5", Toast.LENGTH_LONG).show();


        notificationManager.notify(0, notification);
        Toast.makeText(context, "6", Toast.LENGTH_LONG).show();

    }
}
