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
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager = null;
    private Sensor stepSensor;
    private int totalSteps = 0, previewsSteps = 0;
    public int monday = 0, tuesday = 0, wednesday = 0, thursday = 0 , friday = 0, saturday = 0, sunday = 0;
    private TextView steps;
    private ProgressBar progressBar;
    private Button btn;
    public static ArrayList weekStepsList = new ArrayList<>();

    // текущая дата
    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
    Date d = new Date();
    public String dayOfTheWeek = sdf.format(d); //текущий день недели

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("On create: день недели" +  dayOfTheWeek);

        steps = findViewById(R.id.steps);
        progressBar = findViewById(R.id.progressBar);
        btn = findViewById(R.id.button_to_graph);


        // проверка дня
        SharedPreferences checkDayPref = getSharedPreferences("saveWeekDay", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = checkDayPref.edit();
        // достаем день из хранилища
        String savedDay = (String) checkDayPref.getString("savedDay", "");

        if(checkDay( dayOfTheWeek,  savedDay,  checkDayPref, editor)){
            System.out.println("обнуление шагов");
            //previewsSteps = totalSteps;
            totalSteps = -1;
            previewsSteps = 0;
            steps.setText("0");
            progressBar.setProgress(0);
            //Для очистки значений используйте методы SharedPreferences.Editor.remove(String key) и
            checkDayPref.edit().remove("savedDay").commit(); // очищаем ключ
            editor.putString("savedDay", dayOfTheWeek); // кладем новый день
            editor.apply();
            saveData();
        }else{
            System.out.println("День все тот же - " + savedDay);
            //steps.setText(String.valueOf(loadData()));
            totalSteps = loadData() - 1;
        }

        resetSteps();

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        stepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    protected void onResume(){
        super.onResume();
        if (stepSensor == null){
            Toast.makeText(this, "Ваше устройство не имеет сенсора", Toast.LENGTH_SHORT).show();
        }else{
            mSensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    protected void onPause(){
        super.onPause();
        saveData();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            //totalSteps = (int) event.values[0];
            totalSteps ++;
            System.out.println("Sensor Changed" + totalSteps);
            steps.setText(String.valueOf(totalSteps));
            progressBar.setProgress(totalSteps);

        }
    }

    private void resetSteps(){

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                SharedPreferences getWeekSteps = getSharedPreferences("myPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = getWeekSteps.edit();

                // достаем дни из хранилища
                monday = getWeekSteps.getInt("monday", 0);
                tuesday = getWeekSteps.getInt("tuesday", 0);
                sunday = getWeekSteps.getInt("sunday", 0);

                weekStepsList.add(monday);
                weekStepsList.add(tuesday);
                weekStepsList.add(sunday);

                startActivity(new Intent(getApplicationContext(), GraphActivity.class));
            }
        });

        steps.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(MainActivity.this, "Удерживайте, чтобы сбросить шаги",Toast.LENGTH_SHORT).show();
            }
        });

        steps.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                previewsSteps = totalSteps;
                steps.setText("0");
                progressBar.setProgress(0);
                saveData();
                return true;
            }
        });
    }

    private void saveData(){
        SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("key1", totalSteps);
        switch(dayOfTheWeek){
            case "понедельник":
                System.out.println("Значение сохранено по ключу monday");
                editor.putInt("monday", totalSteps);
                break;
            case "вторник":
                System.out.println("Значение сохранено по ключу tuesday");
                editor.putInt("tuesday", totalSteps);
                break;
            case "воскресенье":
                System.out.println("Значение сохранено по ключу sunday");
                editor.putInt("sunday", totalSteps);
                break;
        }

        System.out.println("save data,  total " + totalSteps);
        editor.apply();
    }

    public boolean checkDay(String curDay, String savedDay, SharedPreferences checkDayPref, SharedPreferences.Editor editor){
        if ( !curDay.equals(savedDay)  ) {
            System.out.println("новый день - " + dayOfTheWeek + " вчера был " + savedDay );
            return true;
        }else return false;
    }

    private int loadData(){
        SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        previewsSteps = (int) sharedPref.getInt("key1", 0);
        System.out.println("load data prew step: " + previewsSteps);
        //previewsSteps = Integer.parseInt(sharedPref.getString("myPref", ""));
        return previewsSteps;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}