package edu.scu.mparihar.mainproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ViewEventActivity extends AppCompatActivity {

    ArrayList<String> profiles = new ArrayList<>();
    ArrayList<String> beacons = new ArrayList<>();
    public static long EVENT_ID;
    EditText eventNameView, editDateView, toTimeView, fromTimeView;
    Spinner profile_spinnerView;
    Context context;
    Switch beacon_toggleView, repeat_infoView;
    Spinner beacon_spinnerView;

    EventData eventObject, eventData;
    ToggleButton toggleButtonMonView, toggleButtonTueView, toggleButtonWedView, toggleButtonThuView,
            toggleButtonFriView, toggleButtonSatView, toggleButtonSunView;
    Button event_cancelView, event_submitView;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("Position");
        eventData = MainActivity.AllData.get(position);
        setTitle(eventData.getName());

        context = this;
        // Initializations.
        eventNameView = (EditText) findViewById(R.id.event_name_view);
        assert eventNameView != null;
        eventNameView.setEnabled(false);

        editDateView = (EditText) findViewById(R.id.editDate_view);
        assert editDateView != null;
        editDateView.setEnabled(false);

        fromTimeView = (EditText) findViewById(R.id.from_time_view);
        assert fromTimeView != null;
        fromTimeView.setEnabled(false);

        toTimeView = (EditText) findViewById(R.id.to_time_view);
        assert toTimeView != null;
        toTimeView.setEnabled(false);

        profile_spinnerView = (Spinner) findViewById(R.id.profile_spinner_view);
        assert profile_spinnerView != null;
        profile_spinnerView.setEnabled(false);

        beacon_spinnerView = (Spinner) findViewById(R.id.beacon_spinner_view);
        assert beacon_spinnerView != null;
        beacon_spinnerView.setEnabled(false);

        beacon_toggleView = (Switch) findViewById(R.id.beacon_toggle_view);
        assert beacon_toggleView != null;
        beacon_toggleView.setEnabled(false);

        repeat_infoView = (Switch) findViewById(R.id.repeat_info_view);
        assert repeat_infoView != null;
        repeat_infoView.setEnabled(false);

        event_cancelView = (Button) findViewById(R.id.event_cancel_view);
        event_submitView = (Button) findViewById(R.id.event_submit_view);
        assert event_submitView != null;
        event_submitView.setEnabled(false);
        event_submitView.setVisibility(View.INVISIBLE);

        final LinearLayout dayOfTime = (LinearLayout) findViewById(R.id.days_linear_layout_view);
        final LinearLayout date_time_layout = (LinearLayout) findViewById(R.id.date_time_layout_view);

        toggleButtonMonView = (ToggleButton) findViewById(R.id.toggleButtonMon_view);
        assert toggleButtonMonView != null;
        toggleButtonMonView.setEnabled(false);
        toggleButtonTueView = (ToggleButton) findViewById(R.id.toggleButtonTue_view);
        assert toggleButtonTueView != null;
        toggleButtonTueView.setEnabled(false);
        toggleButtonWedView = (ToggleButton) findViewById(R.id.toggleButtonWed_view);
        assert toggleButtonWedView != null;
        toggleButtonWedView.setEnabled(false);
        toggleButtonThuView = (ToggleButton) findViewById(R.id.toggleButtonThu_view);
        assert toggleButtonThuView != null;
        toggleButtonThuView.setEnabled(false);
        toggleButtonFriView = (ToggleButton) findViewById(R.id.toggleButtonFri_view);
        assert toggleButtonFriView != null;
        toggleButtonFriView.setEnabled(false);
        toggleButtonSatView = (ToggleButton) findViewById(R.id.toggleButtonSat_view);
        assert toggleButtonSatView != null;
        toggleButtonSatView.setEnabled(false);
        toggleButtonSunView = (ToggleButton) findViewById(R.id.toggleButtonSun_view);
        assert toggleButtonSunView != null;
        toggleButtonSunView.setEnabled(false);

        // // Initial settings.
        assert date_time_layout != null;
        assert dayOfTime != null;

        beacon_toggleView.setChecked(!eventData.getBeaconId().matches("-1"));
        repeat_infoView.setChecked(eventData.getRepeatFlag() == 1);
        eventNameView.setText(eventData.getName());


        // Logic for any changes.
//        profiles = getIntent().getStringArrayListExtra("profiles");
//        profiles = MainActivity.AllProfiles;
        int selectedProfilePos = 0;
        for (int i = 0; i < MainActivity.AllProfiles.size(); i++) {
            profiles.add(MainActivity.AllProfiles.get(i).getName());
            if (profiles.get(i).matches(eventData.getProfile())) {
                selectedProfilePos = i;
            } else {
                selectedProfilePos = 0;
            }
        }
        ArrayAdapter<String> profileAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, profiles);
        profile_spinnerView.setAdapter(profileAdapter);
        profile_spinnerView.setSelection(selectedProfilePos);
        profile_spinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProfile = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), selectedProfile, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Getting spinner elements for Beacon.
        int selectedBeaconPos = 0;
        beacons.add("c22d3f7f-22f8-49ef-a72e-7d6f0b6b990a");
        beacons.add("b9407f30-f5f8-466e-aff9-25556b57fe6d");
        beacons.add("Beacon ID3");
        if (!eventData.getBeaconId().equals("-1")) {
            for (int i = 0; i < beacons.size(); i++) {
                if (eventData.getBeaconId().equals(beacons.get(i))) {
                    selectedBeaconPos = i;
                }
            }
        }
        ArrayAdapter<String> beaconAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, beacons);
        beacon_spinnerView.setAdapter(beaconAdapter);
        beacon_spinnerView.setSelection(selectedBeaconPos);
        beacon_spinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProfile = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), selectedProfile, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // DatePicker onClick.
        final Calendar datePicker = Calendar.getInstance();
        // Setting default value.
        datePicker.getTime();
