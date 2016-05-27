package edu.scu.mparihar.mainproject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mj on 24-May-16.
 */
public class EventData implements Parcelable {
    int id;
    String name;
    String profile, beaconId, startTime, endTime, date, repeatArray;
    int repeatFlag;

    protected EventData(Parcel in) {
        id = in.readInt();
        name = in.readString();
        profile = in.readString();
        beaconId = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        date = in.readString();
        repeatArray = in.readString();
        repeatFlag = in.readInt();
    }

    public static final Creator<EventData> CREATOR = new Creator<EventData>() {
        @Override
        public EventData createFromParcel(Parcel in) {
            return new EventData(in);
        }

        @Override
        public EventData[] newArray(int size) {
            return new EventData[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public EventData() {
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

    public int getRepeatFlag() {
        return repeatFlag;
    }

    public void setRepeatFlag(int repeatFlag) {
        this.repeatFlag = repeatFlag;
    }

    public EventData(int id, String name, String profile, String beaconId, String startTime,
                     String endTime, String date, String repeatArray, int repeatFlag) {
        this.id =id;
        this.name = name;
        this.profile = profile;
        this.beaconId = beaconId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.repeatArray = repeatArray;
        this.repeatFlag = repeatFlag;


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(profile);
        dest.writeString(beaconId);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(date);
        dest.writeString(repeatArray);
        dest.writeInt(repeatFlag);
    }
}
