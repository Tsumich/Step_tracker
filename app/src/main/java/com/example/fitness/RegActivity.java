package com.example.fitness;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class RegActivity extends AppCompatActivity {
    EditText username, password, name;
    Spinner work;
    AppCompatButton signUp, signIn;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        username = findViewById(R.id.reg_username);
        password = findViewById(R.id.reg_password);
        name = findViewById(R.id.reg_name);
        work = findViewById(R.id.reg_work);

        signUp = findViewById(R.id.reg_button_signUp);
        signIn = findViewById(R.id.reg_button_signIn);

        DB = new DBHelper(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_userName = username.getText().toString();
                String new_password = password.getText().toString();
                String new_name = name.getText().toString();
                String new_work = work.getSelectedItem().toString();

                if(TextUtils.isEmpty(new_userName) || TextUtils.isEmpty(new_password) || TextUtils.isEmpty(new_name) || TextUtils.isEmpty(new_work))
                    Toast.makeText(RegActivity.this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkUser = DB.checkUsername(new_userName);
                    if (checkUser==false) { // если в базе нет такого логина
                        Boolean insert = DB.insertData(new_userName, new_password, new_name, new_work);
                        if (insert == true) {
                            Toast.makeText(RegActivity.this, "регистрация успешна", Toast.LENGTH_SHORT).show();
                            // сохраняем данные о пользователе
                            SharedPreferences sharedPref = getSharedPreferences("SavedUserData", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("savedUsername", new_userName);
                            editor.putString("savedPassword", new_password);
                            editor.putString("savedName", new_name);
                            editor.putString("savedWork", new_work);
                            editor.apply();
                            System.out.println(new_userName + " - зарегистрирован");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(RegActivity.this, "Что то пошло не так", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegActivity.this, "Такой пользователь уже существует", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LogActivity.class);
                startActivity(intent);
            }
        });
    }
}
