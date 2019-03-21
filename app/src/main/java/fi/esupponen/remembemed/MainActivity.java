package fi.esupponen.remembemed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Medication> medications;

    public void setExampleAlarm(int hour, int minute) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        medications = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
