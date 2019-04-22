package fi.esupponen.remembemed.dialogfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import fi.esupponen.remembemed.R;

public class AddAlarmDialogFragment extends DialogFragment implements View.OnClickListener {
    View layout;
    int hours;
    int minutes;
    double repeatAfterHour;
    String dose;

    AddAlarmDialogFragmentListener listener;

    public interface AddAlarmDialogFragmentListener {
        void addAlarm(int hours, int minutes, double repeatAfterHour, String dose);
    }

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
        dose = "";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        layout = inflater.inflate(R.layout.dialog_newalarm, null);
        ((RadioButton)layout.findViewById(R.id.radioButtonNoRepeat)).toggle();

        ((RadioButton)layout.findViewById(R.id.radioButtonNoRepeat)).setOnClickListener(this);
        ((RadioButton)layout.findViewById(R.id.radioButton24h)).setOnClickListener(this);
        ((RadioButton)layout.findViewById(R.id.radioButton48h)).setOnClickListener(this);
        ((RadioButton)layout.findViewById(R.id.radioButton72h)).setOnClickListener(this);

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
                sendAlarmInfo();
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

    public void sendAlarmInfo() {
        listener.addAlarm(hours,minutes,repeatAfterHour,dose);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddAlarmDialogFragment.AddAlarmDialogFragmentListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
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


        Log.d("AddAlarmDialog, onClick", "" + repeatAfterHour);
    }
}
