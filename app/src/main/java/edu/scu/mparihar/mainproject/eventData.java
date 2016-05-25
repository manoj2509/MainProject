package edu.scu.mparihar.mainproject;

/**
 * Created by Mj on 24-May-16.
 */
public class eventData {
    String name;
    String profile, beaconId, startTime, endTime, date, repeatArray;
    boolean repeatFlag;


    public eventData() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRepeatArray() {
        return repeatArray;
    }

    public void setRepeatArray(String repeatArray) {
        this.repeatArray = repeatArray;
    }

    public boolean isRepeatFlag() {
        return repeatFlag;
    }

    public void setRepeatFlag(boolean repeatFlag) {
        this.repeatFlag = repeatFlag;
    }

    public eventData(String name, String profile, String beaconId, String startTime, String endTime, String date, String repeatArray, boolean repeatFlag) {
        this.name = name;
        this.profile = profile;
        this.beaconId = beaconId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.repeatArray = repeatArray;
        this.repeatFlag = repeatFlag;


    }
}
