package edu.scu.mparihar.mainproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Mj on 24-May-16.
 */
public class EventDbHelper extends SQLiteOpenHelper{

    protected Context context;
    protected static final String DATABASE_NAME = "SmartNotifier.db";
    protected static final int DATABASE_VERSION = 1;
    protected static final String TABLE_NAME = "EventManager";
    protected static final String UID = "_id";
    protected static final String NAME = "name";
    protected static final String PROFILE = "profile";
    protected static final String BEACONID = "beaconId";
    protected static final String START = "startTime";
    protected static final String END = "endTime";
    protected static final String CDATE = "cdate";
    protected static final String REPEAT = "repeatArray";
    protected static final String FLAG = "repeatFlag";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("+
            UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            NAME + " TEXT, " +
            PROFILE + " TEXT, " +   BEACONID + " TEXT, "+
            START + " TEXT, "+
            END + " TEXT, "+
            CDATE + " TEXT, "+
            REPEAT + " TEXT, "
            +FLAG + " BOOLEAN )";

    protected static final String DROP_TABLE ="DROP TABLE IF EXISTS " +TABLE_NAME;





    public EventDbHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
        } catch (android.database.SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public boolean deleteData(int removeID) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            return db.delete(TABLE_NAME, UID + "=" + removeID, null) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public long insertData(String name,String profile,String beaconId,String startTime, String endTime, String cdate, String repeatArray, boolean flag) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(PROFILE, profile);
        contentValues.put(BEACONID, beaconId);
        contentValues.put(START, startTime);
        contentValues.put(END, endTime);
        contentValues.put(CDATE, cdate);
        contentValues.put(REPEAT, repeatArray);
        contentValues.put(FLAG, flag);

        long id = db.insert(TABLE_NAME, null, contentValues);
        if(id == -1) {

        }

        return id;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
