package com.example.secureaty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

public class Splash_Screen_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Show the splash screen
        setContentView(R.layout.activity_splash__screen_);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            //if any code written in the run() method, itll run after 4sec=4000msec of this splash activity
            public void run() {

                startActivity(new Intent(Splash_Screen_Activity.this, MainActivity.class));
                finish();
            }
        }, 4000);
    }
}