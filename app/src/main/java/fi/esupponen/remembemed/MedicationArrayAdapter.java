package fi.esupponen.remembemed;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

public class MedicationArrayAdapter extends ArrayAdapter {
    private final Activity context;

    public MedicationArrayAdapter(Activity context, Medication[] medications) {
        super(context, R.layout.list_item);
        this.context = context;
    }
}
