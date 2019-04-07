package fi.esupponen.remembemed;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddAlarmDialogFragment extends DialogFragment {
    public void openSelectTimeFragment() {
        TimeDialogFragment frag = new TimeDialogFragment();
        frag.show(getFragmentManager(), "timeDialogFragment");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_newalarm, null);

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
                //listener.updateEditedDialog(request, ((EditText)layout.findViewById(R.id.editFragmentText)).getText().toString());
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
}
