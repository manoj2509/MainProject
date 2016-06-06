package edu.scu.mparihar.mainproject;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.List;

/**
 * Created by Mj on 05-Jun-16.
 */
public class MyBeaconService extends Service implements BeaconConsumer {

    protected static final String TAG = "MyBeaconService";
//    private static List<EventData> eventDatas;
    static List<String> beaconIds;
    static ProfileData profileForBeacon;
    private BeaconManager beaconManager;
    public static Context context;
    private AudioManager audioManager;
    NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;


    /** indicates how to behave if the service is killed */
    int mStartMode;

    /** interface for clients that bind */
    IBinder mBinder;

    /** indicates whether onRebind should be used */
    boolean mAllowRebind;

//    /** Called when the service is being created. */
//    @Override
//    public boolean bindService(Intent intent, ServiceConnection conn, int mode) {
//        return context.bindService(intent, conn, mode);
//    }
//
//    @Override
//    public void unbindService(ServiceConnection conn) {
//        context.unbindService(conn);
//    }
    /** The service is starting, due to a call to startService() */

    @Override
    public void onCreate() {
        super.onCreate();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
        Log.e("", "onCreate");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplication().getApplicationContext();
        beaconManager = BeaconManager.getInstanceForApplication(context);
        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
        return mStartMode;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        beaconManager.setBackgroundScanPeriod(1100L);
        beaconManager.setBackgroundBetweenScanPeriod(10000L);

        try {
            beaconManager.updateScanPeriods();
        } catch (Exception e) {
            e.printStackTrace();
        }
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                try {
                    Log.e(TAG, "didEnterRegion");
                    beaconManager.startRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didExitRegion(Region region) {
                try {
                    Log.e(TAG, "didExitRegion");
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    notifyUser("Normal Ringer Mode");
                    beaconManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {

            }
        });

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    //EditText editText = (EditText)RangingActivity.this.findViewById(R.id.rangingText);
                    Beacon firstBeacon = beacons.iterator().next();
                    Log.v("Beacon ID Service ", "" + firstBeacon.getId1());
                    getAllEventBeacons();
                    int pos = checkForBeacon(firstBeacon.getId1(), firstBeacon.getId2(), firstBeacon.getId3());
                    if (pos > -1) {
                        // TODO Set profile properties of matching beacon.

//                        Toast.makeText(context, "Beacon Intent detected", Toast.LENGTH_SHORT).show();
//                        Log.v("Beacon found", "You can do your stuff here.");
                        getProfileForBeacon(firstBeacon.getId1().toString());
                        int flag = 0;
                        if (profileForBeacon.getType().equalsIgnoreCase("Silent")) {
                            if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT)
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                            flag = 1;
                        } else if (profileForBeacon.getType().equalsIgnoreCase("Ringer Mode")) {
                            if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL &&
                                    audioManager.getStreamVolume(AudioManager.STREAM_RING) !=
                                            profileForBeacon.getVolume()) {
                                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                audioManager.setStreamVolume(AudioManager.STREAM_RING,
                                        profileForBeacon.getVolume(), 0);
                                flag = 2;
                            }
                        } else {
                            if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_VIBRATE) {
                                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                                flag = 3;
                            }
                        }
                        if (flag != 0) {
                            notifyUser(profileForBeacon.getName());
                        }

                    } else {
//                        Toast.makeText(context, "Beacon Intent detected but no beacon found", Toast.LENGTH_SHORT).show();
                        Log.v("Beacon not found", "You can do your stuff here.");
                    }

//                    logToDisplay("The first beacon " + firstBeacon.toString() + " is about " + firstBeacon.getDistance() + " meters away.");
                }
            }

        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region(
                    "myBeaons", null, null, null));
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }

    private void notifyUser(String name) {
        mBuilder =   new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon_outline) // notification icon
                .setContentTitle("Smart Notifier!") // title for notification
                .setContentText("Your phone setting has been changed to "+name) // message for notification
                .setAutoCancel(true); // clear notification after click

        mNotificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    private void getProfileForBeacon(String BeaconId) {
        EventDbHelper eventDbHelper = new EventDbHelper(context);
        String profileName = eventDbHelper.getProfileNameforBeaconId(BeaconId);
        ProfileDbHelper profileDbHelper = new ProfileDbHelper(context);
        profileForBeacon = profileDbHelper.getProfilebyProfileName(profileName);
        Log.i("All Beacon profile data", "Data fetched");
    }

    private void getAllEventBeacons() {
        EventDbHelper eventDbHelper = new EventDbHelper(context);
        beaconIds = eventDbHelper.getAllBeaconIds();
        Log.i("All data", "Data fetched");
    }

//    @Override
//    public Context getApplicationContext() {
//        return null;
//    }

    private int checkForBeacon(Identifier firstBeaconId1, Identifier firstBeaconId2, Identifier firstBeaconId3) {
        int i;

        for (i = 0; i < beaconIds.size(); i++) {
            if (beaconIds.get(i).equalsIgnoreCase(firstBeaconId1.toString())) {
//                Toast.makeText(context,  "In proximity of a beacon", Toast.LENGTH_LONG).show();
                Log.v("My Beacon Service", "Event detection successful" + beaconIds.get(i));
                return i;
            } else {
                Log.v("My Beacon Service", "Events detection unsuccessful" + beaconIds.get(i));
            }
        }
        return -1;
    }

//    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
