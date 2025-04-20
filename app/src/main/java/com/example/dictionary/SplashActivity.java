package com.example.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Delay for 2 seconds and move to ThemeSelectionActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, ThemeSelectionActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}
