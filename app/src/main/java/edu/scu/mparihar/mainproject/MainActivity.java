package edu.scu.mparihar.mainproject;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.app.AlarmManager;
import java.io.Serializable;
import android.app.PendingIntent;

import org.altbeacon.beacon.BeaconManager;

import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.List;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements Serializable {

    protected static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    int ADD_EVENT_INTENT = 1;
    int ADD_PROFILE_INTENT = 2;
    EventData ed;
    ProfileData newData;
    Context context;
    Intent intentAlarm,intentAlarm1;
    AlarmManager alarmManager,alarmManager1;
    PendingIntent pendingIntent,pendingIntent1;
    static List<EventData> AllData;
    List<ProfileData> AllProfiles;
    List<String> ProfileNames;
    private TabLayout tabLayout;
    SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yy");
    EventDbHelper eventDbHelper;
    ProfileDbHelper profileDbHelper;


    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_assignment_24dp,
            R.drawable.ic_assignment_ind_24dp
    };
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        verifyBluetooth();
        AllData = new ArrayList<>();
        AllProfiles = new ArrayList<>();
        ProfileNames = new ArrayList<>();
        eventDbHelper =  new EventDbHelper(context);
        profileDbHelper = new ProfileDbHelper(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons in the background.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @TargetApi(23)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSION_REQUEST_COARSE_LOCATION);
                    }

                });
                builder.show();
            }
        }
        // Delete past events.
        try {
            eventDbHelper.deletePastData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Extract from database.
        AllData = eventDbHelper.getAllData();
//        Log.v("Getting all data: ", AllData.get(0).getName());
//        Log.v("Getting all data: ", AllData.get(1).getName());
        AllProfiles = profileDbHelper.getAllProfiles();

        Log.v("Accessing All Profiles ", String.valueOf(AllProfiles.size()));


        FloatingActionButton fabEvent = (FloatingActionButton) findViewById(R.id.fabEvent);
        assert fabEvent != null;
        fabEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddEvent.class);
                ProfileNames = new ArrayList<>();
                for (int i = 0; i < AllProfiles.size(); i++) {
                    ProfileNames.add(AllProfiles.get(i).getName());
                }
                intent.putStringArrayListExtra("profiles", (ArrayList<String>) ProfileNames);
                startActivityForResult(intent, ADD_EVENT_INTENT);
            }
        });

        FloatingActionButton fabProfile = (FloatingActionButton) findViewById(R.id.fabProfile);
        if (fabProfile != null) {
            fabProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this,AddProfile.class);
                    startActivityForResult(intent, ADD_PROFILE_INTENT);
                }
            });
        }

        // Putting tabLayout
        TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                animateFab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        };



        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                animateFab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(onPageChangeListener);    //To listen to any page changes.

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(onTabSelectedListener);  //To find which tab is selected.
        setupTabIcons();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle b = new Bundle();
        EventFragment eventFragment = new EventFragment();
        b.putParcelableArrayList("EventData", (ArrayList<? extends Parcelable>) AllData);
        eventFragment.setArguments(b);
        Log.i("In MA: Sent bundle", String.valueOf(AllData.size()));
        adapter.addFrag(eventFragment, "Dashboard");

        Bundle p = new Bundle();
        ProfileFragment profileFragment = new ProfileFragment();
        p.putParcelableArrayList("ProfileData", (ArrayList<? extends Parcelable>) AllProfiles);
        profileFragment.setArguments(p);
        // TODO add data to this.
        adapter.addFrag(profileFragment, "Profiles");
        viewPager.setAdapter(adapter);
    }

    private void animateFab(int position) {
        FloatingActionButton fabP = (FloatingActionButton) findViewById(R.id.fabProfile);
        FloatingActionButton fabE = (FloatingActionButton) findViewById(R.id.fabEvent);
        switch (position) {
            case 0:
                fabE.show();
                fabP.hide();
                break;
            case 1:
                fabP.show();
                fabE.hide();
                break;

            default:
                fabE.show();
                fabP.hide();
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        long difference;
        try {
            if (requestCode == ADD_EVENT_INTENT) {
                if (resultCode == RESULT_OK) {
                    // get object, put to database, notify data-set changed
                    ed = data.getParcelableExtra("Object");
//                    ed = (EventData) data.getSerializableExtra("Object");

                    long r= eventDbHelper.insertData(ed.getName(), ed.getProfile(), ed.getBeaconId(), ed.getStartTime(), ed.getEndTime(), ed.getDate(), ed.getRepeatArray(), ed.getRepeatFlag());
                    Toast.makeText(this,  Integer.toString((int)r), Toast.LENGTH_LONG).show();
                    Date d = formatter.parse(ed.getDate());

                    String formatted = formatter.format(new Date());
                    // difference=(d.getTime()-formatter.parse(formatted).getTime());
                    String[] units = ed.getStartTime().split(":"); //will break the string up into an array
                    int hours = Integer.parseInt(units[0]); //first element
                    int minutes = Integer.parseInt(units[1]); //second element

                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(System.currentTimeMillis());
                    //  c.set(Calendar.MONTH,d.getMonth());
                    // c.set(Calendar.YEAR,d.getYear());
                    //c.set(Calendar.DAY_OF_MONTH,d.getDay());
                    c.set(Calendar.HOUR_OF_DAY,hours);
                    c.set(Calendar.MINUTE,minutes);
                    c.set(Calendar.SECOND,0);
                    long time = new GregorianCalendar().getTimeInMillis();

                    // create an Intent and set the class which will execute when Alarm triggers, here we have
                    // given AlarmReciever in the Intent, the onRecieve() method of this class will execute when
                    // alarm triggers and
                    //we will write the code to send SMS inside onRecieve() method pf Alarmreciever class
                    intentAlarm = new Intent(this, AlarmReceiver.class);
                    intentAlarm.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                    intentAlarm.putExtra("v",ed);
                    intentAlarm.putExtra("x","intent_start");

                    pendingIntent = PendingIntent.getBroadcast(this, (int)r*2, intentAlarm,0);

                    // create the object
                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    //set the alarm for particular time
                    alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                    //Toast.makeText(this, "Success" + time, Toast.LENGTH_LONG).show();

                    //new intent
                    // Setting the end time
                    String[] stop = ed.getEndTime().split(":"); //will break the string up into an array
                    hours = Integer.parseInt(stop[0]); //first element
                    minutes = Integer.parseInt(stop[1]); //second element

                    c.set(Calendar.HOUR_OF_DAY,hours);
                    c.set(Calendar.MINUTE,minutes);
                    c.set(Calendar.SECOND,0);

                    // create an Intent and set the class which will execute when Alarm triggers, here we have
                    // given AlarmReciever in the Intent, the onRecieve() method of this class will execute when
                    // alarm triggers and
                    //we will write the code to send SMS inside onRecieve() method pf Alarmreciever class


                    intentAlarm1 = new Intent(this, AlarmReceiver.class);
                    intentAlarm1.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                    intentAlarm1.putExtra("v",ed);
                    intentAlarm1.putExtra("x","intent_stop");
                    pendingIntent1 = PendingIntent.getBroadcast(this, ((int)r*2)-1, intentAlarm1,0);

                    // create the object
                    alarmManager1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    //set the alarm for particular time
                    alarmManager1.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent1);
                    //Toast.makeText(this, "Success" + time, Toast.LENGTH_LONG).show();
                }
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "There was an error setting the event. Try again.", Toast.LENGTH_LONG).show();
                }

            }

            if (requestCode == ADD_PROFILE_INTENT) {
                if (resultCode == RESULT_OK) {
                    // get object, put to database, notify data set changed.
                    newData = data.getParcelableExtra("Profile");
//                    newData = (ProfileData) data.getSerializableExtra("Profile");
                    Log.v("After getting to insert", "Success");
                    long r = profileDbHelper.insertData(newData.getName(), newData.getType(),
                            newData.getRingtone(), newData.getVolume());
                    Toast.makeText(this,  Integer.toString((int)r), Toast.LENGTH_LONG).show();
                    AllProfiles.add(newData);
                    adapter.notifyDataSetChanged();
                    viewPager.setAdapter(adapter);

                }
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Error encountered", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (ParseException es) {

        }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            AllData.add(ed);
            adapter.notifyDataSetChanged();
            viewPager.setAdapter(adapter);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        ((BeaconApplication) this.getApplicationContext()).setMainActivity(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((BeaconApplication) this.getApplicationContext()).setMainActivity(null);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void verifyBluetooth() {

        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Bluetooth not enabled");
                builder.setMessage("Please enable bluetooth in settings and restart this application.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                        System.exit(0);
                    }
                });
                builder.show();
            }
        }
        catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not available");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                    System.exit(0);
                }

            });
            builder.show();

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.uninstall) {
            uninstallApp();
        }

        return super.onOptionsItemSelected(item);
    }

    public static int convert_time(String time) {


        String[] units = time.split(":"); //will break the string up into an array
        int hours = Integer.parseInt(units[0]); //first element
        int minutes = Integer.parseInt(units[1]); //second element
        return 60 * 60 * hours + minutes * 60;
    }

    public void uninstallApp() {
        startActivity(new Intent(Intent.ACTION_DELETE).setData(Uri.parse("package:edu.scu.mparihar.mainproject")));
    }



//    public void logToDisplay(final String line) {
//        runOnUiThread(new Runnable() {
//            public void run() {
//                EditText editText = (EditText)MainActivity.this
//                        .findViewById(R.id.monitoringText);
//                editText.append(line+"\n");
//            }
//        });
//    }

}
