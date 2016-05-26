package edu.scu.mparihar.mainproject;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


public class AddProfile extends AppCompatActivity {
    TextView tv;
    EditText prof;
    Button b,save,cancel;
    Spinner s;
    SeekBar seekBar;
    ProfileDbHelper helper;
    int prog= 0;
    Uri ringtoneURI;
    Context context;
    AudioManager audioManager;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_profile);
        context = this;
        helper = new ProfileDbHelper(context);
        s = (Spinner)findViewById(R.id.spinner);
        b = (Button)findViewById(R.id.button);
        save = (Button)findViewById(R.id.button3);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        tv = (TextView)findViewById(R.id.textView) ;
        prof = (EditText) findViewById(R.id.editText) ;
        cancel = (Button)findViewById(R.id.button2);
        audioManager = (AudioManager)getSystemService(context.AUDIO_SERVICE);
        int max_v = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        seekBar.setMax(max_v);
        tv.setText( seekBar.getProgress() + "/" + seekBar.getMax());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

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
                helper.insertData(prof.getText().toString(),"silent",ringtoneURI.toString(),prog);

            }
        });


        b.setOnClickListener(new View.OnClickListener()
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

        if (resultCode == RESULT_OK) {
            ringtoneURI = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (ringtoneURI != null) {
                try {
                    b.setText(RingtoneManager.getRingtone(this, ringtoneURI).getTitle(this));

                } catch (final Exception e) {
                    b.setText("Unkown");
                }

            }
        }


    }



}
