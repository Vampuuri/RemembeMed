package fi.esupponen.remembemed.arrayadapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import fi.esupponen.remembemed.classes.Alarm;
import fi.esupponen.remembemed.R;

/**
 * @author Essi Supponen [essi.supponen@tuni.fi]
 * @version 2019-04-22
 * @since 1.8
 */
public class AlarmArrayAdapter extends ArrayAdapter {

    /**
     * Context of the array adapter, MedicationActivity
     */
    private final Activity context;

    /**
     * AlarmManaging class, also MedicationActivity
     */
    private AlarmManaging manager;

    /**
     * Array of alarms for ListView
     */
    private Alarm[] alarms;

    /**
     * Interface for any methods needed for OnClickListeners and OnCheckedChangedListeners
     */
    public interface AlarmManaging {
        /**
         * Cancel given alarm.
         *
         * @param position of alarm in the list
         */
        void cancelAlarm(int position);

        /**
         * Activate given alarm.
         *
         * @param position of alarm in the list
         */
        void activateAlarm(int position);

        /**
         * Remove given alarm.
         *
         * @param position of alarm in the list
         */
        void removeAlarm(int position);

        /**
         * Set taken dose for alarm.
         *
         * @param position  of alarm in the list
         * @param taken     is dose taken or not
         */
        void setTakenAlarm(int position, boolean taken);
    }

    /**
     * Constructor for AlarmArrayAdapter
     *
     * @param context   context, MedicationActivity
     * @param manager   AlarmManaging, also Mediation Activity
     * @param alarms    array of alarms
     */
    public AlarmArrayAdapter(Activity context, AlarmManaging manager, Alarm[] alarms) {
        super(context, R.layout.list_item, new String[alarms.length]);
        this.context = context;
        this.manager = manager;
        this.alarms = alarms;
    }

    /**
     * Help method for taken CheckBox OnCheckedChangeListener
     *
     * @param onOff CheckBox to change
     * @param isOn  new status
     */
    private void setCheckedOnOff(CheckBox onOff, boolean isOn) {
        onOff.setChecked(isOn);
    }

    /**
     * Returns one view object of the list in the given position.
     *
     * @param position
     * @param view
     * @param parent
     * @return one view object
     */
    public View getView(final int position, View view, ViewGroup parent) {

        // Create layout
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View listItemView = layoutInflater.inflate(R.layout.list_alarmitem, null, true);

        // Set checkBoxOnOff status and listener
        final CheckBox checkBoxOnOff = (CheckBox)listItemView.findViewById(R.id.buttonOnOff);
        checkBoxOnOff.setChecked(alarms[position].isAlarmOn());
        checkBoxOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    manager.activateAlarm(position);
                } else {
                    manager.cancelAlarm(position);
                }
            }
        });

        // Set doseTakenCheckBox status and listener
        ((CheckBox)listItemView.findViewById(R.id.doseTakenCheckBox)).setChecked(alarms[position].isTaken());
        ((CheckBox)listItemView.findViewById(R.id.doseTakenCheckBox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manager.setTakenAlarm(position, isChecked);
                if (alarms[position].getHourToRepeat() == 0) {
                    setCheckedOnOff(checkBoxOnOff, false);
                }
            }
        });

        // Get TextViews from layout
        TextView titleTextView = listItemView.findViewById(R.id.alarmTitle);
        TextView repeatTextView = listItemView.findViewById(R.id.alarmInfo);
        TextView doseTextView = listItemView.findViewById(R.id.alarmDose);

        // Set texts to TextViews
        titleTextView.setText(String.format(
                "%02d:%02d", alarms[position].getHour(), alarms[position].getMinute()
        ));
        if (alarms[position].getHourToRepeat() == 0) {
            repeatTextView.setText("No repeat");
        } else {
            repeatTextView.setText(String.format("Repeat every %2.0f hours", alarms[position].getHourToRepeat()));
        }

        doseTextView.setText(alarms[position].getDose());

        // Set listener for removeButton
        ((Button)listItemView.findViewById(R.id.buttonDeleteAlarm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.cancelAlarm(position);
                manager.removeAlarm(position);
            }
        });

        return listItemView;
    }
}
