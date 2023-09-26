package com.example.fitness;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    ImageView logo;
    TextView logo_title;
    Animation up, down;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        logo = findViewById(R.id.logo);
        logo_title = findViewById(R.id.appname);

        up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up);
        logo.setAnimation(up);

        down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down);
        logo_title.setAnimation(down);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }, 2500);



    }

}
