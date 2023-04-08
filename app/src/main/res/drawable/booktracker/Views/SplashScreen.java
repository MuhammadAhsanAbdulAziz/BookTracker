package com.example.booktracker.Views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.booktracker.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Thread thread = new Thread()
        {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent = new Intent(SplashScreen.this,Login.class);
                    startActivity(intent);
                    finish();
                }
                catch (Exception ex){
                    Toast.makeText(SplashScreen.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        };
        thread.start();
    }
}