//        updateDateText(datePicker);
        if (!eventData.getDate().equals("0")) {
            updateDateText(eventData.getDate());
        } else {
            updateDateText(datePicker);
        }
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                datePicker.set(Calendar.YEAR, year);
                datePicker.set(Calendar.MONTH, monthOfYear);
                datePicker.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateText(datePicker);
            }
        };
        editDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, dateSetListener, datePicker
                        .get(Calendar.YEAR), datePicker.get(Calendar.MONTH),
                        datePicker.get(Calendar.DAY_OF_MONTH)).show();
                Toast.makeText(context, "Reached to datePicker", Toast.LENGTH_LONG).show();
            }
        });
        // TimePicker onClick.
        final Calendar timePicker = Calendar.getInstance();
        fromTimeView.setText(eventData.getStartTime());
        fromTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        fromTimeView.setText(hourOfDay + ":" + minute);
                    }
                }, timePicker.get(Calendar.HOUR_OF_DAY), timePicker.get(Calendar.MINUTE), true);
                mTimePicker.setTitle("From Time");
                mTimePicker.show();
            }
        });
        toTimeView.setText(eventData.getEndTime());
        toTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        toTimeView.setText(hourOfDay + ":" + minute);
                    }
                }, timePicker.get(Calendar.HOUR_OF_DAY), timePicker.get(Calendar.MINUTE), true);
                mTimePicker.setTitle("Time Upto");
                mTimePicker.show();
            }
        });
        // Set repeat days here.
        String temp = eventData.getRepeatArray();
        if (!temp.equals("")){
            toggleButtonMonView.setChecked(temp.substring(0, 1).equals("1"));
            toggleButtonTueView.setChecked(temp.substring(1, 2).equals("1"));
            toggleButtonWedView.setChecked(temp.substring(2, 3).equals("1"));
            toggleButtonThuView.setChecked(temp.substring(3, 4).equals("1"));
            toggleButtonFriView.setChecked(temp.substring(4, 5).equals("1"));
            toggleButtonSatView.setChecked(temp.substring(5, 6).equals("1"));
            toggleButtonSunView.setChecked(temp.substring(6, 7).equals("1"));
        }
        //attach a listener to check for changes in state
        beacon_toggleView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    // Code for switch on.
                    beacon_spinnerView.setVisibility(View.VISIBLE);
                    repeat_infoView.setVisibility(View.GONE);
                    dayOfTime.setVisibility(View.GONE);
                    editDateView.setVisibility(View.GONE);
                    date_time_layout.setVisibility(View.GONE);
                } else {
                    // Code for beacon switch off.
                    beacon_spinnerView.setVisibility(View.GONE);
                    date_time_layout.setVisibility(View.VISIBLE);
                    repeat_infoView.setVisibility(View.VISIBLE);
                    if (repeat_infoView.isChecked()) {
                        dayOfTime.setVisibility(View.VISIBLE);
                    } else {
                        editDateView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        if (beacon_toggleView.isChecked()) {
            // Code for switch on.
            beacon_spinnerView.setVisibility(View.VISIBLE);
            repeat_infoView.setVisibility(View.GONE);
            dayOfTime.setVisibility(View.GONE);
            editDateView.setVisibility(View.GONE);
            date_time_layout.setVisibility(View.GONE);
        } else {
            // Code for switch off.
            beacon_spinnerView.setVisibility(View.GONE);
            date_time_layout.setVisibility(View.VISIBLE);
            repeat_infoView.setVisibility(View.VISIBLE);
            if (repeat_infoView.isChecked()) {
                dayOfTime.setVisibility(View.VISIBLE);
            } else {
                editDateView.setVisibility(View.VISIBLE);
            }
        }

        //attach a listener to check for changes in state
        repeat_infoView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    // Code for switch on.
                    dayOfTime.setVisibility(View.VISIBLE);
                    editDateView.setVisibility(View.GONE);
                } else {
                    // Code for beacon switch off.
                    dayOfTime.setVisibility(View.GONE);
                    editDateView.setVisibility(View.VISIBLE);

                }
            }
        });

        if (repeat_infoView.isChecked()) {
            // Code for switch on.
            dayOfTime.setVisibility(View.VISIBLE);
            editDateView.setVisibility(View.GONE);
        } else {
            // Code for switch off.
            dayOfTime.setVisibility(View.GONE);
            editDateView.setVisibility(View.VISIBLE);
        }

        // On cancel button.
        event_cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent resultIntent = new Intent();
                setResult(RESULT_FIRST_USER);
                finish();
            }
        });

        // On Submit button.

        event_submitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventObject = new EventData();
                eventObject.setName(eventNameView.getText().toString());                                // 1
                int flag = 0;
                eventObject.setStartTime(fromTimeView.getText().toString());                            // 2
                eventObject.setEndTime(toTimeView.getText().toString());                                // 3

                if (eventObject.getName().isEmpty()) {
                    eventNameView.setError("Enter a Name too!");
                    flag = 1;
                } else {
                    eventObject.setProfile(profile_spinnerView.getSelectedItem().toString());           // 4
                    if (beacon_toggleView.isChecked()) {
                        eventObject.setBeaconId(beacon_spinnerView.getSelectedItem().toString());       // 5
                        eventObject.setRepeatFlag(0);                                               // 6
                        eventObject.setDate("0");                                                   // 7
                        eventObject.setRepeatArray("");                                             // 8
                    } else {
                        eventObject.setBeaconId("-1");                                              // 5
                        if (eventObject.getStartTime().equals("")) {
                            fromTimeView.setError("Choose start time!");
                            flag = 1;
                        }
                        if (eventObject.getEndTime().equals("")) {
                            toTimeView.setError("Choose End time!");
                            flag = 2;
                        }
                        if (repeat_infoView.isChecked()) {
                            eventObject.setRepeatFlag(1);                                        // 6
                            // Get marked days.
                            StringBuilder stringBuilder = new StringBuilder();
                            int temp;
                            temp = toggleButtonMonView.isChecked() ? 1 : 0;
                            stringBuilder.append(Integer.toString(temp));
                            temp = toggleButtonTueView.isChecked() ? 1 : 0;
                            stringBuilder.append(Integer.toString(temp));
                            temp = toggleButtonWedView.isChecked() ? 1 : 0;
                            stringBuilder.append(Integer.toString(temp));
                            temp = toggleButtonThuView.isChecked() ? 1 : 0;
                            stringBuilder.append(Integer.toString(temp));
                            temp = toggleButtonFriView.isChecked() ? 1 : 0;
                            stringBuilder.append(Integer.toString(temp));
                            temp = toggleButtonSatView.isChecked() ? 1 : 0;
                            stringBuilder.append(Integer.toString(temp));
                            temp = toggleButtonSunView.isChecked() ? 1 : 0;
                            stringBuilder.append(Integer.toString(temp));
                            eventObject.setRepeatArray(stringBuilder.toString());                   // 8
                            if (eventObject.getRepeatArray().equals("0000000")) {
                                flag = 3;
                                fromTimeView.setError("Select some days below!");
                            }
                            eventObject.setDate("0");                                               // 7


                        } else {
                            eventObject.setRepeatFlag(0);                                       // 6
                            eventObject.setDate(editDateView.getText().toString());                     // 7
                            eventObject.setRepeatArray("");                                         // 8
                            if (eventObject.getDate().isEmpty()) {
                                flag = 4;
                                editDateView.setError("Input a date!");
                            }
                        }
                    }
                }


                if (flag == 0) {
                    MainActivity.AllData.set(position, eventObject);
                    MainActivity.eventDbHelper.updateData(eventData.getId(), eventObject);
                    EventFragment.mRecyclerviewAdapterEvent.notifyDataSetChanged();
//                    MainActivity.adapter.notifyDataSetChanged();
//                    Intent resultIntent = new Intent();
//                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }

            }
        });

    }

    private void updateDateText(String date) {
        editDateView.setText(date);
    }

    private void updateDateText(Calendar datePicker) {
        String dateFormat = "yy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        editDateView.setText(sdf.format(datePicker.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_edit, menu);
        MenuItem menuItem = menu.findItem(R.id.edit);
        if (!flag) {
            menuItem.setVisible(false);
            flag = true;
        }
        return true;
    }

    public static boolean flag = true;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit) {
            makeEditable();
            flag = false;
            invalidateOptionsMenu();
        }
        if (id == R.id.delete_view) {
            MainActivity.AllData.remove(position);
            MainActivity.eventDbHelper.deleteData(eventData.getId());
            EventFragment.mRecyclerviewAdapterEvent.notifyDataSetChanged();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeEditable() {
        eventNameView.setEnabled(true);
        editDateView.setEnabled(true);
        fromTimeView.setEnabled(true);
        toTimeView.setEnabled(true);
        profile_spinnerView.setEnabled(true);
        beacon_spinnerView.setEnabled(true);
        beacon_toggleView.setEnabled(true);
        repeat_infoView.setEnabled(true);
        event_submitView.setEnabled(true);
        event_submitView.setVisibility(View.VISIBLE);
        toggleButtonMonView.setEnabled(true);
        toggleButtonTueView.setEnabled(true);
        toggleButtonWedView.setEnabled(true);
        toggleButtonThuView.setEnabled(true);
        toggleButtonFriView.setEnabled(true);
        toggleButtonSatView.setEnabled(true);
        toggleButtonSunView.setEnabled(true);
    }

}
