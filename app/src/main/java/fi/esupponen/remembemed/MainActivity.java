package fi.esupponen.remembemed;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Paint;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Time;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NewMedicationDialogFragment.NewMedicationFragmentListener {
    private ArrayList<Medication> medications;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    private BroadcastReceiver modifyDataReceiver;

    public void updateData(int index, Medication updatedData) {
        medications.get(index).setName(updatedData.getName());
        medications.get(index).setDose(updatedData.getDose());

        writeJsonFile();
        refreshListView();
    }

    public void deleteData(int index) {
        medications.remove(index);

        writeJsonFile();
        refreshListView();
    }

    public void addAlarmToMedication(int index, Alarm alarm) {
        medications.get(index).getAlarms().add(alarm);
    }

    private void registerModifyDataReceiver() {
        modifyDataReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                MedicationRequest request = (MedicationRequest) intent.getExtras().getSerializable("request");

                if (request.equals(MedicationRequest.UPDATE)) {
                    Medication medication = (Medication) intent.getExtras().getSerializable("medication");
                    int index = intent.getExtras().getInt("index");
                    updateData(index, medication);
                } else if (request.equals(MedicationRequest.DELETE)) {
                    int index = intent.getExtras().getInt("index");
                    deleteData(index);
                } else if (request.equals(MedicationRequest.ADD_ALARM)) {
                    Alarm alarm = (Alarm) intent.getExtras().getSerializable("alarm");
                    int index = intent.getExtras().getInt("index");
                    addAlarmToMedication(index, alarm);
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(modifyDataReceiver, new IntentFilter("modify-data"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        medications = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView list = (ListView) findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toMedicationInfo(view, position, id);
            }
        });

        registerModifyDataReceiver();

        readInfoFromFile();
        refreshListView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshListView();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(modifyDataReceiver);
        super.onDestroy();
    }

    public void toMedicationInfo(View view, int position, long id) {
        Intent intent = new Intent(this, MedicineActivity.class);

        intent.putExtra("medication", medications.get(position));

        startActivity(intent);
    }

    private void refreshListView() {
        Medication[] medicationsArray = new Medication[medications.size()];

        for (int i = 0; i < medications.size(); i++) {
            medicationsArray[i] = medications.get(i);
        }

        ListView list = (ListView) findViewById(R.id.listView);
        MedicationArrayAdapter arrayAdapter = new MedicationArrayAdapter(this, medicationsArray);
        list.setAdapter(arrayAdapter);
    }

    public void addNewMedication(View v) {
        NewMedicationDialogFragment dialog = new NewMedicationDialogFragment();
        dialog.show(getFragmentManager(), "newMedicationDialog");
    }

    public void readInfoFromFile() {
        String jsonString = readJsonStringFromFile();

        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray medicationArray = json.getJSONArray("medications");

            for (int i = 0; i < medicationArray.length(); i++) {
                JSONObject medObject = medicationArray.getJSONObject(i);
                String name = medObject.getString("name");
                String dose = medObject.getString("dose");

                medications.add(new Medication(name, dose));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readJsonStringFromFile() {
        Writer writer = new StringWriter();

        try {
            InputStream infoInput = openFileInput("info.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(infoInput, "UTF-8"));
            String line = reader.readLine();

            while (line != null) {
                writer.write(line);
                line = reader.readLine();
            }

            infoInput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("readJson", writer.toString());
        return writer.toString();
    }

    public void writeJsonFile() {
        JSONObject jsonObject = makeJsonObject();

        try {
            File file = new File(this.getFilesDir(), "info.json");

            if (file.isDirectory()) {
                file.delete();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream stream = new FileOutputStream(file);
            stream.write(jsonObject.toString().getBytes());
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject makeJsonObject() {
        JSONObject jsonObject = new JSONObject();

        try {
            JSONArray array = new JSONArray();

            for (Medication med : medications) {
                JSONObject medObject = new JSONObject();
                medObject.put("name", med.getName());
                medObject.put("dose", med.getDose());
                array.put(medObject);
            }

            jsonObject.put("medications", array);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    public void addNewMedication(String name, String dose) {
        if (name == null || dose == null || name.equals("") || name.equals("")) {
            Log.d("addNewMedication", "empty field");
            // Implement warning later
        } else {
            Medication newMed = new Medication(name, dose);
            medications.add(newMed);
            writeJsonFile();
            refreshListView();

            Intent intent = new Intent(this, MedicineActivity.class);
            intent.putExtra("medication", newMed);
            startActivity(intent);
        }
    }
}
