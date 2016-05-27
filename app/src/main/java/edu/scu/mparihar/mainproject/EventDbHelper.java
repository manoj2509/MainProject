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
    protected static final String REPEAT_FLAG = "repeatFlag";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("+
            UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            NAME + " TEXT, " +
            PROFILE + " TEXT, " +   BEACONID + " TEXT, "+
            START + " TEXT, "+
            END + " TEXT, "+
            CDATE + " TEXT, "+
            REPEAT + " TEXT, "
            + REPEAT_FLAG + " INTEGER )";

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

    public long insertData(String name,String profile,String beaconId,String startTime, String endTime, String cdate, String repeatArray, int flag) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(PROFILE, profile);
        contentValues.put(BEACONID, beaconId);
        contentValues.put(START, startTime);
        contentValues.put(END, endTime);
        contentValues.put(CDATE, cdate);
        contentValues.put(REPEAT, repeatArray);
        contentValues.put(REPEAT_FLAG, flag);

        long id = db.insert(TABLE_NAME, null, contentValues);
        if(id == -1) {

        }

        return id;
    }

    public List<EventData> getAllData() {
        EventData eventData1;
        List<EventData> toSend = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {EventDbHelper.UID, EventDbHelper.NAME,EventDbHelper.PROFILE ,
                EventDbHelper.BEACONID, EventDbHelper.REPEAT_FLAG, EventDbHelper.CDATE,
                EventDbHelper.START, EventDbHelper.END, EventDbHelper.REPEAT};
        try {
            Cursor cursor = db.query(EventDbHelper.TABLE_NAME, columns, null, null, null, null, null);
//            int size = cursor.getCount();
//            StringBuffer stringBuffer = new StringBuffer();
            if (cursor.moveToFirst()) {
                do {
                    // get the data into array, or class variable
                    eventData1 = new EventData();
                    eventData1.setId(cursor.getInt(0));
                    eventData1.setName(cursor.getString(1));
                    eventData1.setProfile(cursor.getString(2));
                    eventData1.setBeaconId(cursor.getString(3));
                    eventData1.setRepeatFlag(cursor.getInt(4));
                    eventData1.setDate(cursor.getString(5));
                    eventData1.setStartTime(cursor.getString(6));
                    eventData1.setEndTime(cursor.getString(7));
                    eventData1.setRepeatArray(cursor.getString(8));
                    toSend.add(eventData1);
                } while (cursor.moveToNext());

                db.close();
                return toSend;
            }
        } catch (Exception e) {
            Log.v("In All Data: ", e.toString());
        }
        return toSend;
    }


    public String getRowData( String[] profile_name) {
        String send = null;
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {UID, NAME, PROFILE, BEACONID};
        try {
            Cursor cursor = db.query(TABLE_NAME, columns, NAME+" = ? ", profile_name, null,null,null);
            int size = cursor.getCount();
            StringBuffer buffer = new StringBuffer();

            while (cursor.moveToNext()) {

                int id = cursor.getInt(0);
                String pname = cursor.getString(1);
                String type = cursor.getString(2);
                String ring =cursor.getString(3);
                send = id +","+pname+","+type+","+ring;

            }


        } catch (Exception e) {
            Log.i("AllData", "getAllData: " + e.toString());
            Toast.makeText(context, "getAllData: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        return send;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
