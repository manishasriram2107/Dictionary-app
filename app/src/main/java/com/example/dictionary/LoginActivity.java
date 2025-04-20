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

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnLogin, btnSignup;
    ImageView imgMascot;
    TextView welcomeText;
    SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // use a layout with all views

        // Initialize DB helper
        dbHelper = new SQLiteHelper(this);

        // Views
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        imgMascot = findViewById(R.id.imgMascot);
        welcomeText = findViewById(R.id.textViewWelcome);

        // Animations
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        imgMascot.startAnimation(bounce);
        welcomeText.startAnimation(fadeIn);
        btnLogin.startAnimation(bounce);
        btnSignup.startAnimation(bounce);

        // Login button logic
        btnLogin.setOnClickListener(view -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (dbHelper.checkUser(username, password)) {
                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, AgeNameActivity.class); // Go to next page
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        // Signup button action
        btnSignup.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}
