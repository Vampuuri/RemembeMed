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

public class AlarmArrayAdapter extends ArrayAdapter {
    private final Activity context;
    private AlarmManaging manager;
    private Alarm[] alarms;

    public interface AlarmManaging {
        void cancelAlarm(int position);
        void activateAlarm(int position);
        void removeAlarm(int position);
        void setTakenAlarm(int position, boolean taken);
    }

    public AlarmArrayAdapter(Activity context, AlarmManaging manager, Alarm[] alarms) {
        super(context, R.layout.list_item, new String[alarms.length]);
        this.context = context;
        this.manager = manager;
        this.alarms = alarms;
    }

    private void setCheckedOnOff(CheckBox onOff, boolean isOn) {
        onOff.setChecked(isOn);
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View listItemView = layoutInflater.inflate(R.layout.list_alarmitem, null, true);

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

        TextView titleTextView = listItemView.findViewById(R.id.alarmTitle);
        TextView repeatTextView = listItemView.findViewById(R.id.alarmInfo);
        TextView doseTextView = listItemView.findViewById(R.id.alarmDose);

        titleTextView.setText(String.format(
                "%02d:%02d", alarms[position].getHour(), alarms[position].getMinute()
        ));
        if (alarms[position].getHourToRepeat() == 0) {
            repeatTextView.setText("No repeat");
        } else {
            repeatTextView.setText(String.format("Repeat every %2.0f hours", alarms[position].getHourToRepeat()));
        }

        doseTextView.setText(alarms[position].getDose());

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
