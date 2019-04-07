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
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    String CHANNEL_ID = "medicationChannel";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "Alarm fired");
        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Medicine alarms";
            String description = "Alarms for medicines";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        //Medication med = (Medication) intent.getSerializableExtra("medication");
        //System.out.println(intent.getExtras().get("index"));
        //System.out.println(intent.getExtras().get("medication"));
        //Intent openIntent = new Intent(context, Medication.class);
        //openIntent.putExtra("index", intent.getExtras().getInt("index"));
        //openIntent.putExtra("medication", med);
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openIntent, 0);

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Time to take medication!");
        builder.setContentText("Medication information here when i get the intent to work");
        //builder.setContentText(med.getName() + ": " + med.getDose());
        //builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int id = 0;
        manager.notify(id, notification);
    }
}
