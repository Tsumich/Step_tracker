package com.example.fitness;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
                // загружаем хранилище
                SharedPreferences sharedPref = getSharedPreferences("SavedUserData", Context.MODE_PRIVATE);
                // достаем логин
                String savedUsername = (String) sharedPref.getString("savedUsername", "0");
                System.out.println(savedUsername + " papipo");
                // загружаем экран в зависимости от авторизации
                if(savedUsername.equals("0") || savedUsername==null){
                    System.out.println("Пользователь не найден");
                    startActivity(new Intent(getApplicationContext(), RegActivity.class));
                    finish();
                }else{
                    System.out.println("Пользователь " + savedUsername + " найден");
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }

            }
        }, 2500);



    }

}
