package fi.esupponen.remembemed;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
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

public class MainActivity extends AppCompatActivity {
    private ArrayList<Medication> medications;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    /**
    public int millisToAlarm(int currentH, int alarmH, int currentM, int alarmM) {
        int currentInMinutes = currentH * 60 + currentM;
        int alarmInMinutes = alarmH * 60 + alarmM;

        if (currentInMinutes < alarmInMinutes) {
            return (alarmInMinutes - currentInMinutes) * 60000;
        } else if (currentInMinutes > alarmInMinutes) {
            return 24*60*60000 - (currentInMinutes - alarmInMinutes);
        } else {
            return 0;
        }
    }

    public void setExampleAlarm(int hour, int minute) {
        alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        Time now = new Time(System.currentTimeMillis());

        int currentHour = now.getHours();
        int currentMin = now.getMinutes();

        Log.d("MainActivity", "hour: " + currentHour + " min: " + currentMin);
        Log.d("MainActivity", "millisToAlarm: " + millisToAlarm(currentHour, hour, currentMin, minute));

        alarmManager.set(AlarmManager.RTC_WAKEUP, millisToAlarm(currentHour, hour, currentMin, minute), alarmIntent);
    }*/

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

        readInfoFromFile();
        refreshListView();

        //setExampleAlarm(20,55);
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
        EditText nameEdit = (EditText) findViewById(R.id.editName);
        EditText doseEdit = (EditText) findViewById(R.id.editDose);

        String nameText = nameEdit.getText().toString();
        String doseText = doseEdit.getText().toString();

        if (nameText == null || doseText == null || nameText.equals("") || nameText.equals("")) {
            Log.d("addNewMedication", "empty field");
        } else {
            medications.add(new Medication(nameText, doseText));
        }

        nameEdit.setText("");
        doseEdit.setText("");

        refreshListView();
        writeJsonFile();
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
}
