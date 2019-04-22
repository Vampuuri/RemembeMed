package fi.esupponen.remembemed.dialogfragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.TimePicker;

/**
 * @author Essi Supponen [essi.supponen@tuni.fi]
 * @version 2019-04-23
 * @since 1.8
 */
public class TimeDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    /**
     * AddAlarmDialogFragment that is TimeDialogFragment's parent.
     */
    AddAlarmDialogFragment parent;

    /**
     * Sets parent.
     *
     * @param parent
     */
    public void setParent(AddAlarmDialogFragment parent) {
        this.parent = parent;
    }

    /**
     * Creates and returns dialog.
     *
     * @param savedInstanceState
     * @return TimeDialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create and return premade TimePickerDialog
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    /**
     * Forwards selected time to parent.
     *
     * @param view
     * @param hourOfDay
     * @param minute
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        parent.setTime(hourOfDay, minute);
    }
}
