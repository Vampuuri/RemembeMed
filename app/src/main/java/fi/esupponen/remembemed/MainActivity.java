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

    public void setExampleAlarm(int hour, int minute) {
        alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        Time now = new Time(System.currentTimeMillis());

        int currentHour = now.getHours();
        int currentMin = now.getMinutes();

        Log.d("MainActivity", "hour: " + currentHour + " min: " + currentMin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        medications = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setExampleAlarm(0,0);
    }
}
