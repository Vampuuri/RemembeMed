package fi.esupponen.remembemed;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

public class AddAlarmDialogFragment extends DialogFragment implements View.OnClickListener {
    View layout;
    int hours;
    int minutes;
    double repeatAfterHour;

    public void openSelectTimeFragment() {
        TimeDialogFragment frag = new TimeDialogFragment();
        frag.setParent(this);
        frag.show(getFragmentManager(), "timeDialogFragment");
    }

    public void setTime(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
        ((TextView)layout.findViewById(R.id.selectedTime)).setText(String.format(
                "Selected time: %02d:%02d", hours, minutes
        ));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        hours = 0;
        minutes = 0;
        repeatAfterHour = 0;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        layout = inflater.inflate(R.layout.dialog_newalarm, null);
        ((RadioButton)layout.findViewById(R.id.radioButtonNoRepeat)).toggle();

        ((RadioButton)layout.findViewById(R.id.radioButtonNoRepeat)).setOnClickListener(this);
        ((RadioButton)layout.findViewById(R.id.radioButton12h)).setOnClickListener(this);
        ((RadioButton)layout.findViewById(R.id.radioButton24h)).setOnClickListener(this);
        ((RadioButton)layout.findViewById(R.id.radioButton48h)).setOnClickListener(this);

        setTime(0,0);

        Button addTimeButton = (Button) layout.findViewById(R.id.selectTimeButton);
        addTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectTimeFragment();
            }
        });

        builder.setView(layout);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent("add-alarm");
                LocalBroadcastManager.getInstance(AddAlarmDialogFragment.this.getActivity()).sendBroadcast(intent);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("EditDialogFragment", "cancelled");
                    }
                });

        return builder.create();
    }

    @Override
    public void onClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radioButtonNoRepeat:
                if (checked) {
                    repeatAfterHour = 0;
                }
                break;
            case R.id.radioButton12h:
                if (checked) {
                    repeatAfterHour = 12;
                }
                break;
            case R.id.radioButton24h:
                if (checked) {
                    repeatAfterHour = 24;
                }
                break;
            case R.id.radioButton48h:
                if (checked) {
                    repeatAfterHour = 48;
                }
                break;
        }

        Log.d("AddAlarmDialog, onClick", "" + repeatAfterHour);
    }
}
