package fi.esupponen.remembemed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import fi.esupponen.remembemed.arrayadapters.MedicationArrayAdapter;
import fi.esupponen.remembemed.classes.Alarm;
import fi.esupponen.remembemed.classes.Medication;
import fi.esupponen.remembemed.dialogfragments.NewMedicationDialogFragment;

/**
 * @author Essi Supponen [essi.supponen@tuni.fi]
 * @version 2019-04-23
 * @since 1.8
 */
public class MainActivity extends AppCompatActivity implements NewMedicationDialogFragment.NewMedicationFragmentListener {
    /**
     * All medications in a list.
     */
    private ArrayList<Medication> medications;

    /**
     * BroadcastReceiver for local broadcasts to update info in json-file.
     */
    private BroadcastReceiver modifyDataReceiver;

    /**
     * Update data in given index of medications.
     *
     * @param index         of medication in list
     * @param updatedData   updated data
     */
    public void updateData(int index, Medication updatedData) {
        // set new data
        medications.get(index).setName(updatedData.getName());

        // Save to file
        writeJsonFile();
        refreshListView();
    }

    /**
     * Delete data in given index of medications.
     *
     * @param index     of medication in list
     */
    public void deleteData(int index) {
        // Delete data
        medications.remove(index);

        // Update to file
        writeJsonFile();
        refreshListView();
    }

    /**
     * Adds alarm to medication in given index.
     *
     * @param index     of medication in list
     * @param alarm
     */
    public void addAlarmToMedication(int index, Alarm alarm) {
        medications.get(index).getAlarms().add(alarm);
        writeJsonFile();
    }

    /**
     * Remove alarm in given index from medication in given index.
     *
     * @param medicationIndex
     * @param alarmIndex
     */
    public void removeAlarmFromMedication(int medicationIndex, int alarmIndex) {
        medications.get(medicationIndex).getAlarms().remove(alarmIndex);
        writeJsonFile();
    }

    /**
     * Set taken to alarm in given index of medication in given index.
     *
     * @param medicationIndex
     * @param alarmIndex
     * @param taken
     */
    public void setTakenDose(int medicationIndex, int alarmIndex, boolean taken) {
        medications.get(medicationIndex).getAlarms().get(alarmIndex).setTaken(taken);
        writeJsonFile();
    }

    /**
     * Set alarm activity in given index of medicatino in given index.
     *
     * @param medicationIndex
     * @param alarmIndex
     * @param active
     */
    public void setAlarmActive(int medicationIndex, int alarmIndex, boolean active) {
        medications.get(medicationIndex).getAlarms().get(alarmIndex).setAlarmOn(active);
        writeJsonFile();
    }

