package com.example.dictionary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dictionary.R;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Ensure this is correct

        // Check if ImageView 'logo' exists in activity_main.xml
        ImageView logo = findViewById(R.id.logo);

        // Delay for 3 seconds and move to Login/Signup Page
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, com.example.dictionary.LoginActivity.class);
            startActivity(intent);
            finish(); // Close splash screen
        }, SPLASH_TIME_OUT);
    }
    private void loadTheme() {
        SharedPreferences preferences = getSharedPreferences("AppTheme", MODE_PRIVATE);
        String themeName = preferences.getString("SelectedTheme", "Theme.Dictionary");

        switch (themeName) {
            case "Theme.Dictionary.Dark":
                setTheme(R.style.Theme_Dictionary_Dark);
                break;
            case "Theme.Dictionary.Blue":
                setTheme(R.style.Theme_Dictionary_Blue);
                break;
            case "Theme.Dictionary.Green":
                setTheme(R.style.Theme_Dictionary_Green);
                break;
            default:
                setTheme(R.style.Theme_Dictionary);
                break;
        }
    }
}
