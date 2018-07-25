package com.codedbyte.countdowntimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //initializing ....
    SeekBar seekBar;
    TextView timer;
    ImageView startPause, reset;
    CountDownTimer countDownTimer;
    int timeleft;
    MediaPlayer mediaPlayer;

    boolean timerIsActive, isFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        seekBar = (SeekBar) findViewById(R.id.seekbar);
        timer = (TextView) findViewById(R.id.timer);
        startPause = (ImageView) findViewById(R.id.startpause);
        reset = (ImageView) findViewById(R.id.reset);

        seekBar.setMax(600);
        seekBar.setProgress(0);

        disableButton(startPause);
        disableButton(reset);

        //seekbar listener to keep track of the progress and update the timer
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                timeleft = seekBar.getProgress() * 1000;
                updatetimer(timeleft);
                if (seekBar.getProgress() == 0){
                    disableButton(startPause);
                    disableButton(reset);
                }else {
                    enableButton(startPause);
                    enableButton(reset);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //start the timer
        startPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check if the timer is already active or not
                if(timerIsActive){
                    pause();
                }else {
                    start();
                }
            }
        });

        //reset the timer
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBar.setEnabled(true);
                if(countDownTimer!=null){
                    countDownTimer.cancel();
                }
                timerIsActive = false;
                startPause.setImageResource(R.drawable.start);
                seekBar.setProgress(0);
                timeleft = seekBar.getProgress()*1000;
                updatetimer(timeleft);
                if(mediaPlayer!=null){
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }
                }
            }
        });
    }

    //starting the countdown timerr
    private void start(){
        seekBar.setEnabled(false);
            countDownTimer = new CountDownTimer(timeleft, 1000) {
                @Override
                public void onTick(long l) {
                    timeleft = (int)l;
                    updatetimer(timeleft);
                }

                @Override
                public void onFinish() {
                    timer.setText("0 : 00");
                    seekBar.setEnabled(true);
                    startPause.setImageResource(R.drawable.start);
                    disableButton(startPause);
                    disableButton(reset);
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ring);
                    mediaPlayer.start();
                }
            }.start();
            timerIsActive = true;
            startPause.setImageResource(R.drawable.stop);
    }

    //pausing the countdown timer
    private void pause(){
        countDownTimer.cancel();
        timerIsActive = false;
        startPause.setImageResource(R.drawable.start);
        if(mediaPlayer!=null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
        }

    }

    //updating the seekbar and the countdown timer
    private void updatetimer(int timeleft){
        int minutes = (timeleft/1000) / 60;
        int seconds = (timeleft/1000) % 60;
        String scondString = String.valueOf(seconds);
        String minuteString = String.valueOf(minutes);
        if(seconds<=9){
            scondString = "0" + seconds;}
        timer.setText(minuteString + " : " + scondString);
        seekBar.setProgress(timeleft/1000);
    }

    //disabling the button
    private void disableButton(ImageView view){
        view.setAlpha(128);
        view.setEnabled(false);
    }

    //enabling the button
    private void enableButton(ImageView view){
        view.setAlpha(225);
        view.setEnabled(true);
    }
}
