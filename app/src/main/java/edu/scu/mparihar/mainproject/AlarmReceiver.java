package edu.scu.mparihar.mainproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;

/**
 * Created by mahendramhatre on 5/25/16.
 */
public class AlarmReceiver extends BroadcastReceiver {
    ProfileDbHelper profileDbHelper;
    EventDbHelper eventDbHelper;
    AudioManager audioManager;
    RingtoneManager ringtoneManager;
    public void onReceive(Context context, Intent intent) {

        /* String s[] ={"manoj"};
         EventDbHelper eventDbHelper = new EventDbHelper(context);
         String g = eventDbHelper.getAllData(s);
         eventData ed = (eventData) intent.getSerializableExtra("v");
         String []split = g.split(",");*/
        audioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        profileDbHelper =  new ProfileDbHelper(context);
        EventData ed = (EventData) intent.getSerializableExtra("v");
        String s[] = {ed.getProfile()};
        String prof_info = profileDbHelper.getRowData(s);
        String []split = prof_info.split(",");
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM,Integer.parseInt(split[4]),0);
        ringtoneManager.getRingtone(context,Uri.parse(split[3]));

        Toast.makeText(context, "Alarm Triggered "+s[0] ,Toast.LENGTH_LONG).show();

    }

}