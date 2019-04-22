package fi.esupponen.remembemed;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import fi.esupponen.remembemed.arrayadapters.AlarmArrayAdapter;
import fi.esupponen.remembemed.classes.Alarm;
import fi.esupponen.remembemed.classes.Medication;
import fi.esupponen.remembemed.dialogfragments.AddAlarmDialogFragment;
import fi.esupponen.remembemed.dialogfragments.EditDialogFragment;

/**
 * @author Essi Supponen [essi.supponen@tuni.fi]
 * @version 2019-04-23
 * @since 1.8
 */
public class MedicineActivity extends AppCompatActivity implements EditDialogFragment.EditDialogListener, AddAlarmDialogFragment.AddAlarmDialogFragmentListener, AlarmArrayAdapter.AlarmManaging {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to delete this medication?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent("modify-data");
                        intent.putExtra("request", MedicationRequest.DELETE);
                        intent.putExtra("index", index);
                        LocalBroadcastManager.getInstance(MedicineActivity.this).sendBroadcast(intent);
                        MedicineActivity.super.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showAlarms() {
        List<Alarm> alarms = medication.getAlarms();
        Alarm[] alarmArray = new Alarm[alarms.size()];

        for (int i = 0; i < alarms.size(); i++) {
            alarmArray[i] = alarms.get(i);
        }

        ListView list = (ListView) findViewById(R.id.alarmsList);
        AlarmArrayAdapter adapter = new AlarmArrayAdapter(this, this, alarmArray);
        list.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        Bundle b = getIntent().getExtras();
        medication = (Medication) b.getSerializable("medication");
        index = b.getInt("index");

        Log.d("MedicineActivity, onCreate", "index: " + b.getInt("index"));

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

        if (dose == null ||dose.equals("")) {
            Toast.makeText(this, "Dose cannot be empty, please try again", Toast.LENGTH_LONG).show();
        } else {
            Alarm alarm = new Alarm(hours, minutes, (float)repeatAfterHour, true, false, dose);
            medication.getAlarms().add(alarm);

            Intent broadcastIntent = new Intent("modify-data");
            broadcastIntent.putExtra("request", MedicationRequest.ADD_ALARM);
            broadcastIntent.putExtra("index", index);
            broadcastIntent.putExtra("alarm", alarm);
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

            activateAlarm(medication.getAlarms().size()-1);

            showAlarms();
        }
    }

    @Override
    public void activateAlarm(int position) {
        Log.d("MedicineActivity", "activate alarm: " + position);

        Alarm alarm = medication.getAlarms().get(position);
        alarm.setAlarmOn(true);

        AlarmManager manager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("medName", medication.getName());
        intent.putExtra("medDose", alarm.getDose());

        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, alarm.getId(), intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());

        if (alarm.getHourToRepeat() == 0) {
            manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),alarmIntent);
        } else {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000 * 60 * (long)alarm.getHourToRepeat(), alarmIntent);
        }

        Intent broadcastIntent = new Intent("modify-data");
        broadcastIntent.putExtra("request", MedicationRequest.SET_ALARM_ACTIVE);
        broadcastIntent.putExtra("medicationIndex", index);
        broadcastIntent.putExtra("alarmIndex", position);
        broadcastIntent.putExtra("active", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    @Override
    public void cancelAlarm(int position) {
        Log.d("MedicineActivity", "cancel alarm: " + position);

        Alarm alarm = medication.getAlarms().get(position);
        alarm.setAlarmOn(false);
        AlarmManager manager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

        Intent intentRemove = new Intent(this, AlarmReceiver.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intentRemove.putExtra("medName", medication.getName());
        intentRemove.putExtra("medDose", alarm.getDose());
        PendingIntent alarmIntentRemove = PendingIntent.getBroadcast(this, alarm.getId(), intentRemove, 0);
        manager.cancel(alarmIntentRemove);

        Intent broadcastIntent = new Intent("modify-data");
        broadcastIntent.putExtra("request", MedicationRequest.SET_ALARM_ACTIVE);
        broadcastIntent.putExtra("medicationIndex", index);
        broadcastIntent.putExtra("alarmIndex", position);
        broadcastIntent.putExtra("active", false);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    @Override
    public void removeAlarm(int position) {
        Log.d("MedicineActivity", "remove alarm: " + position);

        medication.getAlarms().remove(position);

        Intent broadcastIntent = new Intent("modify-data");
        broadcastIntent.putExtra("request", MedicationRequest.REMOVE_ALARM);
        broadcastIntent.putExtra("medicationIndex", index);
        broadcastIntent.putExtra("alarmIndex", position);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

        showAlarms();
    }

    @Override
    public void setTakenAlarm(int position, boolean taken) {
        medication.getAlarms().get(position).setTaken(taken);

        Intent broadcastIntent = new Intent("modify-data");
        broadcastIntent.putExtra("request", MedicationRequest.SET_TAKEN);
        broadcastIntent.putExtra("medicationIndex", index);
        broadcastIntent.putExtra("alarmIndex", position);
        broadcastIntent.putExtra("taken", taken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
}
