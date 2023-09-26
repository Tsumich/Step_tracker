package com.example.fitness;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {

    // пустой лист для гистограммы
    BarChart histogram;
    Button btn;
    private ArrayList<BarEntry> histogram_list = new ArrayList<>();
    private int monday = 0, tuesday = 0, wednesday = 0, thursday = 0 , friday = 0, saturday = 0, sunday = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        btn = findViewById(R.id.btn_to_main_screen);
        BarChart histogram = findViewById(R.id.histogram);
        getData();
        BarDataSet barDataSet = new BarDataSet(histogram_list, "Ваш прогресс за неделю");
        BarData barData = new BarData(barDataSet);
        histogram.setData(barData);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);


        histogram.getDescription().setText(""); //описание справа
        barDataSet.setDrawValues(false);
        barDataSet.setValueTextSize(16f);

        backBtn();



    }

    private void getData(){
        monday = (int) MainActivity.weekStepsList.get(0);
        tuesday = (int) MainActivity.weekStepsList.get(1);
        sunday = (int) MainActivity.weekStepsList.get(2);

        histogram_list.add(new BarEntry(1f,monday));
        histogram_list.add(new BarEntry(2f,tuesday));
        histogram_list.add(new BarEntry(3f,wednesday));
        histogram_list.add(new BarEntry(4f,saturday));
        histogram_list.add(new BarEntry(5f,friday));
        histogram_list.add(new BarEntry(6f,saturday));
        histogram_list.add(new BarEntry(7f,sunday));
    }

    public void backBtn(){
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

}
