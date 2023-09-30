package com.example.fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager = null;
    private Sensor stepSensor;
    private int totalSteps = 0, previewsSteps = 0, totalUserSteps = 0, totalUserStepsForRecord = 0;
    public int monday = 0, tuesday = 0, wednesday = 0, thursday = 0 , friday = 0, saturday = 0, sunday = 0;
    private TextView steps, distance;
    private ProgressBar progressBar;
    private ImageButton btn;
    public static ArrayList weekStepsList = new ArrayList<>();
    public static boolean todayMonday = false;

    // текущая дата
    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
    Date d = new Date();
    public String dayOfTheWeek = sdf.format(d); //текущий день недели

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadAllSteps();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("On create: день недели" +  dayOfTheWeek);
        steps = findViewById(R.id.steps);
        distance = findViewById(R.id.distance);

        progressBar = findViewById(R.id.progressBar);
        btn = findViewById(R.id.button_to_graph);
        resetSteps();
        // проверка дня
        SharedPreferences checkDayPref = getSharedPreferences("saveWeekDay", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = checkDayPref.edit();
        // достаем день из хранилища
        String savedDay = (String) checkDayPref.getString("savedDay", "");

        if(checkDay( dayOfTheWeek,  savedDay,  checkDayPref, editor)){
            System.out.println("обнуление шагов");
            //previewsSteps = totalSteps;
            previewsSteps = -1;
            steps.setText("0");
            progressBar.setProgress(0);
            //Для очистки значений используйте методы SharedPreferences.Editor.remove(String key) и
            checkDayPref.edit().remove("savedDay").commit(); // очищаем ключ
            editor.putString("savedDay", dayOfTheWeek); // кладем новый день
            editor.apply();
           // saveData();
        }else{
            System.out.println("День все тот же - " + savedDay);
            //steps.setText(String.valueOf(loadData()));
            totalSteps = 0;
            loadData();
        }
     mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        stepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    protected void onResume(){
        System.out.println("onResume");
        super.onResume();
        if (stepSensor == null){
            Toast.makeText(this, "Ваше устройство не имеет сенсора", Toast.LENGTH_SHORT).show();
        }else{
            mSensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        saveData();
    }

    protected void onPause(){
        super.onPause();
        saveData();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            totalSteps = (int) event.values[0];

            previewsSteps ++;
            totalUserSteps ++;

            steps.setText(String.valueOf(previewsSteps));
            distance.setText(String.valueOf(previewsSteps * 1.5 / 1000 + " km" ));

            progressBar.setProgress((previewsSteps * 100)/ 6000);
            //saveData();
        }
    }

    private void resetSteps(){
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                saveData();
                loadAllSteps();
                SharedPreferences getWeekSteps = getSharedPreferences("myPref", Context.MODE_PRIVATE);
                totalUserSteps = (int) getWeekSteps.getInt("key2", 0);
                SharedPreferences.Editor editor = getWeekSteps.edit();

                // достаем дни из хранилища
                monday = getWeekSteps.getInt("monday", 0);
                tuesday = getWeekSteps.getInt("tuesday", 0);
                wednesday = getWeekSteps.getInt("wednesday", 0);
                thursday = getWeekSteps.getInt("thursday", 0);
                friday = getWeekSteps.getInt("friday", 0);
                saturday = getWeekSteps.getInt("saturday", 0);
                sunday = getWeekSteps.getInt("sunday", 0);

                weekStepsList.add(monday);
                weekStepsList.add(tuesday);
                weekStepsList.add(wednesday);
                weekStepsList.add(thursday);
                weekStepsList.add(friday);
                weekStepsList.add(saturday);
                weekStepsList.add(sunday);

                weekStepsList.add(previewsSteps); //7 элемент
                weekStepsList.add(totalUserSteps); //8 элемент
                //weekStepsList.add(totalUserStepsForRecord); //9 элемент
                startActivity(new Intent(getApplicationContext(), GraphActivity.class));
            }
        });

        steps.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(MainActivity.this, "Чем больше шагов, тем лучше живется котикам!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveData(){
        SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("key1", previewsSteps); // шаги за сегодня
        editor.apply();

        System.out.println("тотал перед сейв " + totalUserSteps);
        totalUserSteps = totalUserSteps;
        editor.putInt("key2", totalUserSteps); //шаги за все время
        editor.apply();

        System.out.println("save data prew step: " + previewsSteps);
        System.out.println("save data totalUserSteps step: " + totalUserSteps);
       // System.out.println("save data totalUserStepsForRecord step: " + totalUserStepsForRecord);

        switch(dayOfTheWeek){
            case "понедельник":
                todayMonday = true;
                System.out.println("Значение сохранено по ключу monday");
                editor.putInt("monday", previewsSteps);
                break;
            case "вторник":
                System.out.println("Значение сохранено по ключу tuesday");
                editor.putInt("tuesday", previewsSteps);
                break;
            case "среда":
                System.out.println("Значение сохранено по ключу wed");
                editor.putInt("wednesday", previewsSteps);
                break;
            case "четверг":
                System.out.println("Значение сохранено по ключу thursday");
                editor.putInt("thursday", previewsSteps);
                break;
            case "пятница":
                System.out.println("Значение сохранено по ключу friday");
                editor.putInt("friday", previewsSteps);
                break;
            case "суббота":
                System.out.println("Значение сохранено по ключу saturday");
                editor.putInt("saturday", previewsSteps);
                break;
            case "воскресенье":
                System.out.println("Значение сохранено по ключу sunday");
                editor.putInt("sunday", previewsSteps);
                break;
        }

        System.out.println("save data,  total " + previewsSteps);
        editor.apply();
    }

    public boolean checkDay(String curDay, String savedDay, SharedPreferences checkDayPref, SharedPreferences.Editor editor){
        if ( !curDay.equals(savedDay)  ) {
            System.out.println("новый день - " + dayOfTheWeek + " вчера был " + savedDay );
            return true;
        }else return false;
    }

    private void loadData(){
        SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        int savedData = (int) sharedPref.getInt("key1", 0);
        previewsSteps = savedData;
        System.out.println("load data prew step: " + previewsSteps);
    }

    public void loadAllSteps(){
        SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        totalUserSteps = (int) sharedPref.getInt("key2", 0);
        System.out.println("load data totalUserSteps step: " + totalUserSteps);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}