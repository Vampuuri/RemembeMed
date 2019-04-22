package fi.esupponen.remembemed.dialogfragments;

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

import fi.esupponen.remembemed.classes.Medication;
import fi.esupponen.remembemed.MedicationRequest;
import fi.esupponen.remembemed.R;

public class EditDialogFragment extends DialogFragment {

    EditDialogListener listener;

    public interface EditDialogListener {
        void updateEditedDialog(MedicationRequest request, String updatedInfo);
    }

    public static EditDialogFragment getInstance(Medication medication, MedicationRequest request) {
        EditDialogFragment frag = new EditDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("request", request);
        args.putSerializable("medication", medication);

        if (request.equals(MedicationRequest.CHANGE_NAME)) {
            args.putString("field", "name");
        } else if (request.equals(MedicationRequest.CHANGE_DOSE)) {
            args.putString("field", "dose");
        }

        frag.setArguments(args);

        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        Bundle args = getArguments();

        Medication med = (Medication) args.getSerializable("medication");
        final MedicationRequest request = (MedicationRequest) args.getSerializable("request");
        final String field = args.getString("field");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_edit, null);
        ((TextView)layout.findViewById(R.id.editFragmentTitle)).setText("Edit " + field);
        if (request.equals(MedicationRequest.CHANGE_NAME)) {
            ((EditText)layout.findViewById(R.id.editFragmentText)).setHint(med.getName());
        }

        builder.setView(layout);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.updateEditedDialog(request, ((EditText)layout.findViewById(R.id.editFragmentText)).getText().toString());
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
            listener = (EditDialogListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
