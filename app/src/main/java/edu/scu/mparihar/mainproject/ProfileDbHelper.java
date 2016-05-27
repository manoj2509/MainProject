package edu.scu.mparihar.mainproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahendramhatre on 5/23/16.
 */

public class ProfileDbHelper extends SQLiteOpenHelper {

    protected static final String DATABASE_NAME = "SmartNotifier1.db";
    protected static final int DATABASE_VERSION = 1;

    protected static final String TABLE_NAME = "ProfileManager";

    protected static final String UID = "_id";
    protected static final String PROFILENAME = "profile_name";
    protected static final String TYPE = "type";
    protected static final String RINGTONE = "ringtone";
    protected static final String VOLUME = "volume";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("+
            UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            PROFILENAME + " TEXT, " +
            TYPE + " TEXT, " +
            RINGTONE + " TEXT, "
            +VOLUME + " INTEGER )";

    protected static final String DROP_TABLE ="DROP TABLE IF EXISTS " +TABLE_NAME;
    protected Context context;


    public ProfileDbHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
            Toast.makeText(context, "Database successfully created", Toast.LENGTH_LONG).show();
        } catch (android.database.SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public boolean deleteData(int removeID) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            return db.delete(TABLE_NAME, UID + "=" + removeID, null) > 0;
        } catch (Exception e) {
            return false;
        }
    }


    public long insertData(String profile_name, String type, String ringtone,int volume) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROFILENAME, profile_name);
        contentValues.put(TYPE, type);
        contentValues.put(RINGTONE, ringtone);
        contentValues.put(VOLUME, volume);
        long id = db.insert(TABLE_NAME, null, contentValues);
        if(id == -1) {

        }

        return id;
    }


    public List<ProfileData> getAllProfiles() {
        ProfileData profileData1;
        List<ProfileData> toSend = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ProfileDbHelper.UID, ProfileDbHelper.PROFILENAME, ProfileDbHelper.TYPE,
                ProfileDbHelper.RINGTONE, ProfileDbHelper.VOLUME};
        try {
            Cursor cursor = db.query(ProfileDbHelper.TABLE_NAME, columns, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    // get the data into array, or class variable
                    profileData1 = new ProfileData();

                    profileData1.setId(cursor.getInt(0));
                    profileData1.setName(cursor.getString(1));
                    profileData1.setType(cursor.getString(2));
                    profileData1.setRingtone(cursor.getString(3));
                    profileData1.setVolume(cursor.getInt(4));
                    toSend.add(profileData1);
                } while (cursor.moveToNext());

                db.close();
                cursor.close();
                return toSend;
            }
        } catch (Exception e) {
            Log.v("In All Profile Data: ", e.toString());
        }
        return toSend;
    }
}
