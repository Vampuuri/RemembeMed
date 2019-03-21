package fi.esupponen.remembemed;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Medication> medications;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    /**
    public int millisToAlarm(int currentH, int alarmH, int currentM, int alarmM) {
        int currentInMinutes = currentH * 60 + currentM;
        int alarmInMinutes = alarmH * 60 + alarmM;

        if (currentInMinutes < alarmInMinutes) {
            return (alarmInMinutes - currentInMinutes) * 60000;
        } else if (currentInMinutes > alarmInMinutes) {
            return 24*60*60000 - (currentInMinutes - alarmInMinutes);
        } else {
            return 0;
        }
    }

    public void setExampleAlarm(int hour, int minute) {
        alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        Time now = new Time(System.currentTimeMillis());

        int currentHour = now.getHours();
        int currentMin = now.getMinutes();

        Log.d("MainActivity", "hour: " + currentHour + " min: " + currentMin);
        Log.d("MainActivity", "millisToAlarm: " + millisToAlarm(currentHour, hour, currentMin, minute));

        alarmManager.set(AlarmManager.RTC_WAKEUP, millisToAlarm(currentHour, hour, currentMin, minute), alarmIntent);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        medications = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setExampleAlarm(20,55);
    }
}
