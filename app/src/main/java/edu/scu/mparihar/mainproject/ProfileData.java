package edu.scu.mparihar.mainproject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mj on 26-May-16.
 */
public class ProfileData implements Parcelable {
    int id, volume;
    String name, type, ringtone;

    protected ProfileData(Parcel in) {
        id = in.readInt();
        volume = in.readInt();
        name = in.readString();
        type = in.readString();
        ringtone = in.readString();
    }

    public static final Creator<ProfileData> CREATOR = new Creator<ProfileData>() {
        @Override
        public ProfileData createFromParcel(Parcel in) {
            return new ProfileData(in);
        }

        @Override
        public ProfileData[] newArray(int size) {
            return new ProfileData[size];
        }
    };

    public ProfileData() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRingtone() {
        return ringtone;
    }

    public void setRingtone(String ringtone) {
        this.ringtone = ringtone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(volume);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(ringtone);
    }
}
