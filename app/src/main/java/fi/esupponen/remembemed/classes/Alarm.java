package fi.esupponen.remembemed.classes;

import java.io.Serializable;
import java.util.LinkedList;

public class Alarm implements Serializable {
    public static LinkedList<Integer> ALL_IDS = new LinkedList<>();

    int id;
    int hour;
    int minute;
    float hourToRepeat;

    boolean alarmOn;
    boolean taken;

    String dose;

    public Alarm(int hour, int minute, float hourToRepeat, boolean alarmOn, boolean taken, String dose) {
        this.hour = hour;
        this.minute = minute;
        this.hourToRepeat = hourToRepeat;
        this.alarmOn = alarmOn;
        this.taken = taken;
        this.dose = dose;

        this.id = (int)(Math.random()*100000);

        while (ALL_IDS.contains(Integer.valueOf(this.id))) {
            this.id = (int)(Math.random()*100000);
        }

        ALL_IDS.add(Integer.valueOf(this.id));
    }

    public Alarm(int id, int hour, int minute, float hourToRepeat, boolean alarmOn, String dose) {
        this.hour = hour;
        this.minute = minute;
        this.hourToRepeat = hourToRepeat;
        this.alarmOn = alarmOn;
        this.dose = dose;

        if (ALL_IDS.contains(Integer.valueOf(id))) {
            this.id = (int)(Math.random()*100000);

            while (ALL_IDS.contains(Integer.valueOf(this.id))) {
                this.id = (int)(Math.random()*100000);
            }
        } else {
            this.id = id;
        }

        ALL_IDS.add(Integer.valueOf(this.id));
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public float getHourToRepeat() {
        return hourToRepeat;
    }

    public void setHourToRepeat(float hourToRepeat) {
        this.hourToRepeat = hourToRepeat;
    }

    public boolean isAlarmOn() {
        return alarmOn;
    }

    public void setAlarmOn(boolean alarmOn) {
        this.alarmOn = alarmOn;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public int getId() {
        return id;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }
}
