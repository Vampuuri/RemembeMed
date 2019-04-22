package fi.esupponen.remembemed;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

public class MedicineActivity extends AppCompatActivity implements EditDialogFragment.EditDialogListener, AddAlarmDialogFragment.AddAlarmDialogFragmentListener {
    Medication medication;
    int index;

    public void editMedicineName(View v) {
        EditDialogFragment frag = EditDialogFragment.getInstance(medication, MedicationRequest.CHANGE_NAME);
        frag.show(getFragmentManager(), "editNameDialog");
    }

    public void addNewAlarm(View v) {
        AddAlarmDialogFragment frag = new AddAlarmDialogFragment();
        frag.show(getFragmentManager(), "addAlarmDialogFragment");
    }

    public void deleteMedication(View v) {
        // TODO: dialog for making sure, that user really wants to delete medication
        Intent intent = new Intent("modify-data");
        intent.putExtra("request", MedicationRequest.DELETE);
        intent.putExtra("index", index);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        super.finish();
    }

    public void showAlarms() {
        List<Alarm> alarms = medication.getAlarms();
        Alarm[] alarmArray = new Alarm[alarms.size()];

        for (int i = 0; i < alarms.size(); i++) {
            alarmArray[i] = alarms.get(i);
        }

        ListView list = (ListView) findViewById(R.id.alarmsList);
        AlarmArrayAdapter adapter = new AlarmArrayAdapter(this, alarmArray);
        list.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        Bundle b = getIntent().getExtras();
        medication = (Medication) b.getSerializable("medication");
        index = b.getInt("index");

        TextView tvTitle = (TextView) findViewById(R.id.nameView);
        tvTitle.setText(medication.getName());

        if (medication.getAlarms().size() > 0) {
            showAlarms();
        }
    }

    @Override
    public void updateEditedDialog(MedicationRequest request, String updatedInfo) {
        if (request.equals(MedicationRequest.CHANGE_NAME) && !updatedInfo.equals("")) {
            medication.setName(updatedInfo);
            ((TextView)findViewById(R.id.nameView)).setText(updatedInfo);
        }

        Intent intent = new Intent("modify-data");
        intent.putExtra("request", MedicationRequest.UPDATE);
        intent.putExtra("index", index);
        intent.putExtra("medication", medication);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void addAlarm(int hours, int minutes, double repeatAfterHour, String dose) {
        Log.d("MedicineActivity", hours + ":" + minutes + " repeat: " + repeatAfterHour + " Dose: " + dose);

        Alarm alarm = new Alarm(hours, minutes, (float)repeatAfterHour, true, dose);
        medication.getAlarms().add(alarm);

        AlarmManager manager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("medName", medication.getName());
        intent.setAction("" + Math.random());

        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, alarm.getId(), intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());

        if (repeatAfterHour == 0) {
            manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),alarmIntent);
        } else {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000 * 60 * (long)repeatAfterHour, alarmIntent);
        }

        showAlarms();

        Intent broadcastIntent = new Intent("modify-data");
        broadcastIntent.putExtra("request", MedicationRequest.ADD_ALARM);
        broadcastIntent.putExtra("index", index);
        broadcastIntent.putExtra("alarm", alarm);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
}
