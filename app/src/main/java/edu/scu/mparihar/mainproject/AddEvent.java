package edu.scu.mparihar.mainproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

/**
 * Created by Mj on 23-May-16.
 */
public class AddEvent extends AppCompatActivity {

    // Default variables.
    EventData eventObject;
    ArrayList<String> profiles = new ArrayList<>();
    ArrayList<String> beacons = new ArrayList<>();
    EditText eventName, editDate, toTime, fromTime;
    Spinner profile_spinner;
    Context context;
    Switch beacon_toggle, repeat_info;
    Spinner beacon_spinner;
    ToggleButton toggleButtonMon, toggleButtonTue, toggleButtonWed, toggleButtonThu,
            toggleButtonFri, toggleButtonSat, toggleButtonSun;
    Button event_cancel, event_submit;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        context = this;
        // Initialize all elements.
        eventName = (EditText) findViewById(R.id.event_name);
        editDate = (EditText) findViewById(R.id.editDate);
        fromTime = (EditText) findViewById(R.id.from_time);
        toTime = (EditText) findViewById(R.id.to_time);

        profile_spinner = (Spinner) findViewById(R.id.profile_spinner);
        beacon_spinner = (Spinner) findViewById(R.id.beacon_spinner);

        beacon_toggle = (Switch) findViewById(R.id.beacon_toggle);
        repeat_info = (Switch) findViewById(R.id.repeat_info);

        event_cancel = (Button) findViewById(R.id.event_cancel);
        event_submit = (Button) findViewById(R.id.event_submit);

        final LinearLayout dayOfTime = (LinearLayout) findViewById(R.id.days_linear_layout);
        final LinearLayout date_time_layout = (LinearLayout) findViewById(R.id.date_time_layout);
        toggleButtonMon = (ToggleButton) findViewById(R.id.toggleButtonMon);
        toggleButtonTue = (ToggleButton) findViewById(R.id.toggleButtonTue);
        toggleButtonWed = (ToggleButton) findViewById(R.id.toggleButtonWed);
        toggleButtonThu = (ToggleButton) findViewById(R.id.toggleButtonThu);
        toggleButtonFri = (ToggleButton) findViewById(R.id.toggleButtonFri);
        toggleButtonSat = (ToggleButton) findViewById(R.id.toggleButtonSat);
        toggleButtonSun = (ToggleButton) findViewById(R.id.toggleButtonSun);

        // Initial Setting.

        assert date_time_layout != null;
        assert dayOfTime != null;

        beacon_toggle.setChecked(false);
        repeat_info.setChecked(false);
        Log.v("Main Activity", "onCreate: Set repeatinfo to 0");
        dayOfTime.setVisibility(View.GONE);
        Log.v("Main Activity", "onCreate: Set daytime to 0");

