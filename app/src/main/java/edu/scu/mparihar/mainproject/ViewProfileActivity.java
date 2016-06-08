package edu.scu.mparihar.mainproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ViewProfileActivity extends AppCompatActivity {

    TextView tv;
    EditText profName;
    Button ringtoneButton,save,cancel;
    Spinner profileTypeSpinner;
    SeekBar volumeSeekbar;
    int prog= 0;
    Uri ringtoneURI;
    Context context;
    AudioManager audioManager;
    TextView errorVol, errorRt;
    static String ringtoneAddress;
    int initialSetter = 0;
    int position;
    ProfileData profileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("Position");
        profileData = MainActivity.AllProfiles.get(position);
        context = this;

        // Initialisations. Setting all uneditable.
        errorVol = (TextView) findViewById(R.id.error_vol);
        errorRt = (TextView) findViewById(R.id.error_rt);
        profileTypeSpinner = (Spinner)findViewById(R.id.profile_type_spinner);
        ringtoneButton = (Button)findViewById(R.id.ringtone_button);
        save = (Button)findViewById(R.id.profile_save);
        volumeSeekbar = (SeekBar) findViewById(R.id.volume_seekbar);
        tv = (TextView)findViewById(R.id.textView) ;
        profName = (EditText) findViewById(R.id.profileName) ;
        cancel = (Button)findViewById(R.id.profile_cancel);

        final String[] currentSelection = new String[1];
        audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        int max_v = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        volumeSeekbar.setMax(max_v);

        // Putting current values.
        String temp = profileData.getVolume() + "/" + volumeSeekbar.getMax();
        tv.setText(temp);
        profName.setText(profileData.getName());
        if (profileData.getType().equals("Vibrate Mode")) {
            profileTypeSpinner.setSelection(1);
        } else if (profileData.getType().equals("Ringer Mode")) {
            profileTypeSpinner.setSelection(2);
        } else {
            profileTypeSpinner.setSelection(0);
        }
        Uri uri = Uri.parse("content://media" + profileData.getRingtone());
        Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
        String title = ringtone.getTitle(this);
        ringtoneButton.setText(title);
        volumeSeekbar.setProgress(profileData.getVolume());

        // On editing options.
        profileTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View v, int position, long id)
            {

                currentSelection[0] = profileTypeSpinner.getItemAtPosition(position).toString();
                if (currentSelection[0].equals("Silent")) {
                    volumeSeekbar.setProgress(0);
                    volumeSeekbar.setEnabled(false);
                    ringtoneButton.setEnabled(false);
                }
                if (currentSelection[0].equals("Vibrate Mode")) {
                    volumeSeekbar.setEnabled(false);
                    ringtoneButton.setEnabled(false);
                }
                if (currentSelection[0].equals("Ringer Mode")) {
                    if (initialSetter == 0) {
                        initialSetter = 1;
                    } else {
                        volumeSeekbar.setEnabled(true);
                        ringtoneButton.setEnabled(true);
                        Toast.makeText(context, "already Accessed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> arg0)
            {
                Log.v("routes", "nothing selected");
            }
        });

        volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                prog = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv.setText(prog + "/" + seekBar.getMax());

            }
        });

        // Setting uneditable.
        profName.setEnabled(false);
        ringtoneButton.setEnabled(false);
        profileTypeSpinner.setEnabled(false);
        save.setVisibility(View.INVISIBLE);
        volumeSeekbar.setEnabled(false);

        ringtoneButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Ringtone:");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_RINGTONE);
                startActivityForResult(intent, 999);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int flag;
                ProfileData profileObject = new ProfileData();
                profileObject.setName(profName.getText().toString());
                profileObject.setVolume(volumeSeekbar.getProgress());
                profileObject.setType(currentSelection[0]);
                if (ringtoneAddress == null) {
                    profileObject.setRingtone(profileData.getRingtone());
                } else {
                    profileObject.setRingtone(ringtoneAddress);
                }
                flag = 0;
                if (profileObject.getName().isEmpty()) {
                    profName.setError("Enter a name!");
                    flag = 1;
                } else {
                    if (profileObject.getType().equals("Ringer Mode")) {
                        if (profileObject.getVolume() == 0) {
                            flag = 1;
                            Snackbar snackbar = Snackbar.make(v, "Volume cannot be 0!", Snackbar.LENGTH_LONG);
                            snackbar.show();

                        }
                        if (profileObject.getRingtone().equals(R.string.tap_to_select_ringtone)) {
                            flag = 1;
                            Snackbar snackbar = Snackbar.make(v, "Select a Ringtone!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    } else {
                        profileObject.setRingtone("");
                        profileObject.setVolume(0);
                    }

                }
                if (flag == 0) {
                    MainActivity.AllProfiles.set(position, profileObject);
                    MainActivity.profileDbHelper.updateData(profileData.getId(), profileObject);

                    ProfileFragment.mRecyclerviewAdapterProfile.notifyDataSetChanged();

//                    Intent resultIntent = new Intent();
//                    resultIntent.putExtra("Profile", profileObject);
//                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
//                helper.insertData(profName.getText().toString(),"silent",ringtoneURI.toString(),prog);

            }
        });
    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        if (resultCode == RESULT_OK && requestCode == 999) {
            ringtoneURI = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (ringtoneURI != null) {
                try {
                    ringtoneButton.setText(RingtoneManager.getRingtone(this, ringtoneURI).getTitle(this));
                    ringtoneAddress = ringtoneURI.getPath();

                } catch (final Exception e) {
                    ringtoneButton.setText("Unknown. Select Again");
                }

            }
        }


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
            MainActivity.eventDbHelper.deleteData(profileData.getId());
            EventFragment.mRecyclerviewAdapterEvent.notifyDataSetChanged();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeEditable() {
        profName.setEnabled(true);
        profileTypeSpinner.setEnabled(true);
        save.setVisibility(View.VISIBLE);
        if (profileData.getType().equals("Ringer Mode")) {
            volumeSeekbar.setEnabled(true);
            ringtoneButton.setEnabled(true);
        }
    }
}
