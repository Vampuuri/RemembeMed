package fi.esupponen.remembemed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MedicineActivity extends AppCompatActivity {
    Medication medication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        Bundle b = getIntent().getExtras();
        medication = (Medication) b.getSerializable("medication");

        TextView tv = (TextView) findViewById(R.id.textplace);
        tv.setText(medication.getName());
    }
}
