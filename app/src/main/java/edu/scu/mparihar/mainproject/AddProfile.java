package edu.scu.mparihar.mainproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class AddProfile extends AppCompatActivity {
    TextView tv;
    EditText profName;
    Button ringtoneButton,save,cancel;
    Spinner profileTypeSpinner;
    SeekBar volumeSeekbar;
    ProfileDbHelper helper;
    int prog= 0;
    Uri ringtoneURI;
    Context context;
    AudioManager audioManager;
    TextView errorVol, errorRt;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_profile);
        context = this;
//        helper = new ProfileDbHelper(context);
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
        audioManager = (AudioManager)getSystemService(context.AUDIO_SERVICE);
        int max_v = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        volumeSeekbar.setMax(max_v);
        tv.setText( volumeSeekbar.getProgress() + "/" + volumeSeekbar.getMax());


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
                    volumeSeekbar.setEnabled(true);
                    ringtoneButton.setEnabled(true);
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
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int flag;
                ProfileData profileObject = new ProfileData();
                profileObject.setName(profName.getText().toString());
                profileObject.setVolume(volumeSeekbar.getProgress());
                profileObject.setType(currentSelection[0]);
                profileObject.setRingtone(ringtoneButton.getText().toString());
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
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("Profile", profileObject);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
//                helper.insertData(profName.getText().toString(),"silent",ringtoneURI.toString(),prog);

            }
        });


        ringtoneButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Ringtone:");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_ALARM);
                startActivityForResult(intent, 999);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        if (resultCode == RESULT_OK && requestCode == 999) {
            ringtoneURI = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (ringtoneURI != null) {
                try {
                    ringtoneButton.setText(RingtoneManager.getRingtone(this, ringtoneURI).getTitle(this));

                } catch (final Exception e) {
                    ringtoneButton.setText("Unkown");
                }

            }
        }


    }



}
