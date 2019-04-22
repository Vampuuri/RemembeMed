package fi.esupponen.remembemed.dialogfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import fi.esupponen.remembemed.R;

/**
 * @author Essi Supponen [essi.supponen@tuni.fi]
 * @version 2019-04-23
 * @since 1.8
 */
public class AddAlarmDialogFragment extends DialogFragment implements View.OnClickListener {

    /**
     * Layout of the dialog.
     */
    View layout;

    /**
     * Hour of day of the alarm.
     */
    int hours;

    /**
     * Minute of day of the alarm.
     */
    int minutes;

    /**
     * Hours between repeats, 0 = no repeats.
     */
    double repeatAfterHour;

    /**
     * Dose of the alarm.
     */
    String dose;

    /**
     * Listener for adding alarm.
     */
    AddAlarmDialogFragmentListener listener;

    /**
     * Interface for adding alarms.
     */
    public interface AddAlarmDialogFragmentListener {

        /**
         * Adds alarm.
         *
         * @param hours
         * @param minutes
         * @param repeatAfterHour
         * @param dose
         */
        void addAlarm(int hours, int minutes, double repeatAfterHour, String dose);
    }

    /**
     * Opens dialog for setting time.
     */
    public void openSelectTimeFragment() {
        TimeDialogFragment frag = new TimeDialogFragment();
        frag.setParent(this);
        frag.show(getFragmentManager(), "timeDialogFragment");
    }

    /**
     * Sets hour of day and minute of day of the alarm.
     *
     * @param hours     hour of day
     * @param minutes   minute of day
     */
    public void setTime(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
        ((TextView)layout.findViewById(R.id.selectedTime)).setText(String.format(
                "Selected time: %02d:%02d", hours, minutes
        ));
    }

    /**
     * Creates dialog and adds listeners to buttons.
     *
     * @param savedInstance
     * @return dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {

        // Set initial values
        hours = 0;
        minutes = 0;
        repeatAfterHour = 0;
        dose = "";

        // Set builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        layout = inflater.inflate(R.layout.dialog_newalarm, null);
        ((RadioButton)layout.findViewById(R.id.radioButtonNoRepeat)).toggle();

        // Set OnClickListener to radiobuttons
        ((RadioButton)layout.findViewById(R.id.radioButtonNoRepeat)).setOnClickListener(this);
        ((RadioButton)layout.findViewById(R.id.radioButton24h)).setOnClickListener(this);
        ((RadioButton)layout.findViewById(R.id.radioButton48h)).setOnClickListener(this);
        ((RadioButton)layout.findViewById(R.id.radioButton72h)).setOnClickListener(this);

        // Set TextChangedListener for newAlarmDose
        ((EditText)layout.findViewById(R.id.newAlarmDose)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                dose = s.toString();
            }
        });

        // set time to layout
        setTime(0,0);

        // Add OnClickListener to addTimeButton
        Button addTimeButton = (Button) layout.findViewById(R.id.selectTimeButton);
        addTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectTimeFragment();
            }
        });

        // Set layout to view
        builder.setView(layout);

        // Set negative and positive button
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendAlarmInfo();
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });

        // Create and return dialog
        return builder.create();
    }

    /**
     * Sends gathered information to listener.
     */
    public void sendAlarmInfo() {
        listener.addAlarm(hours,minutes,repeatAfterHour,dose);
    }

    /**
     * Checks if context is AddAlarmDialogFragmentListener.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddAlarmDialogFragment.AddAlarmDialogFragmentListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Methdo for every radiobutton.
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radioButtonNoRepeat:
                if (checked) {
                    repeatAfterHour = 0;
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
            case R.id.radioButton72h:
                if (checked) {
                    repeatAfterHour = 72;
                }
                break;
        }
    }
}
