package com.example.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AgeNameActivity extends AppCompatActivity {

    private EditText etName, etAge;
    private Button btnContinue;
    private ImageView mascotImage;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_name);

        // Linking UI
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        btnContinue = findViewById(R.id.btnContinue);
        mascotImage = findViewById(R.id.mascotImage);
        tvTitle = findViewById(R.id.tvTitle);

        // Load animations
        Animation bounceAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        Animation fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Apply animations
        mascotImage.startAnimation(bounceAnim);
        tvTitle.startAnimation(fadeInAnim);
        btnContinue.startAnimation(bounceAnim);

        // Button action
        btnContinue.setOnClickListener(view -> {
            String name = etName.getText().toString().trim();
            String age = etAge.getText().toString().trim();

            if (name.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "Please enter your name and age!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(AgeNameActivity.this, WelcomeActivity.class);
                intent.putExtra("userName", name);
                startActivity(intent);
                finish();
            }
        });
    }
}
