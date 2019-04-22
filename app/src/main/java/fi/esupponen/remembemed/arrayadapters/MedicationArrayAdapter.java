package fi.esupponen.remembemed.arrayadapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fi.esupponen.remembemed.classes.Alarm;
import fi.esupponen.remembemed.classes.Medication;
import fi.esupponen.remembemed.R;

/**
 * @author Essi Supponen [essi.supponen@tuni.fi]
 * @version 2019-04-19
 * @since 1.8
 */
public class MedicationArrayAdapter extends ArrayAdapter {

    /**
     * Context of the array adapter, MainActivity
     */
    private final Activity context;

    /**
     * Array of medications for ListView
     */
    private Medication[] medications;

    /**
     * Constructor for MedicationArrayAdapter.
     *
     * @param context
     * @param medications
     */
    public MedicationArrayAdapter(Activity context, Medication[] medications) {
        super(context, R.layout.list_item, new String[medications.length]);
        this.context = context;
        this.medications = medications;
    }

    /**
     * Returns one view object of the list in the given position.
     *
     * @param position
     * @param view
     * @param parent
     * @return one view object
     */
    public View getView(int position, View view, ViewGroup parent) {

        // Create layout
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View listItemView = layoutInflater.inflate(R.layout.list_item, null, true);

        // Get TextViews from layout
        TextView titleTextView = listItemView.findViewById(R.id.medicineTitle);
        TextView allDosesTextView = listItemView.findViewById(R.id.allDoses);

        // Set title text
        titleTextView.setText(medications[position].getName());

        // Parse an string of every alarm/dose of the medication
        String allDosesString = "";
        List<Alarm> alarms = medications[position].getAlarms();

        if (alarms.size() == 0) {
            allDosesString = "No alarms set.";
        } else {
            for (int i = 0; i < alarms.size(); i++) {
                allDosesString += String.format(
                        "%02d:%02d", alarms.get(i).getHour(), alarms.get(i).getMinute()
                ) + " - " + alarms.get(i).getDose();

                if (alarms.get(i).isTaken()) {
                    allDosesString += " - TAKEN!";
                }

                if (i != alarms.size() - 1) {
                    allDosesString += "\n";
                }
            }
        }

        allDosesTextView.setText(allDosesString);

        return listItemView;
    }
}
