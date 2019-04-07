package fi.esupponen.remembemed;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class NewMedicationDialogFragment extends DialogFragment {

    NewMedicationFragmentListener listener;

    public interface NewMedicationFragmentListener {
        void addNewMedication(String name, String dose);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_newmed, null);
        builder.setView(layout);

        builder.setTitle("Add new medication")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = ((EditText)layout.findViewById(R.id.newMedName)).getText().toString();
                String dose = ((EditText)layout.findViewById(R.id.newMedName)).getText().toString();
                listener.addNewMedication(name, dose);
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
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (NewMedicationFragmentListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
