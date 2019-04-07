package fi.esupponen.remembemed;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditDialogFragment extends DialogFragment {
    public static EditDialogFragment getInstance(Medication medication, String field) {
        EditDialogFragment frag = new EditDialogFragment();
        Bundle args = new Bundle();
        args.putString("field", field);
        args.putSerializable("medication", medication);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        Bundle args = getArguments();

        Medication med = (Medication) args.getSerializable("medication");
        String field = args.getString("field");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_edit, null);
        ((TextView)layout.findViewById(R.id.editFragmentTitle)).setText("Edit " + field);
        if (field.equals("name")) {
            ((EditText)layout.findViewById(R.id.editFragmentText)).setHint(med.getName());
        } else {
            ((EditText)layout.findViewById(R.id.editFragmentText)).setHint(med.getDose());
        }

        builder.setView(layout);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

        return builder.create();
    }
}
