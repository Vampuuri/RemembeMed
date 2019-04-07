package fi.esupponen.remembemed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MedicineActivity extends AppCompatActivity {
    Medication medication;

    public void editMedicineName(View v) {
        Log.d("MedicineActivity", "Edit name");
    }

    public void editMedicineDose(View v) {
        Log.d("MedicineActivity", "Edit dose");
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
        tvTitle.setText(medication.getName());
    }
}