        // Getting spinner elements for Profile.
        profiles.add("Silent");
        profiles.add("Vibrate");
        profiles.add("Loud");
        profiles.add("Home");
        ArrayAdapter<String> profileAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, profiles);
        profile_spinner.setAdapter(profileAdapter);
        profile_spinner.setSelection(0);
        profile_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        beacons.add("Beacon ID1");
        beacons.add("Beacon ID2");
        beacons.add("Beacon ID3");
        ArrayAdapter<String> beaconAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, beacons);
        beacon_spinner.setAdapter(beaconAdapter);
        beacon_spinner.setSelection(0);
        beacon_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        updateDateText(datePicker);
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                datePicker.set(Calendar.YEAR, year);
                datePicker.set(Calendar.MONTH, monthOfYear);
                datePicker.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateText(datePicker);
            }
        };
        editDate.setOnClickListener(new View.OnClickListener() {
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
        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            fromTime.setText(hourOfDay + ":" + minute);
                    }
                }, timePicker.get(Calendar.HOUR_OF_DAY), timePicker.get(Calendar.MINUTE), true);
                mTimePicker.setTitle("From Time");
                mTimePicker.show();
            }
        });

        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        toTime.setText(hourOfDay + ":" + minute);
                    }
                }, timePicker.get(Calendar.HOUR_OF_DAY), timePicker.get(Calendar.MINUTE), true);
                mTimePicker.setTitle("Time Upto");
                mTimePicker.show();
            }
        });

        //attach a listener to check for changes in state
        beacon_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    // Code for switch on.
                    beacon_spinner.setVisibility(View.VISIBLE);
                    repeat_info.setVisibility(View.GONE);
                    dayOfTime.setVisibility(View.GONE);
                    editDate.setVisibility(View.GONE);
                    date_time_layout.setVisibility(View.GONE);
                } else {
                    // Code for beacon switch off.
                    beacon_spinner.setVisibility(View.GONE);
                    date_time_layout.setVisibility(View.VISIBLE);
                    repeat_info.setVisibility(View.VISIBLE);
                    if (repeat_info.isChecked()) {
                        dayOfTime.setVisibility(View.VISIBLE);
                    } else {
                        editDate.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        if (beacon_toggle.isChecked()) {
            // Code for switch on.
            beacon_spinner.setVisibility(View.VISIBLE);
            repeat_info.setVisibility(View.GONE);
            dayOfTime.setVisibility(View.GONE);
            editDate.setVisibility(View.GONE);
            date_time_layout.setVisibility(View.GONE);
        } else {
            // Code for switch off.
            beacon_spinner.setVisibility(View.GONE);
            date_time_layout.setVisibility(View.VISIBLE);
            repeat_info.setVisibility(View.VISIBLE);
            if (repeat_info.isChecked()) {
                dayOfTime.setVisibility(View.VISIBLE);
            } else {
                editDate.setVisibility(View.VISIBLE);
            }
        }

        //attach a listener to check for changes in state
        repeat_info.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    // Code for switch on.
                    dayOfTime.setVisibility(View.VISIBLE);
                    editDate.setVisibility(View.GONE);
                } else {
                    // Code for beacon switch off.
                    dayOfTime.setVisibility(View.GONE);
                    editDate.setVisibility(View.VISIBLE);

                }
            }
        });

        if (repeat_info.isChecked()) {
            // Code for switch on.
            dayOfTime.setVisibility(View.VISIBLE);
            editDate.setVisibility(View.GONE);
        } else {
            // Code for switch off.
            dayOfTime.setVisibility(View.GONE);
            editDate.setVisibility(View.VISIBLE);
        }

        // On cancel button.
        event_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent resultIntent = new Intent();
                setResult(RESULT_FIRST_USER);
                finish();
            }
        });

        // On Submit button.
        event_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventObject = new EventData();
                eventObject.setName(eventName.getText().toString());                                // 1
                int flag = 0;
                eventObject.setStartTime(fromTime.getText().toString());                            // 2
                eventObject.setEndTime(toTime.getText().toString());                                // 3

                if (eventObject.getName().isEmpty()) {
                    eventName.setError("Enter a Name too!");
                    flag = 1;
                } else {
                    eventObject.setProfile(profile_spinner.getSelectedItem().toString());           // 4
                    if (beacon_toggle.isChecked()) {
                        eventObject.setBeaconId(beacon_spinner.getSelectedItem().toString());       // 5
                        eventObject.setRepeatFlag(0);                                           // 6
                        eventObject.setDate("0");                                                   // 7
                        eventObject.setRepeatArray("");                                             // 8
                    } else {
                        eventObject.setBeaconId("-1");                                              // 5
                        if (eventObject.getStartTime() == "") {
                            fromTime.setError("Choose start time!");
                            flag = 1;
                        }
                        if (eventObject.getEndTime() == "") {
                            toTime.setError("Choose End time!");
                            flag = 2;
                        }
                        if (repeat_info.isChecked()) {
                            eventObject.setRepeatFlag(1);                                        // 6
                            // Get marked days.
                            StringBuilder stringBuilder = new StringBuilder();
                            int temp;
                            temp = toggleButtonMon.isChecked() ? 1 : 0;
                            stringBuilder.append(Integer.toString(temp));
                            temp = toggleButtonTue.isChecked() ? 1 : 0;
                            stringBuilder.append(Integer.toString(temp));
                            temp = toggleButtonWed.isChecked() ? 1 : 0;
                            stringBuilder.append(Integer.toString(temp));
                            temp = toggleButtonThu.isChecked() ? 1 : 0;
                            stringBuilder.append(Integer.toString(temp));
                            temp = toggleButtonFri.isChecked() ? 1 : 0;
                            stringBuilder.append(Integer.toString(temp));
                            temp = toggleButtonSat.isChecked() ? 1 : 0;
                            stringBuilder.append(Integer.toString(temp));
                            temp = toggleButtonSun.isChecked() ? 1 : 0;
                            stringBuilder.append(Integer.toString(temp));
                            eventObject.setRepeatArray(stringBuilder.toString());                   // 8
                            if (eventObject.getRepeatArray() == "0000000") {
                                flag = 3;
                                fromTime.setError("Select some days below!");
                            }
                            eventObject.setDate("0");                                               // 7


                        } else {
                            eventObject.setRepeatFlag(0);                                       // 6
                            eventObject.setDate(editDate.getText().toString());                     // 7
                            eventObject.setRepeatArray("");                                         // 8
                            if (eventObject.getDate().isEmpty()) {
                                flag = 4;
                                editDate.setError("Input a date!");
                            }
                        }
                    }
                }


                if (flag == 0) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("Object",eventObject);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }

            }
        });
    }

    // Update date on edit Text.
    private void updateDateText(Calendar datePicker) {
        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        editDate.setText(sdf.format(datePicker.getTime()));
    }
}
