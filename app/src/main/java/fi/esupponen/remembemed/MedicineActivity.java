package fi.esupponen.remembemed;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MedicineActivity extends AppCompatActivity implements EditDialogFragment.EditDialogListener {
    Medication medication;

    public void editMedicineName(View v) {
        EditDialogFragment frag = EditDialogFragment.getInstance(medication, MedicationRequest.CHANGE_NAME);
        frag.show(getFragmentManager(), "editNameDialog");
    }

    public void editMedicineDose(View v) {
        EditDialogFragment frag = EditDialogFragment.getInstance(medication, MedicationRequest.CHANGE_DOSE);
        frag.show(getFragmentManager(), "editNameDialog");
    }

    public void addNewAlarm(View v) {
        Log.d("MedicineActivity", "Add alarm");
    }

    public void deleteMedication(View v) {
        Log.d("MedicineActivity", "Delete medication");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        Bundle b = getIntent().getExtras();
        medication = (Medication) b.getSerializable("medication");

        TextView tvTitle = (TextView) findViewById(R.id.nameView);
        tvTitle.setText(medication.getName());

        TextView tvDosage = (TextView) findViewById(R.id.dosageView);
        tvDosage.setText(medication.getDose());
    }

    @Override
    public void updateEditedDialog(MedicationRequest request, String updatedInfo) {
        if (request.equals(MedicationRequest.CHANGE_NAME) && !updatedInfo.equals("")) {
            medication.setName(updatedInfo);
            ((TextView)findViewById(R.id.nameView)).setText(updatedInfo);
        } else if (request.equals(MedicationRequest.CHANGE_DOSE) && !updatedInfo.equals("")) {
            medication.setDose(updatedInfo);
            ((TextView)findViewById(R.id.dosageView)).setText(updatedInfo);
        }

        Log.d("MedicineActivity", "update info " + medication.toString());
    }
}
