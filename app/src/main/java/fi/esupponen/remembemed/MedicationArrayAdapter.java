package fi.esupponen.remembemed;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MedicationArrayAdapter extends ArrayAdapter {
    private final Activity context;
    private Medication[] medications;

    public MedicationArrayAdapter(Activity context, Medication[] medications) {
        super(context, R.layout.list_item, new String[medications.length]);
        this.context = context;
        this.medications = medications;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View listItemView = layoutInflater.inflate(R.layout.list_item, null, true);

        TextView titleTextView = listItemView.findViewById(R.id.medicineTitle);
        TextView allDosesTextView = listItemView.findViewById(R.id.allDoses);

        titleTextView.setText(medications[position].getName());

        String allDosesString = "";
        List<Alarm> alarms = medications[position].getAlarms();

        if (alarms.size() == 0) {
            allDosesString = "No alarms set.";
        } else {
            for (int i = 0; i < alarms.size(); i++) {
                allDosesString += String.format(
                        "%02d:%02d", alarms.get(i).getHour(), alarms.get(i).getMinute()
                ) + " - " + alarms.get(i).getDose();

                if (i != alarms.size() - 1) {
                    allDosesString += "\n";
                }
            }
        }

        allDosesTextView.setText(allDosesString);

        return listItemView;
    }
}
