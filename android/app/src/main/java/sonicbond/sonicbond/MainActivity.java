package sonicbond.sonicbond;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;

/*
Summary: UIManager & MainActivity Module for Sonic Bond
*Description: This MainActivity manages the primary view for the application
*This class also contains the private class UIManager which operates the main functions of our
*created Software
*
* Author: Roger Herzfeldt
* Date: 3/31/2018
 */

public class MainActivity extends AppCompatActivity {

    private UiManager ui;

    private AudioManager mAudioManager;

    private Button mBtnSettings;
    private Switch switchMute;
    private Switch switchVibration;
    private SeekBar volumeSeekBar = null;

    private Button mBtnStart;
    private Button mBtnStop;

    private Button mBtnClear;

    //This MainActivity is the window that is opened when the application is started.
    //MainActivity holds button context and maintains the user of the UiManager.
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);

        volumeSeekBar = findViewById(R.id.seekBar);
        volumeSeekBar.setActivated(false);
        volumeSeekBar.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeSeekBar.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        switchMute = findViewById(R.id.switchMute);
        if (mAudioManager.getRingerMode() == mAudioManager.RINGER_MODE_SILENT)
            switchMute.setChecked(true);
        else
            switchMute.setChecked(false);

        switchMute.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if (switchMute.isChecked()) {
                    switchMute.setChecked(false);
                }
                else{
                    switchMute.setChecked(true);
                }
            }
        });

        switchVibration = findViewById(R.id.switchVibration);
        if (mAudioManager.getRingerMode() == mAudioManager.RINGER_MODE_VIBRATE)
            switchVibration.setChecked(true);
        else
            switchVibration.setChecked(false);

        switchMute.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if (switchVibration.isChecked()) {
                    switchVibration.setChecked(false);
                }
                else{
                    switchVibration.setChecked(true);
                }
            }
        });


        mBtnSettings = findViewById(R.id.btnSettings);
        mBtnSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                volumeSeekBar.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM));

                if (volumeSeekBar.getVisibility() == View.INVISIBLE){
                    volumeSeekBar.setActivated(true);
                    volumeSeekBar.setVisibility(View.VISIBLE);
                    switchMute.setVisibility(View.VISIBLE);
                    switchVibration.setVisibility(View.VISIBLE);
                }
                else{
                    volumeSeekBar.setActivated(false);
                    volumeSeekBar.setVisibility(View.INVISIBLE);}
                    mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM,
                        volumeSeekBar.getProgress(), 0);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_RING,
                        volumeSeekBar.getProgress(), 0);

                    switchMute.setVisibility(View.INVISIBLE);
                    switchVibration.setVisibility(View.INVISIBLE);
                }
        });




        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onStopTrackingTouch(SeekBar arg0)
            {
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0)
            {

            }

            @Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
            {
                mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM,
                        progress, 0);
            }
        });

        mBtnStart = findViewById(R.id.btnStart);
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if (mBtnStop.getVisibility() == View.INVISIBLE){
                    mBtnStop.setActivated(true);
                    mBtnStop.setVisibility(View.VISIBLE);
                    mBtnStart.setVisibility(View.INVISIBLE);
                }

            }
        });
        mBtnStop = findViewById(R.id.btnStop);
        mBtnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if (mBtnStart.getVisibility() == View.INVISIBLE){
                    mBtnStart.setActivated(true);
                    mBtnStart.setVisibility(View.VISIBLE);
                    mBtnStop.setVisibility(View.INVISIBLE);
                }
            }
        });
        mBtnClear = findViewById(R.id.btnClear);
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                //Clear Alert, Stop Alarm
                //ui.ClearAlert();
            }
        });

        ui = new UiManager(); //R.layout.activity_main.txtUser.text, txtPass);
        ui.Init();
    }

    @Override
    protected void onDestroy(){
        ui.LogOut();
        super.onDestroy();
    }

    private class UiManager {

        public String UserName;
        private void SetUserName(String value)
        {
            this.UserName = EncryptCredentials(value);
        }
        private String GetUserName()
        {
            return this.UserName;
        }
        public String Password;
        private void SetPassword(String value)
        {
            this.Password = EncryptCredentials(value);
        }
        private String GetPassword()
        {
            return this.Password;
        }
        private String EncryptCredentials(String value)
        {
            return value;
        }


        //Stubbed, API needed from development for other modules
        //AlertManager Alert;
        //private boolean InitializeAlertManager()  { return true;}
        //ServerManager Server;
        //private boolean InitializeServerManager()  { return true;}

        public UiManager() // String UserName, String PassWord)
        {
            SetUserName("defaultuser");
            SetPassword("defaultpw");
        }

        public boolean Init()
        {
            //Set up Networking Object
            //Server = new ServerManager();
            if (true) //ServerManager.LogIn(UserName, Password))
            {
                //Login and connected to server
                //Display log in formation
                //Set parameters to relay alert information automatically
            }
            else
            {
                //Server = null;
                //Unable to Connect
                //Start Listening app and set for non-networked mode

            }
            //Set up Listening Object
            //InitializeAlertManager();
            return true;
        }

        public void LogOut()
        {
            //Server = null;
            //Alert = null;
            UserName = null;
            Password = null;
        }

    }
}
