package com.example.booktracker.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.booktracker.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity   {

    NavController navController;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        NavHostFragment navHost = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

        navController = navHost.getNavController();
        bottomNavigationView = findViewById(R.id.bottom_nav);

        NavigationUI.setupWithNavController(bottomNavigationView,navController);
        setBackgroundColor();
    }

    public void setFontSize(int size) {
        Resources res = getResources();
        Configuration configuration = res.getConfiguration();
        configuration.fontScale = size / (float) 100;
        res.updateConfiguration(configuration, res.getDisplayMetrics());
        recreate();
    }
    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, String.valueOf(1.0));
    }

    public void setBackgroundColor()
    {
        String color = getDefaults("color",this);

        if(color.equals("red"))
        {
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
            bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.red));
        }
        else if(color.equals("green"))
        {
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
            bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.green));
        }
        else
        {
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
            bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.myTheme));
        }
    }
}