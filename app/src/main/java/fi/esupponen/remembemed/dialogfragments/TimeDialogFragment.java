package fi.esupponen.remembemed.dialogfragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.TimePicker;

public class TimeDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    AddAlarmDialogFragment parent;

    public void setParent(AddAlarmDialogFragment parent) {
        this.parent = parent;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        parent.setTime(hourOfDay, minute);
    }
}
