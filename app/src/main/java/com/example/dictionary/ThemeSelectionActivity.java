package com.example.dictionary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ThemeSelectionActivity extends AppCompatActivity {

    private Button btnDark, btnBlue, btnGreen, btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_selection);

        btnDark = findViewById(R.id.btnDark);
        btnBlue = findViewById(R.id.btnBlue);
        btnGreen = findViewById(R.id.btnGreen);
        btnContinue = findViewById(R.id.btnContinue); // Ensure button exists in XML

        btnDark.setOnClickListener(view -> saveTheme("Theme.Dictionary.Dark"));
        btnBlue.setOnClickListener(view -> saveTheme("Theme.Dictionary.Blue"));
        btnGreen.setOnClickListener(view -> saveTheme("Theme.Dictionary.Green"));

        // When "Continue" is clicked, navigate to LoginActivity
        btnContinue.setOnClickListener(view -> {
            Intent intent = new Intent(ThemeSelectionActivity.this, WordLearningActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void saveTheme(String theme) {
        SharedPreferences preferences = getSharedPreferences("AppTheme", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("SelectedTheme", theme);
        editor.apply();

        // Reload WordLearningActivity with the new theme
        startActivity(new Intent(this, WordLearningActivity.class));
        finish();
    }

    private void loadTheme() {
        SharedPreferences preferences = getSharedPreferences("AppTheme", MODE_PRIVATE);
        String themeName = preferences.getString("SelectedTheme", "Theme.Dictionary");

        // Debugging: Print the theme to Logcat
        System.out.println("Loaded Theme: " + themeName);

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
