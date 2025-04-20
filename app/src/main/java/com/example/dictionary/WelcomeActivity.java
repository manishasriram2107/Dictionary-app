package com.example.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private TextView welcomeMessage, funMessage;
    private ImageView welcomeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeMessage = findViewById(R.id.welcomeMessage);
        funMessage = findViewById(R.id.funMessage);
        welcomeImage = findViewById(R.id.welcomeImage);

        String userName = getIntent().getStringExtra("userName");
        welcomeMessage.setText("Hey, " + userName + "! 🚀");
        funMessage.setText("Ready to learn some wicked words? 😎📚");

        // Animate image and texts
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        welcomeImage.startAnimation(bounce);
        welcomeMessage.startAnimation(fadeIn);
        funMessage.startAnimation(fadeIn);

        // Delay to go to next activity
        new Handler().postDelayed(() -> {
            startActivity(new Intent(WelcomeActivity.this, ThemeSelectionActivity.class));
            finish();
        }, 3000);
    }
}
