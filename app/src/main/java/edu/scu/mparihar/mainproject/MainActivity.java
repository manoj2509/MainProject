package edu.scu.mparihar.mainproject;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements Serializable {

    EventData ed;
    ProfileData newData;
    Context context;
    Intent intentAlarm;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    List<EventData> AllData = new ArrayList<>();
    List<ProfileData> AllProfiles = new ArrayList<>();
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

        eventDbHelper =  new EventDbHelper(context);
        profileDbHelper = new ProfileDbHelper(context);


        FloatingActionButton fabEvent = (FloatingActionButton) findViewById(R.id.fabEvent);
        assert fabEvent != null;
        fabEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddEvent.class);
                startActivityForResult(intent, 1);
            }
        });

        FloatingActionButton fabProfile = (FloatingActionButton) findViewById(R.id.fabProfile);
        if (fabProfile != null) {
            fabProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this,AddProfile.class);
                    startActivityForResult(intent,2);
                }
            });
        }

        // Extract from database.
        AllData = eventDbHelper.getAllData();
//        Log.v("Getting all data: ", AllData.get(0).getName());
//        Log.v("Getting all data: ", AllData.get(1).getName());
        AllProfiles = profileDbHelper.getAllProfiles();
        Log.v("Accessing All Profiles ", String.valueOf(AllProfiles.size()));

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        long difference;
        try {
            if (requestCode == 1) {
                if (resultCode == RESULT_OK) {
                    // get object, put to database, notify data-set changed
//                    ed = (EventData) data.getParcelableExtra("Object");
                    ed = (EventData) data.getSerializableExtra("Object");

                    long r = eventDbHelper.insertData(ed.getName(), ed.getProfile(), ed.getBeaconId(), ed.getStartTime(), ed.getEndTime(), ed.getDate(), ed.getRepeatArray(), ed.getRepeatFlag());
                    Toast.makeText(this,  Integer.toString((int)r), Toast.LENGTH_LONG).show();
                    Date d = formatter.parse(ed.getDate());

                    String formatted = formatter.format(new Date());
                    // difference=(d.getTime()-formatter.parse(formatted).getTime());

                    long time = new GregorianCalendar().getTimeInMillis()+15*1000;


                    // create an Intent and set the class which will execute when Alarm triggers, here we have
                    // given AlarmReciever in the Intent, the onRecieve() method of this class will execute when
                    // alarm triggers and
                    //we will write the code to send SMS inside onRecieve() method pf Alarmreciever class
                    intentAlarm = new Intent(this, AlarmReceiver.class);
                    intentAlarm.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                    intentAlarm.putExtra("v",ed);
                    pendingIntent = PendingIntent.getBroadcast(this, (int)r, intentAlarm,0);

                    // create the object
                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    //set the alarm for particular time
                    alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
                    //Toast.makeText(this, "Success" + time, Toast.LENGTH_LONG).show();

                    // TODO
                    AllData.add(ed);
                    adapter.notifyDataSetChanged();
                    viewPager.setAdapter(adapter);

                    //set one more alarm for toggling back to the nonrmal mode

                }


            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "There was an error setting the event. Try again.", Toast.LENGTH_LONG).show();
            }

            if (requestCode == 2) {
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

                }
            }
        }
        catch (ParseException es) {

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

        return super.onOptionsItemSelected(item);
    }

    public static int convert_time(String time) {


        String[] units = time.split(":"); //will break the string up into an array
        int hours = Integer.parseInt(units[0]); //first element
        int minutes = Integer.parseInt(units[1]); //second element
        int duration = 60 * 60 * hours + minutes * 60; //add up our values
        return duration;
    }


}
