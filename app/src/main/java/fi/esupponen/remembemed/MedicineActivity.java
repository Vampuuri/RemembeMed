package fi.esupponen.remembemed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MedicineActivity extends AppCompatActivity implements EditDialogFragment.EditDialogListener {
    Medication medication;
    int index;

    public void editMedicineName(View v) {
        EditDialogFragment frag = EditDialogFragment.getInstance(medication, MedicationRequest.CHANGE_NAME);
        frag.show(getFragmentManager(), "editNameDialog");
    }

    public void editMedicineDose(View v) {
        EditDialogFragment frag = EditDialogFragment.getInstance(medication, MedicationRequest.CHANGE_DOSE);
        frag.show(getFragmentManager(), "editNameDialog");
    }

    public void addNewAlarm(View v) {
        AddAlarmDialogFragment frag = new AddAlarmDialogFragment();
        frag.show(getFragmentManager(), "addAlarmDialogFragment");
    }

    public void deleteMedication(View v) {
        // TODO: dialog for making sure, that user really wants to delete medication
        Intent intent = new Intent("modify-data");
        intent.putExtra("request", MedicationRequest.DELETE);
        intent.putExtra("index", index);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        super.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        Bundle b = getIntent().getExtras();
        medication = (Medication) b.getSerializable("medication");
        index = b.getInt("index");

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

        Intent intent = new Intent("modify-data");
        intent.putExtra("request", MedicationRequest.UPDATE);
        intent.putExtra("index", index);
        intent.putExtra("medication", medication);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
