package fi.esupponen.remembemed;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * @author Essi Supponen [essi.supponen@tuni.fi]
 * @version 2019-04-23
 * @since 1.8
 */
public class AlarmReceiver extends BroadcastReceiver {
    /**
     * Id for notification channel.
     */
    String CHANNEL_ID = "medicationChannel";

    /**
     * Create alarm notification from information from the intent.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Newer versions need channel for all notifications.
            CharSequence name = "Medicine alarms";
            String description = "Alarms for medicines";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            // Older version do not.
            builder = new NotificationCompat.Builder(context);
        }

        // Create intent for opening the app
        Intent openIntent = new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openIntent, 0);

        // Set content of notification
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Time to take medication!");
        builder.setContentText(intent.getExtras().getCharSequence("medName") + ": " + intent.getExtras().getCharSequence("medDose"));
        builder.setContentIntent(pendingIntent);

        // Build notification
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Show notification
        int id = 0;
        manager.notify(id, notification);
    }
}
