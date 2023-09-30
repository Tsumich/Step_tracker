package com.example.fitness;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {

    // пустой лист для гистограммы
    BarChart histogram;
    AppCompatButton btn, NO, YES;
    TextView stepsAll, nameUser, check_empty, panelToPay, money;
    RelativeLayout layerToPay;
    ImageView count_50k, count_100k, count_150k;
    private ArrayList<BarEntry> histogram_list = new ArrayList<>();
    int todayStepCounter = 0, totalStepCounter, totalStepForRecord;
    private int monday = 0, tuesday = 0, wednesday = 0, thursday = 0 , friday = 0, saturday = 0, sunday = 0;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DB = new DBHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        btn = findViewById(R.id.button_to_pay);
        stepsAll = findViewById(R.id.profile_title);
        nameUser = findViewById(R.id.profile_name);
        count_50k = findViewById(R.id.count_50k);
        count_100k = findViewById(R.id.count_100k);
        count_150k = findViewById(R.id.count_150k);
        check_empty = findViewById(R.id.check_empty);
        panelToPay = findViewById(R.id.q_to_pay);
        money = findViewById(R.id.money_to_pay);
        layerToPay = (RelativeLayout) findViewById(R.id.layer_q_to_pay);
        NO =  findViewById(R.id.btn_no);
        YES =  findViewById(R.id.btn_yes);


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
        getData();

    }

    private void getData(){
        // данные об имени
        SharedPreferences sharedPref = getSharedPreferences("SavedUserData", Context.MODE_PRIVATE);
        String userName = sharedPref.getString("savedName", "noName");
        //String savedName = (String) storage.getString("savedUsername", "0");
        //String nameFromDB = DB.getName(savedName);
        nameUser.setText(userName + ", your achievements");
        payBtn(userName);

        monday = (int) MainActivity.weekStepsList.get(0);
        tuesday = (int) MainActivity.weekStepsList.get(1);
        wednesday = (int) MainActivity.weekStepsList.get(2);
        thursday = (int) MainActivity.weekStepsList.get(3);
        friday = (int) MainActivity.weekStepsList.get(4);
        saturday = (int) MainActivity.weekStepsList.get(5);
        sunday = (int) MainActivity.weekStepsList.get(6);


        todayStepCounter = (int) MainActivity.weekStepsList.get(7);
        totalStepCounter = (int) MainActivity.weekStepsList.get(8);
        //totalStepForRecord = (int) MainActivity.weekStepsList.get(5);

        stepsAll.setText(String.valueOf("Total steps: " + totalStepCounter));

        histogram_list.add(new BarEntry(1f,monday));
        histogram_list.add(new BarEntry(2f,tuesday));
        histogram_list.add(new BarEntry(3f,wednesday));
        histogram_list.add(new BarEntry(4f,thursday));
        histogram_list.add(new BarEntry(5f,friday));
        histogram_list.add(new BarEntry(6f,saturday));
        histogram_list.add(new BarEntry(7f,sunday));
        //System.out.println("рекорд " + totalStepForRecord);
        // отрисовка достижений
        if (totalStepCounter > 1000) {
            count_50k.setVisibility(View.VISIBLE);
            check_empty.setVisibility(View.GONE);
        }
        if (totalStepCounter > 2000) count_100k.setVisibility(View.VISIBLE);
        if (totalStepCounter > 3000) count_150k.setVisibility(View.VISIBLE);
    }

    public void payBtn(String userName){
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
               // startActivity(new Intent(getApplicationContext(), MainActivity.class));
                if (totalStepCounter < 1000){
                    System.out.println("not enough steps?" + totalStepCounter);
                    Toast.makeText(GraphActivity.this, "cats wanna more steps!!!",Toast.LENGTH_SHORT).show();
                }else{
                    System.out.println("ви круттой" + totalStepCounter);
                    layerToPay.setVisibility(View.VISIBLE);
                    money.setText(totalStepCounter / 1000 + " rub.");
                    panelToPay.setText(userName + ", do you want to transfer the accumulated money?");
                }

            }
        });
        NO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layerToPay.setVisibility(View.INVISIBLE);
            }
        });
        YES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FundActivity.class);
                startActivity(intent);
            }
        });


    }


}