    /**
     * Registers broadcastreceiver for updating data.
     */
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

                } else if (request.equals(MedicationRequest.REMOVE_ALARM)) {
                    int medIndex = intent.getExtras().getInt("medicationIndex");
                    int alarmIndex = intent.getExtras().getInt("alarmIndex");
                    removeAlarmFromMedication(medIndex, alarmIndex);

                } else if (request.equals(MedicationRequest.SET_TAKEN)) {
                    int medIndex = intent.getExtras().getInt("medicationIndex");
                    int alarmIndex = intent.getExtras().getInt("alarmIndex");
                    boolean taken = intent.getExtras().getBoolean("taken");
                    setTakenDose(medIndex, alarmIndex, taken);

                } else if (request.equals(MedicationRequest.SET_ALARM_ACTIVE)) {
                    int medIndex = intent.getExtras().getInt("medicationIndex");
                    int alarmIndex = intent.getExtras().getInt("alarmIndex");
                    boolean active = intent.getExtras().getBoolean("active");
                    setAlarmActive(medIndex, alarmIndex, active);
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(modifyDataReceiver, new IntentFilter("modify-data"));
    }

    /**
     * Creates MainActivity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        medications = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set OnClickListener to ListView
        ListView list = (ListView) findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toMedicationInfo(position);
            }
        });

        // register datareceiver
        registerModifyDataReceiver();

        // read data from file
        readInfoFromFile();
        refreshListView();
    }

    /**
     * Refreshes ListView onResume.
     */
    @Override
    protected void onResume() {
        super.onResume();
        refreshListView();
    }

    /**
     * Unregisters receiver before destruction.
     */
    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(modifyDataReceiver);
        super.onDestroy();
    }

    /**
     * Starts MedicationActivity with medication in given position in list.
     *
     * @param position
     */
    public void toMedicationInfo(int position) {
        Intent intent = new Intent(this, MedicineActivity.class);
        intent.putExtra("medication", medications.get(position));
        intent.putExtra("index", position);
        startActivity(intent);
    }

    /**
     * Updates ListView.
     */
    private void refreshListView() {
        Medication[] medicationsArray = new Medication[medications.size()];

        for (int i = 0; i < medications.size(); i++) {
            medicationsArray[i] = medications.get(i);
        }

        ListView list = (ListView) findViewById(R.id.listView);
        MedicationArrayAdapter arrayAdapter = new MedicationArrayAdapter(this, medicationsArray);
        list.setAdapter(arrayAdapter);
    }

    /**
     * Opens NewMedicationDialog.
     *
     * @param v     Add new medication -button
     */
    public void addNewMedication(View v) {
        NewMedicationDialogFragment dialog = new NewMedicationDialogFragment();
        dialog.show(getFragmentManager(), "newMedicationDialog");
    }

    /**
     * Reads saved info from file.
     *
     * Goes trough JsonString from file. Finds medications and alarms and saves them to medication
     * list.
     */
    public void readInfoFromFile() {
        String jsonString = readJsonStringFromFile();

        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray medicationArray = json.getJSONArray("medications");

            for (int i = 0; i < medicationArray.length(); i++) {
                JSONObject medObject = medicationArray.getJSONObject(i);
                String name = medObject.getString("name");

                Medication newMed = new Medication(name);

                JSONArray alarmArray = medObject.getJSONArray("alarms");

                for (int j = 0; j < alarmArray.length(); j++) {
                    JSONObject alarmObject = alarmArray.getJSONObject(j);
                    int id = alarmObject.getInt("id");
                    int hour = alarmObject.getInt("hour");
                    int minute = alarmObject.getInt("minute");
                    float hourToRepeat = (float)alarmObject.getLong("hourToRepeat");

                    boolean alarmOn = alarmObject.getBoolean("alarmOn");
                    boolean taken = alarmObject.getBoolean("taken");

                    String dose = alarmObject.getString("dose");

                    newMed.getAlarms().add(new Alarm(id, hour, minute, hourToRepeat, alarmOn, taken, dose));
                }

                medications.add(newMed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens file and reads data.
     *
     * @return String of json from file
     */
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
        return writer.toString();
    }

    /**
     * Creates JsonObject of data and saves it to file.
     */
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

    /**
     * Creates JsonObject of medication data in application.
     *
     * @return JsonObject of all data
     */
    public JSONObject makeJsonObject() {
        JSONObject jsonObject = new JSONObject();

        try {
            JSONArray array = new JSONArray();

            for (Medication med : medications) {
                JSONObject medObject = new JSONObject();
                medObject.put("name", med.getName());

                JSONArray alarmArray = new JSONArray();

                for (Alarm alarm : med.getAlarms()) {
                    JSONObject alarmObject = new JSONObject();
                    alarmObject.put("id", alarm.getId());
                    alarmObject.put("hour", alarm.getHour());
                    alarmObject.put("minute", alarm.getMinute());
                    alarmObject.put("hourToRepeat", alarm.getHourToRepeat());
                    alarmObject.put("alarmOn", alarm.isAlarmOn());
                    alarmObject.put("taken", alarm.isTaken());
                    alarmObject.put("dose", alarm.getDose());

                    alarmArray.put(alarmObject);
                }

                medObject.put("alarms", alarmArray);
                array.put(medObject);
            }

            jsonObject.put("medications", array);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     * Adds new medication with given name. Redirects user to MedicineActivity of new medication.
     *
     * @param name
     */
    @Override
    public void addNewMedication(String name) {
        if (name == null || name.equals("")) {
            // Show toast if name was empty
            Toast.makeText(this, "Cannot create medication with empty name. Please try again", Toast.LENGTH_LONG).show();
        } else {
            // Create new Medication object and save it.
            Medication newMed = new Medication(name);
            medications.add(newMed);
            writeJsonFile();
            refreshListView();

            // Start MedicineActivity of newly created Medication.
            Intent intent = new Intent(this, MedicineActivity.class);
            intent.putExtra("medication", newMed);
            intent.putExtra("index", medications.size()-1);
            startActivity(intent);
        }
    }
}
