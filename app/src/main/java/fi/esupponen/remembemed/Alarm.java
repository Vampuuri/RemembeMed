package fi.esupponen.remembemed;

import java.io.Serializable;
import java.util.LinkedList;

public class Alarm implements Serializable {
    public static LinkedList<Integer> allIds = new LinkedList<>();

    int id;
    int hour;
    int minute;
    float hourToRepeat;
    boolean alarmOn;

    String dose;

    public Alarm(int id, int hour, int minute, float hourToRepeat, boolean alarmOn, String dose) {
        this.hour = hour;
        this.minute = minute;
        this.hourToRepeat = hourToRepeat;
        this.alarmOn = alarmOn;
        this.dose = dose;
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
