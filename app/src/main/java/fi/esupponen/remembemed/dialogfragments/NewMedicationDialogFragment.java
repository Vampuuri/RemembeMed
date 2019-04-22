package fi.esupponen.remembemed.dialogfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import fi.esupponen.remembemed.R;

/**
 * @author Essi Supponen [essi.supponen@tuni.fi]
 * @version 2019-04-23
 * @since 1.8
 */
public class NewMedicationDialogFragment extends DialogFragment {

    /**
     * Listener for NewMedicationFragment.
     */
    NewMedicationFragmentListener listener;

    /**
     * Interface for adding new medication.
     */
    public interface NewMedicationFragmentListener {
        /**
         * Adds new medication with given name.
         *
         * @param name
         */
        void addNewMedication(String name);
    }

    /**
     * Creates and returns dialog.
     *
     * @param savedInstance
     * @return NewMedicationDialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {

        // Get builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set layout
        final View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_newmed, null);
        builder.setView(layout);

        // Set OnCLickListeners
        builder.setTitle("Add new medication")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = ((EditText)layout.findViewById(R.id.newMedName)).getText().toString();
                listener.addNewMedication(name);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });

        // Build and return dialog.
        return builder.create();
    }

    /**
     * Checks if context is MewMedicationFragmentListener.
     *
     * @param context
     */
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
