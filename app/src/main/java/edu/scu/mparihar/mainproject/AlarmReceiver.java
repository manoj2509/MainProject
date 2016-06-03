package edu.scu.mparihar.mainproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;
import java.net.URI;
import java.util.List;

import android.app.NotificationManager;

/**
 * Created by mahendramhatre on 5/25/16.
 */
public class AlarmReceiver extends BroadcastReceiver {
    ProfileDbHelper profileDbHelper;
    EventDbHelper eventDbHelper;
    AudioManager audioManager;
    RingtoneManager ringtoneManager;
    NotificationManager mNotificationManager;
    List<String> profileNames;
    NotificationCompat.Builder mBuilder;
    public void onReceive(Context context, Intent intent) {

        /* String s[] ={"manoj"};
         EventDbHelper eventDbHelper = new EventDbHelper(context);
         String g = eventDbHelper.getAllData(s);
         eventData ed = (eventData) intent.getSerializableExtra("v");
         String []split = g.split(",");*/
        audioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);

        String start = intent.getStringExtra("x");
        profileNames = intent.getStringArrayListExtra("profiles");
        if(start.equalsIgnoreCase("intent_start")) {


            profileDbHelper = new ProfileDbHelper(context);
            EventData ed = (EventData) intent.getParcelableExtra("v");
            String s[] = {ed.getProfile()};

            String prof_info = profileDbHelper.getRowData(s);
            String[] split = prof_info.split(",");

            if (split[2].equalsIgnoreCase("Silent")) {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

            } else if (split[2].equalsIgnoreCase("Vibrate Mode")) {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

            } else {

                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, Integer.parseInt(split[4]), 0);
            }

            //  ringtoneManager.getRingtone(context,Uri.parse(split[3]));

            Toast.makeText(context, "Alarm Triggered " + split[2] + split[4], Toast.LENGTH_LONG).show();

            mBuilder =   new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_add_alert_50dp) // notification icon
                    .setContentTitle("Smart Notifier!") // title for notification
                    .setContentText("Your phone setting has been changed to "+split[2]) // message for notification
                    .setAutoCancel(true); // clear notification after click

            mNotificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());



        }
        else {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            Toast.makeText(context, "Alarm Triggered ", Toast.LENGTH_LONG).show();
            mBuilder =  new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_add_alert_50dp) // notification icon
                    .setContentTitle("Smart Notifier!") // title for notification
                    .setContentText("Your phone setting has been changed to Ringer") // message for notification
                    .setAutoCancel(true); // clear notification after click

           mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());


        }

    }

}