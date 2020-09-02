package com.example.coursework2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Timer;
import java.util.TimerTask;

public class Loading extends AppCompatActivity {
    int counter=0;
    ProgressBar progressBar;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        progress();
    }

    public void progress(){
        progressBar=findViewById(R.id.progressBar);
        final int pro=progressBar.getProgress();
        final Timer timer=new Timer();

        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                counter++;
                progressBar.setProgress(counter);

                        if (counter==100){
                        timer.cancel();
                        intent=new Intent(Loading.this,MainActivity.class);
                        startActivity(intent);
                        }

            }
        };
        timer.schedule(timerTask,0,30);
    }
}
