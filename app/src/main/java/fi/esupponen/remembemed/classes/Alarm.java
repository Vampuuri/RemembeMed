package fi.esupponen.remembemed.classes;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * @author Essi Supponen [essi.supponen@tuni.fi]
 * @version 2019-04-22
 * @since 1.8
 */
public class Alarm implements Serializable {

    /**
     * Static list of all used ids
     */
    public static LinkedList<Integer> ALL_IDS = new LinkedList<>();

    /**
     * Unique id
     */
    int id;

    /**
     * Hour of the day of the alarm
     */
    int hour;

    /**
     * Minute of the day of the alarm
     */
    int minute;

    /**
     * How long time between repeating alarm. 0 = no repeat
     */
    float hourToRepeat;

    /**
     * Is the alarm on or off
     */
    boolean alarmOn;

    /**
     * Is the dose taken or not
     */
    boolean taken;

    /**
     * Text description of the dose.
     */
    String dose;

    /**
     * Constructor of Alarm, generates own id.
     *
     * @param hour
     * @param minute
     * @param hourToRepeat
     * @param alarmOn
     * @param taken
     * @param dose
     */
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

    /**
     * Constructor of Alarm, checks if given id is already in use. If not, use it, if is, generate a new one.
     *
     * @param id
     * @param hour
     * @param minute
     * @param hourToRepeat
     * @param alarmOn
     * @param taken
     * @param dose
     */
    public Alarm(int id, int hour, int minute, float hourToRepeat, boolean alarmOn, boolean taken, String dose) {
        this.hour = hour;
        this.minute = minute;
        this.hourToRepeat = hourToRepeat;
        this.alarmOn = alarmOn;
        this.taken = taken;
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

    /**
     * Returns hour.
     *
     * @return hour of day
     */
    public int getHour() {
        return hour;
    }

    /**
     * Sets given hour of day.
     *
     * @param hour
     */
    public void setHour(int hour) {
        this.hour = hour;
    }

    /**
     * Returns minute.
     *
     * @return minute of day.
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Sets given minute of day.
     *
     * @param minute
     */
    public void setMinute(int minute) {
        this.minute = minute;
    }

    /**
     * Returns amount of hours between repeats.
     *
     * @return hours between repeats
     */
    public float getHourToRepeat() {
        return hourToRepeat;
    }

    /**
     * Sets amout of hours between repeats.
     *
     * @param hourToRepeat
     */
    public void setHourToRepeat(float hourToRepeat) {
        this.hourToRepeat = hourToRepeat;
    }

    /**
     * Returns alarmOn.
     *
     * @return if alarm is on
     */
    public boolean isAlarmOn() {
        return alarmOn;
    }

    /**
     * Sets if alarm is on.
     *
     * @param alarmOn
     */
    public void setAlarmOn(boolean alarmOn) {
        this.alarmOn = alarmOn;
    }

    /**
     * Return isTaken.
     *
     * @return if dose has been taken.
     */
    public boolean isTaken() {
        return taken;
    }

    /**
     * Sets if dose has been taken.
     *
     * @param taken
     */
    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    /**
     * Returns unique id of this alarm.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns dose of this alarm.
     *
     * @return dose
     */
    public String getDose() {
        return dose;
    }

    /**
     * Sets dose of this alarm.
     *
     * @param dose
     */
    public void setDose(String dose) {
        this.dose = dose;
    }
}
