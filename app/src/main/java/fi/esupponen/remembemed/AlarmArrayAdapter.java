package fi.esupponen.remembemed;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AlarmArrayAdapter extends ArrayAdapter {
    private final Activity context;
    private Alarm[] alarms;

    public AlarmArrayAdapter(Activity context, Alarm[] alarms) {
        super(context, R.layout.list_item, new String[alarms.length]);
        this.context = context;
        this.alarms = alarms;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View listItemView = layoutInflater.inflate(R.layout.list_alarmitem, null, true);

        TextView titleTextView = listItemView.findViewById(R.id.alarmTitle);
        TextView repeatTextView = listItemView.findViewById(R.id.alarmInfo);
        TextView doseTextView = listItemView.findViewById(R.id.alarmDose);

        titleTextView.setText(String.format(
                "%02d:%02d", alarms[position].getHour(), alarms[position].getMinute()
        ));
        if (alarms[position].getHourToRepeat() == 0) {
            repeatTextView.setText("No repeat");
        } else {
            repeatTextView.setText("Repeat: every " + alarms[position].getHourToRepeat() + " hours");
        }

        doseTextView.setText(alarms[position].getDose());

        return listItemView;
    }
}
