package fi.esupponen.remembemed.dialogfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import fi.esupponen.remembemed.classes.Medication;
import fi.esupponen.remembemed.MedicationRequest;
import fi.esupponen.remembemed.R;

/**
 * @author Essi Supponen [essi.supponen@tuni.fi]
 * @version 2019-04-23
 * @since 1.8
 */
public class EditDialogFragment extends DialogFragment {

    /**
     * Listener for EditDialog.
     */
    EditDialogListener listener;

    /**
     * Interface for updating information.
     */
    public interface EditDialogListener {
        /**
         * Sends updated info to listener.
         *
         * @param request
         * @param updatedInfo
         */
        void updateEditedDialog(MedicationRequest request, String updatedInfo);
    }

    /**
     * Initializes and returns EditDialog.
     *
     * @param medication
     * @param request
     * @return EditDialog
     */
    public static EditDialogFragment getInstance(Medication medication, MedicationRequest request) {
        EditDialogFragment frag = new EditDialogFragment();
        Bundle args = new Bundle();

        // Check which attribute to edit
        args.putSerializable("request", request);
        args.putSerializable("medication", medication);

        if (request.equals(MedicationRequest.CHANGE_NAME)) {
            args.putString("field", "name");
        }

        frag.setArguments(args);

        return frag;
    }

    /**
     * Creates and returns EditDialog
     *
     * @param savedInstance
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        Bundle args = getArguments();

        // Get information from args
        Medication med = (Medication) args.getSerializable("medication");
        final MedicationRequest request = (MedicationRequest) args.getSerializable("request");
        final String field = args.getString("field");

        // Get builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_edit, null);
        ((TextView)layout.findViewById(R.id.editFragmentTitle)).setText("Edit " + field);
        if (request.equals(MedicationRequest.CHANGE_NAME)) {
            ((EditText)layout.findViewById(R.id.editFragmentText)).setHint(med.getName());
        }

        builder.setView(layout);

        // Set OnClickListeners
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.updateEditedDialog(request, ((EditText)layout.findViewById(R.id.editFragmentText)).getText().toString());
                }
            })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            });

        // Build and return dialog
        return builder.create();
    }

    /**
     * Checks if context is EditDialogListener.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (EditDialogListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
