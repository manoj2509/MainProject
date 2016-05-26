package edu.scu.mparihar.mainproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by mahendramhatre on 5/25/16.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
         eventData ed = (eventData) intent.getSerializableExtra("v");


        Toast.makeText(context, "Alarm Triggered "+ ed.getName(), Toast.LENGTH_LONG).show();

    }
}