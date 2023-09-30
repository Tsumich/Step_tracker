package com.example.fitness;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class LogActivity extends AppCompatActivity {
    EditText username, password;
    AppCompatButton signIn;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_log);
        super.onCreate(savedInstanceState);
        username = findViewById(R.id.log_username);
        password = findViewById(R.id.log_password);
        signIn = findViewById(R.id.log_button_signIn);
        DB = new DBHelper(this);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_userName = username.getText().toString();
                String new_password = password.getText().toString();

                if(TextUtils.isEmpty(new_userName) || TextUtils.isEmpty(new_password))
                    Toast.makeText(LogActivity.this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show();
                else {
                    Boolean checkUserPassword = DB.checkUsernamePassword(new_userName, new_password);
                    if (checkUserPassword==true){
                        Toast.makeText(LogActivity.this, "Успих", Toast.LENGTH_SHORT).show();

                        // сохраняем данные о пользователе
                        SharedPreferences sharedPref = getSharedPreferences("SavedUserData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("savedUsername", new_userName);
                        editor.putString("savedPassword", new_password);
                        editor.apply();
                        // загружаем новое окно
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LogActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
