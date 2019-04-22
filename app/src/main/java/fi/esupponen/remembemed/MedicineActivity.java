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

    /**
     * Medication in question.
     */
    Medication medication;

    /**
     * Index of medication in MainActivity's list.
     */
    int index;

    /**
     * Opens EditDialog.
     *
     * @param v     Edit button
     */
    public void editMedicineName(View v) {
        EditDialogFragment frag = EditDialogFragment.getInstance(medication, MedicationRequest.CHANGE_NAME);
        frag.show(getFragmentManager(), "editNameDialog");
    }

    /**
     * Opens AddAlarmDialog.
     *
     * @param v     New alarm button
     */
    public void addNewAlarm(View v) {
        AddAlarmDialogFragment frag = new AddAlarmDialogFragment();
        frag.show(getFragmentManager(), "addAlarmDialogFragment");
    }

    /**
     * Opens dialog to confirm user's choice. Deletes medication and opens MainActivity.
     *
     * @param v     Delete medication button
     */
    public void deleteMedication(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to delete this medication?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Make local broadcast to update information.
                        Intent intent = new Intent("modify-data");
                        intent.putExtra("request", MedicationRequest.DELETE);
                        intent.putExtra("index", index);
                        LocalBroadcastManager.getInstance(MedicineActivity.this).sendBroadcast(intent);

                        // Close this activity.
                        MedicineActivity.super.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });

        // Build and show dialog.
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Updates alarms to ListView of alarms.
     */
    public void showAlarms() {
        // Convert list into array.
        List<Alarm> alarms = medication.getAlarms();
        Alarm[] alarmArray = new Alarm[alarms.size()];

        for (int i = 0; i < alarms.size(); i++) {
            alarmArray[i] = alarms.get(i);
        }

        // Use AlarmArrayAdapter to show items in ListView
        ListView list = (ListView) findViewById(R.id.alarmsList);
        AlarmArrayAdapter adapter = new AlarmArrayAdapter(this, this, alarmArray);
        list.setAdapter(adapter);
    }

    /**
     * Creates MedicineActivity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        // Get needed information
        Bundle b = getIntent().getExtras();
        medication = (Medication) b.getSerializable("medication");
        index = b.getInt("index");

        // Set info to layout
        TextView tvTitle = (TextView) findViewById(R.id.nameView);
        tvTitle.setText(medication.getName());

        // if there is alarms already, show them
        if (medication.getAlarms().size() > 0) {
            showAlarms();
        }
    }

    /**
     * Sends updated information to MainActivity.
     *
     * @param request
     * @param updatedInfo
     */
    @Override
    public void updateEditedDialog(MedicationRequest request, String updatedInfo) {
        // Update layout of current activity
        if (request.equals(MedicationRequest.CHANGE_NAME) && !updatedInfo.equals("")) {
            medication.setName(updatedInfo);
            ((TextView)findViewById(R.id.nameView)).setText(updatedInfo);
        }

        // Create intent and make local broadcast
        Intent intent = new Intent("modify-data");
        intent.putExtra("request", MedicationRequest.UPDATE);
        intent.putExtra("index", index);
        intent.putExtra("medication", medication);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * Creates alarm object, forwards it to Mainactivity and activates alarm.
     *
     * @param hours
     * @param minutes
     * @param repeatAfterHour
     * @param dose
     */
    @Override
    public void addAlarm(int hours, int minutes, double repeatAfterHour, String dose) {
        if (dose == null ||dose.equals("")) {
            // If dose was not given, show toast:
            Toast.makeText(this, "Dose cannot be empty, please try again", Toast.LENGTH_LONG).show();
        } else {
            // Create alarm
            Alarm alarm = new Alarm(hours, minutes, (float)repeatAfterHour, true, false, dose);
            medication.getAlarms().add(alarm);

            // Send info to MainActivity
            Intent broadcastIntent = new Intent("modify-data");
            broadcastIntent.putExtra("request", MedicationRequest.ADD_ALARM);
            broadcastIntent.putExtra("index", index);
            broadcastIntent.putExtra("alarm", alarm);
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

            // Activate the new alarm by default
            activateAlarm(medication.getAlarms().size()-1);

            // update alarms
            showAlarms();
        }
    }

    /**
     * Activates alarm in given position.
     *
     * @param position of alarm in alarms
     */
    @Override
    public void activateAlarm(int position) {
        // get alarm
        Alarm alarm = medication.getAlarms().get(position);
        alarm.setAlarmOn(true);

        // Get AlarmManager
        AlarmManager manager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

        // create alarm intent
        Intent intent = new Intent(this, AlarmReceiver.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("medName", medication.getName());
        intent.putExtra("medDose", alarm.getDose());

        // Create pending intent
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, alarm.getId(), intent, 0);

        // Get time to alarm
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());

        // Set alarm with manager
        if (alarm.getHourToRepeat() == 0) {
            manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),alarmIntent);
        } else {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000 * 60 * (long)alarm.getHourToRepeat(), alarmIntent);
        }

        // Send info to MainActivity
        Intent broadcastIntent = new Intent("modify-data");
        broadcastIntent.putExtra("request", MedicationRequest.SET_ALARM_ACTIVE);
        broadcastIntent.putExtra("medicationIndex", index);
        broadcastIntent.putExtra("alarmIndex", position);
        broadcastIntent.putExtra("active", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    /**
     * Cancels alarm in given position
     *
     * @param position of alarm alarms
     */
    @Override
    public void cancelAlarm(int position) {
        // Get alarm and manager
        Alarm alarm = medication.getAlarms().get(position);
        alarm.setAlarmOn(false);
        AlarmManager manager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

        // Create intent to be cancelled and cancel it
        Intent intentRemove = new Intent(this, AlarmReceiver.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intentRemove.putExtra("medName", medication.getName());
        intentRemove.putExtra("medDose", alarm.getDose());
        PendingIntent alarmIntentRemove = PendingIntent.getBroadcast(this, alarm.getId(), intentRemove, 0);
        manager.cancel(alarmIntentRemove);

        // Send info to MainActivity
        Intent broadcastIntent = new Intent("modify-data");
        broadcastIntent.putExtra("request", MedicationRequest.SET_ALARM_ACTIVE);
        broadcastIntent.putExtra("medicationIndex", index);
        broadcastIntent.putExtra("alarmIndex", position);
        broadcastIntent.putExtra("active", false);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    /**
     * Remove alarm in given position.
     *
     * @param position of alarm in alarms
     */
    @Override
    public void removeAlarm(int position) {
        // Remove alarm
        medication.getAlarms().remove(position);

        // Send info to MainActivity
        Intent broadcastIntent = new Intent("modify-data");
        broadcastIntent.putExtra("request", MedicationRequest.REMOVE_ALARM);
        broadcastIntent.putExtra("medicationIndex", index);
        broadcastIntent.putExtra("alarmIndex", position);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

        // Update ListView of alarms
        showAlarms();
    }

    /**
     * Set if alarm's dose has been taken.
     *
     * @param position  of alarm in the list
     * @param taken     is dose taken or not
     */
    @Override
    public void setTakenAlarm(int position, boolean taken) {
        // Set info
        medication.getAlarms().get(position).setTaken(taken);

        // Send info to MainActivity
        Intent broadcastIntent = new Intent("modify-data");
        broadcastIntent.putExtra("request", MedicationRequest.SET_TAKEN);
        broadcastIntent.putExtra("medicationIndex", index);
        broadcastIntent.putExtra("alarmIndex", position);
        broadcastIntent.putExtra("taken", taken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
}
